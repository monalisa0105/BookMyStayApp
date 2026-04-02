import java.util.*;

// Represents an Add-On Service
class AddOnService {
private String serviceName;
private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages Add-On Services for Reservations
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServicesMap = new HashMap<>();

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).addAll(services);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = getServices(reservationId);

        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }
}

// Main class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample reservation ID (already created in previous use cases)
        String reservationId = "RES123";

        System.out.println("Enter number of add-on services:");
        int n = scanner.nextInt();
        scanner.nextLine();

        List<AddOnService> selectedServices = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter service name:");
            String name = scanner.nextLine();

            System.out.println("Enter service cost:");
            double cost = scanner.nextDouble();
            scanner.nextLine();

            selectedServices.add(new AddOnService(name, cost));
        }

        // Add services to reservation
        manager.addServices(reservationId, selectedServices);

        // Display services
        System.out.println("\nServices added to Reservation ID: " + reservationId);
        for (AddOnService service : manager.getServices(reservationId)) {
            System.out.println(service);
        }

        // Display total cost
        double totalCost = manager.calculateTotalCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        scanner.close();
    }
}