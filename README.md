import java.util.*;

// Booking Request class
class BookingRequest {
String customerName;
String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Shared Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    public BookingSystem() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Add request (synchronized)
    public synchronized void addRequest(BookingRequest request) {
        requestQueue.add(request);
        notify(); // notify waiting threads
    }

    // Process request (critical section)
    public synchronized void processRequest() {
        while (requestQueue.isEmpty()) {
            try {
                wait(); // wait if no requests
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BookingRequest request = requestQueue.poll();

        String roomType = request.roomType;

        // Critical Section (ONLY ONE THREAD AT A TIME)
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            int remaining = inventory.get(roomType) - 1;
            inventory.put(roomType, remaining);

            System.out.println(Thread.currentThread().getName() +
                    " SUCCESS: " + request.customerName +
                    " booked " + roomType +
                    " | Remaining: " + remaining);

        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED: " + request.customerName +
                    " - No " + roomType + " rooms available");
        }
    }

    public void showInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingSystem system;

    public BookingProcessor(BookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    public void run() {
        for (int i = 0; i < 2; i++) { // each thread processes 2 requests
            system.processRequest();
        }
    }
}

// Main class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Simulating multiple booking requests
        system.addRequest(new BookingRequest("Alice", "Deluxe"));
        system.addRequest(new BookingRequest("Bob", "Deluxe"));
        system.addRequest(new BookingRequest("Charlie", "Standard"));
        system.addRequest(new BookingRequest("David", "Standard"));
        system.addRequest(new BookingRequest("Eve", "Standard"));

        // Multiple threads (guests)
        BookingProcessor t1 = new BookingProcessor(system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(system, "Thread-2");
        BookingProcessor t3 = new BookingProcessor(system, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final system state
        system.showInventory();
    }
}