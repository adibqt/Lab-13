import java.util.List;
import java.util.Scanner;

public class ReservationService {
    private final FlightRepository flightRepo;
    private final CustomerRepository customerRepo;

    public ReservationService(FlightRepository flightRepo, CustomerRepository customerRepo) {
        this.flightRepo = flightRepo;
        this.customerRepo = customerRepo;
    }

    /** Books a flight for a customer, updating flight seats and customer records. */
    public void bookFlight(String flightNo, int numOfTickets, String userId) {
        Flight flight = flightRepo.findByNumber(flightNo);
        Customer customer = customerRepo.findById(userId);
        if (flight == null || customer == null) {
            System.out.println("Invalid Flight Number...! No flight with the ID \"" + flightNo + "\" was found...");
            return;
        }
        if (numOfTickets > flight.getAvailableSeats()) {
            System.out.println("Not enough seats available on flight " + flightNo);
            return;
        }
        // Check if customer already has a booking on this flight
        Booking existingBooking = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getFlightNumber().equalsIgnoreCase(flightNo)) {
                existingBooking = b;
                break;
            }
        }
        if (existingBooking != null) {
            // Add tickets to existing booking
            existingBooking.addTickets(numOfTickets);
        } else {
            // Create a new booking association
            Booking booking = new Booking(customer, flight, numOfTickets);
            customer.addBooking(booking);
            flight.addBooking(booking);
        }
        flight.reserveSeats(numOfTickets);
        System.out.printf("%n %50s You've booked %d tickets for Flight \"%5s\"...%n", "", numOfTickets, flightNo.toUpperCase());
    }

    /** Cancels a flight booking for a customer (full or partial cancellation). */
    public void cancelFlight(String userId) {
        Customer customer = customerRepo.findById(userId);
        if (customer == null || customer.getBookings().isEmpty()) {
            System.out.println("No flight has been registered by you...");
            return;
        }
        System.out.printf("%50s %s Here is the list of all the Flights registered by you %s%n", " ", "++++++++++++++", "++++++++++++++");
        displayFlightsByUser(userId);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Flight Number of the Flight you want to cancel : ");
        String flightNum = scanner.nextLine();
        System.out.print("Enter the number of tickets to cancel : ");
        int ticketsToCancel = scanner.nextInt();
        Booking bookingToCancel = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getFlightNumber().equalsIgnoreCase(flightNum)) {
                bookingToCancel = b;
                break;
            }
        }
        if (bookingToCancel == null) {
            System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\"...");
            return;
        }
        // Ensure not cancelling more tickets than booked
        while (ticketsToCancel > bookingToCancel.getTickets()) {
            System.out.print("ERROR!!! Number of tickets cannot be greater than " + bookingToCancel.getTickets() + ". Please enter the number of tickets again : ");
            ticketsToCancel = scanner.nextInt();
        }
        Flight flight = bookingToCancel.getFlight();
        if (ticketsToCancel == bookingToCancel.getTickets()) {
            // Cancel entire booking
            flight.releaseSeats(ticketsToCancel);
            customer.removeBooking(bookingToCancel);
            flight.removeBooking(bookingToCancel);
        } else {
            // Partial cancellation
            bookingToCancel.removeTickets(ticketsToCancel);
            flight.releaseSeats(ticketsToCancel);
        }
    }

    /** Displays all flights booked by a specific user in a formatted table. */
    public void displayFlightsByUser(String userId) {
        Customer customer = customerRepo.findById(userId);
        if (customer == null) return;
        List<Booking> bookings = customer.getBookings();
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+-------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO |  Booked Tickets  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME        | FLIGHT TIME |  GATE  |  FLIGHT STATUS   |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+-------------------------+-------------+--------+-----------------+\n");
        int index = 0;
        for (Booking b : bookings) {
            index++;
            Flight flight = b.getFlight();
            String status = (flightRepo.findByNumber(flight.getFlightNumber()) != null) ? "As Per Schedule" : "Cancelled";
            System.out.printf("| %-5d| %-41s | %-9s | \t%-14d | %-21s | %-22s | %-22s |   %-6s   |  %-4s  | %-15s |%n",
                    index, flight.getFlightSchedule(), flight.getFlightNumber(), b.getTickets(),
                    flight.getFromCity(), flight.getToCity(), flight.fetchArrivalTime(), flight.getFlightTime(), flight.getGate(), status);
            System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+-------------------------+-------------+--------+-----------------+\n");
        }
    }

    /** Displays all registered passengers for all flights (only flights that have bookings). */
    public void displayAllPassengersInAllFlights() {
        System.out.println();
        for (Flight flight : flightRepo.getAllFlights()) {
            if (!flight.getBookings().isEmpty()) {
                displayPassengersForFlight(flight.getFlightNumber());
            }
        }
    }

    /** Displays all registered passengers for a specific flight in a formatted table. */
    public void displayPassengersForFlight(String flightNum) {
        Flight flight = flightRepo.findByNumber(flightNum);
        if (flight == null) return;
        List<Booking> bookings = flight.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No passengers registered for flight " + flightNum);
            return;
        }
        System.out.printf("%n%65s Displaying Registered Customers for Flight No. \"%-6s\" %s %n%n", "+++++++++++++", flight.getFlightNumber(), "+++++++++++++");
        System.out.printf("%10s+------------+------------+------------------------------+---------+-----------------------------+-----------------------------+-------------------------+----------------+%n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Name                | Age     | EmailID                     | Home Address                | Phone Number            | Booked Tickets |%n", "");
        System.out.printf("%10s+------------+------------+------------------------------+---------+-----------------------------+-----------------------------+-------------------------+----------------+%n", "");
        int i = 0;
        for (Booking b : bookings) {
            Customer cust = b.getCustomer();
            i++;
            // Format userID with space for readability
            String userIdFormatted = cust.getUserID();
            if (userIdFormatted.length() > 3) {
                userIdFormatted = userIdFormatted.substring(0,3) + " " + userIdFormatted.substring(3);
            }
            System.out.printf("%10s| %-10d | %-10s | %-28s | %-7d | %-27s | %-27s | %-23s | %-14d |%n", "",
                    i, userIdFormatted, cust.getName(), cust.getAge(), cust.getEmail(), cust.getAddress(), cust.getPhone(), b.getTickets());
            System.out.printf("%10s+------------+------------+------------------------------+---------+-----------------------------+-----------------------------+-------------------------+----------------+%n", "");
        }
    }
}
