abstract class Room {
private int beds;
private int size;
private double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + beds);
        System.out.println("Room Size: " + size + " sq ft");
        System.out.println("Price per Night: ₹" + price);
    }
}

class SingleRoom extends Room {
public SingleRoom() {
super(1, 200, 3000);
}
}

class DoubleRoom extends Room {
public DoubleRoom() {
super(2, 350, 5000);
}
}

class SuiteRoom extends Room {
public SuiteRoom() {
super(3, 500, 8000);
}
}

public class BookMyStayApp {
public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v2.0");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("\nSingle Room");
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailability);

        System.out.println("\nDouble Room");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailability);

        System.out.println("\nSuite Room");
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailability);
    }
}