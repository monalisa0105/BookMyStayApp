import java.util.*;

// Reservation class
class Reservation {
private int bookingId;
private String customerName;
private String roomType;
private int nights;
private double pricePerNight;

    public Reservation(int bookingId, String customerName, String roomType, int nights, double pricePerNight) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public double getTotalAmount() {
        return nights * pricePerNight;
    }

    public String toString() {
        return "BookingID: " + bookingId +
               ", Name: " + customerName +
               ", Room: " + roomType +
               ", Nights: " + nights +
               ", Total: " + getTotalAmount();
    }
}

// Booking History class (stores data)
class BookingHistory {
private List<Reservation> reservations = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

// Report Service (separate logic)
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> list) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : list) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> list) {
        int totalBookings = list.size();
        double totalRevenue = 0;

        for (Reservation r : list) {
            totalRevenue += r.getTotalAmount();
        }

        System.out.println("\n--- Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: " + totalRevenue);
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        history.addReservation(new Reservation(1, "Alice", "Deluxe", 2, 2000));
        history.addReservation(new Reservation(2, "Bob", "Standard", 3, 1500));
        history.addReservation(new Reservation(3, "Charlie", "Suite", 1, 5000));

        // Admin views booking history
        reportService.displayAllBookings(history.getAllReservations());

        // Admin generates report
        reportService.generateSummary(history.getAllReservations());
    }
}