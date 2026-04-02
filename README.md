import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
public InvalidBookingException(String message) {
super(message);
}
}

// Reservation class
class Reservation {
private int bookingId;
private String customerName;
private String roomType;
private int nights;

    public Reservation(int bookingId, String customerName, String roomType, int nights) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String toString() {
        return "BookingID: " + bookingId +
               ", Name: " + customerName +
               ", Room: " + roomType +
               ", Nights: " + nights;
    }
}

// Validator class (core logic for validation)
class InvalidBookingValidator {

    private Map<String, Integer> inventory;

    public InvalidBookingValidator(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void validate(String roomType, int nights) throws InvalidBookingException {

        // Validate room type
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate nights
        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0.");
        }

        // Check availability
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

// Booking Service
class BookingService {

    private Map<String, Integer> inventory;
    private List<Reservation> history;
    private InvalidBookingValidator validator;
    private int bookingCounter = 1;

    public BookingService() {
        inventory = new HashMap<>();
        history = new ArrayList<>();

        // Initial inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);

        validator = new InvalidBookingValidator(inventory);
    }

    public void bookRoom(String name, String roomType, int nights) {
        try {
            // Fail-fast validation
            validator.validate(roomType, nights);

            // Update inventory safely
            inventory.put(roomType, inventory.get(roomType) - 1);

            // Create reservation
            Reservation r = new Reservation(bookingCounter++, name, roomType, nights);
            history.add(r);

            System.out.println("Booking Successful: " + r);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
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
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingService service = new BookingService();

        // Valid booking
        service.bookRoom("Alice", "Deluxe", 2);

        // Invalid room type
        service.bookRoom("Bob", "Premium", 1);

        // Invalid nights
        service.bookRoom("Charlie", "Standard", 0);

        // Overbooking
        service.bookRoom("David", "Deluxe", 1);
        service.bookRoom("Eve", "Deluxe", 1); // Should fail

        // Display system state
        service.showInventory();
        service.showHistory();
    }
}