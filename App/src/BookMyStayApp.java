import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service (State Holder)
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }
}

// Booking Request Queue (FIFO)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Allocation Logic)
class BookingService {

    private InventoryService inventoryService;

    // Track allocated room IDs per room type
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Track all room IDs globally (uniqueness)
    private Set<String> allRoomIds = new HashSet<>();

    private int idCounter = 1;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void processBooking(Reservation reservation) {

        String type = reservation.getRoomType();

        System.out.println("\nProcessing request for " + reservation.getGuestName());

        // Check availability
        if (inventoryService.getAvailability(type) <= 0) {
            System.out.println("No rooms available for type: " + type);
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = type.substring(0, 1).toUpperCase() + idCounter++;
        } while (allRoomIds.contains(roomId));

        // Store globally
        allRoomIds.add(roomId);

        // Store per room type
        allocatedRooms.putIfAbsent(type, new HashSet<>());
        allocatedRooms.get(type).add(roomId);

        // Inventory update (IMPORTANT)
        inventoryService.decrement(type);

        // Confirm reservation
        System.out.println("Reservation CONFIRMED for " + reservation.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Allocated Room ID: " + roomId);
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        // Step 2: Create Booking Queue
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single")); // exceeds availability
        queue.addRequest(new Reservation("Diana", "Suite"));

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process Queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }

        // Step 5: Display Results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}