import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class FlightService {
    private final FlightRepository flightRepo;
    private final RandomGenerator randomGenerator;
    private int nextFlightDay = 0;

    public FlightService(FlightRepository repo) {
        this.flightRepo = repo;
        this.randomGenerator = new RandomGenerator();
    }

    /** Generates a schedule of flights and populates the flight repository. */
    public void generateFlightSchedule(int numberOfFlights) {
        for (int i = 0; i < numberOfFlights; i++) {
            String[][] cities = randomGenerator.randomDestinations();
            String fromCity = cities[0][0];
            String toCity = cities[1][0];
            String[] distance = new Flight().calculateDistance(
                    Double.parseDouble(cities[0][1]), Double.parseDouble(cities[0][2]),
                    Double.parseDouble(cities[1][1]), Double.parseDouble(cities[1][2])
            );
            double miles = Double.parseDouble(distance[0]);
            double km = Double.parseDouble(distance[1]);
            String schedule = createRandomFlightDateTime();
            String flightNum = randomGenerator.randomFlightNumbGen(2, 1).toUpperCase();
            int seats = randomGenerator.randomNumOfSeats();
            String gate = randomGenerator.randomFlightNumbGen(1, 30).toUpperCase();
            Flight flight = new Flight(schedule, flightNum, seats, fromCity, toCity, miles, km, gate);
            flightRepo.addFlight(flight);
        }
    }

    /** Removes a flight by flight number and displays the updated schedule. */
    public void deleteFlight(String flightNumber) {
        if (!flightRepo.removeFlight(flightNumber)) {
            System.out.println("Flight with given Number not found...");
        }
        displayFlightSchedule();
    }

    /** Displays the entire flight schedule in a formatted table. */
    public void displayFlightSchedule() {
        List<Flight> flights = flightRepo.getAllFlights();
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+-------------------------+-------------+--------+------------------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t   | FLIGHT NO | Available Seats  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME        | FLIGHT TIME |  GATE  |   DISTANCE(MILES/KMS)  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+-------------------------+-------------+--------+------------------------+\n");
        int i = 0;
        for (Flight f : flights) {
            i++;
            System.out.printf("| %-5d| %-41s | %-9s | \t%-14d | %-21s | %-22s | %-22s |   %-6s   |  %-4s  |  %-8.2f / %-11.2f|%n",
                    i, f.getFlightSchedule(), f.getFlightNumber(), f.getAvailableSeats(),
                    f.getFromCity(), f.getToCity(), f.fetchArrivalTime(), f.getFlightTime(), f.getGate(),
                    f.getDistanceInMiles(), f.getDistanceInKm());
            System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+-------------------------+-------------+--------+------------------------+\n");
        }
    }

    // Helper to create a random flight date/time for scheduling
    private String createRandomFlightDateTime() {
        Calendar c = Calendar.getInstance();
        nextFlightDay += (int)(Math.random() * 7);
        c.add(Calendar.DATE, nextFlightDay);
        c.add(Calendar.HOUR_OF_DAY, nextFlightDay);
        c.add(Calendar.MINUTE, ((c.get(Calendar.MINUTE) * 3) - (int)(Math.random() * 45)));
        java.time.LocalDateTime dateTime = Instant.ofEpochMilli(c.getTimeInMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        dateTime = getNearestQuarterHour(dateTime);
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a "));
    }

    // Round a LocalDateTime to the nearest quarter hour
    private java.time.LocalDateTime getNearestQuarterHour(java.time.LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        int mod = minutes % 15;
        if (mod < 8) {
            dateTime = dateTime.minusMinutes(mod);
        } else {
            dateTime = dateTime.plusMinutes(15 - mod);
        }
        return dateTime.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
    }
}
