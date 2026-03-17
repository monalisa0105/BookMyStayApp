import java.util.*;

// Reservation: Represents a booking request
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all queued requests (read-only)
    public void viewRequests() {
        System.out.println("\nBooking Requests in Queue (FIFO Order):");

        if (queue.isEmpty()) {
            System.out.println("No booking requests available.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (without removing)
    public Reservation peekNext() {
        return queue.peek();
// Domain मॉडल: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("-----------------------------------");
    }
}

// Inventory: State Holder (read-only in this use case)
class Inventory {
    private Map<String, Integer> availabilityMap;

    public Inventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        availabilityMap.put(roomType, count);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availabilityMap.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        boolean found = false;

        for (String roomType : inventory.getAllRoomTypes()) {

            int availableCount = inventory.getAvailability(roomType);

            // Validation: show only available rooms
            if (availableCount > 0) {
                Room room = roomCatalog.get(roomType);

                if (room != null) { // Defensive check
                    room.displayDetails();
                    System.out.println("Available Units: " + availableCount);
                    System.out.println("===================================");
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Create Booking Queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 2: Guests submit booking requests
        System.out.println("Guests are submitting booking requests...\n");

        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));
        bookingQueue.addRequest(new Reservation("Diana", "Single"));

        // Step 3: View all requests (FIFO order preserved)
        bookingQueue.viewRequests();

        // Step 4: Peek next request (no removal)
        System.out.println("\nNext request to be processed:");
        Reservation next = bookingQueue.peekNext();

        if (next != null) {
            next.display();
        }

        // IMPORTANT:
        // - No inventory updates
        // - No room allocation
        // - Only request intake and ordering
        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Step 2: Create Room Catalog (Domain Model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 2000,
                        Arrays.asList("WiFi", "AC")));

        roomCatalog.put("Double",
                new Room("Double", 3500,
                        Arrays.asList("WiFi", "AC", "TV")));

        roomCatalog.put("Suite",
                new Room("Suite", 6000,
                        Arrays.asList("WiFi", "AC", "TV", "Mini Bar")));

        // Step 3: Create Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Guest searches rooms
        System.out.println("Guest is searching for available rooms...");
        searchService.searchAvailableRooms();

        // IMPORTANT: Inventory is NOT modified anywhere
    }
}