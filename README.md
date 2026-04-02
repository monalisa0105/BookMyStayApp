import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
private static final long serialVersionUID = 1L;

    private int bookingId;
    private String customerName;
    private String roomType;

    public Reservation(int bookingId, String customerName, String roomType) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String toString() {
        return "BookingID: " + bookingId +
               ", Name: " + customerName +
               ", Room: " + roomType;
    }
}

// Wrapper class to store system state
class SystemState implements Serializable {
private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> reservations, Map<String, Integer> inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "booking_data.ser";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("Data loaded successfully.");
            return state;
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data. Starting with safe defaults.");
        }
        return null;
    }
}

// Booking System
class BookingSystem {

    private List<Reservation> history;
    private Map<String, Integer> inventory;
    private int bookingCounter = 1;

    public BookingSystem() {
        history = new ArrayList<>();
        inventory = new HashMap<>();

        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void restore(SystemState state) {
        if (state != null) {
            this.history = state.reservations;
            this.inventory = state.inventory;
            this.bookingCounter = history.size() + 1;
        }
    }

    public SystemState getState() {
        return new SystemState(history, inventory);
    }

    public void bookRoom(String name, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking Failed: No rooms available.");
            return;
        }

        inventory.put(roomType, inventory.get(roomType) - 1);
        Reservation r = new Reservation(bookingCounter++, name, roomType);
        history.add(r);

        System.out.println("Booking Successful: " + r);
    }

    public void showState() {
        System.out.println("\nInventory: " + inventory);
        System.out.println("Booking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();
        BookingSystem system = new BookingSystem();

        // Step 1: Load previous state
        SystemState loadedState = persistence.load();
        system.restore(loadedState);

        // Step 2: Perform operations
        system.bookRoom("Alice", "Deluxe");
        system.bookRoom("Bob", "Standard");

        // Step 3: Display current state
        system.showState();

        // Step 4: Save state before exit
        persistence.save(system.getState());
    }
}