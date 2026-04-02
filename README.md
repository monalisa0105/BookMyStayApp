import java.util.*;

// Custom Exception
class CancellationException extends Exception {
public CancellationException(String message) {
super(message);
}
}

// Reservation class
class Reservation {
private int bookingId;
private String customerName;
private String roomType;
private String roomId;
private boolean isCancelled;

    public Reservation(int bookingId, String customerName, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    public String toString() {
        return "BookingID: " + bookingId +
               ", Name: " + customerName +
               ", RoomType: " + roomType +
               ", RoomID: " + roomId +
               ", Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// Booking + Cancellation Service
class BookingService {

    private Map<String, Integer> inventory;
    private List<Reservation> history;
    private Stack<String> rollbackStack;
    private int bookingCounter = 1;

    public BookingService() {
        inventory = new HashMap<>();
        history = new ArrayList<>();
        rollbackStack = new Stack<>();

        // Initial inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Booking method
    public void bookRoom(String name, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking Failed: Room not available.");
            return;
        }

        // Allocate room ID
        String roomId = roomType.substring(0, 1) + (inventory.get(roomType));

        // Reduce inventory
        inventory.put(roomType, inventory.get(roomType) - 1);

        Reservation r = new Reservation(bookingCounter++, name, roomType, roomId);
        history.add(r);

        System.out.println("Booking Successful: " + r);
    }

    // Cancellation method
    public void cancelBooking(int bookingId) {
        try {
            Reservation target = null;

            // Find reservation
            for (Reservation r : history) {
                if (r.getBookingId() == bookingId) {
                    target = r;
                    break;
                }
            }

            // Validation
            if (target == null) {
                throw new CancellationException("Booking does not exist.");
            }

            if (target.isCancelled()) {
                throw new CancellationException("Booking already cancelled.");
            }

            // LIFO rollback tracking
            rollbackStack.push(target.getRoomId());

            // Restore inventory
            String roomType = target.getRoomType();
            inventory.put(roomType, inventory.get(roomType) + 1);

            // Mark cancelled
            target.cancel();

            System.out.println("Cancellation Successful for BookingID: " + bookingId);

        } catch (CancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory: " + inventory);
    }

    public void showHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        BookingService service = new BookingService();

        // Book rooms
        service.bookRoom("Alice", "Deluxe");
        service.bookRoom("Bob", "Standard");

        // Cancel booking
        service.cancelBooking(1);

        // Invalid cancellation
        service.cancelBooking(1); // already cancelled
        service.cancelBooking(5); // does not exist

        // Display system state
        service.showInventory();
        service.showHistory();
        service.showRollbackStack();
    }
}