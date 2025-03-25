import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight extends FlightDistance {
    private final String flightSchedule;
    private final String flightNumber;
    private final String fromCity;
    private final String toCity;
    private final String gate;
    private final double distanceInMiles;
    private final double distanceInKm;
    private final String flightTime;
    private int availableSeats;
    private List<Booking> bookings;

    // Default constructor (for utility usage only)
    public Flight() {
        this.flightSchedule = null;
        this.flightNumber = null;
        this.fromCity = null;
        this.toCity = null;
        this.gate = null;
        this.distanceInMiles = 0;
        this.distanceInKm = 0;
        this.flightTime = null;
        this.availableSeats = 0;
        this.bookings = new ArrayList<>();
    }

    public Flight(String flightSchedule, String flightNumber, int seats,
                  String fromCity, String toCity,
                  double distanceInMiles, double distanceInKm,
                  String gate) {
        this.flightSchedule = flightSchedule;
        this.flightNumber = flightNumber;
        this.availableSeats = seats;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distanceInMiles = distanceInMiles;
        this.distanceInKm = distanceInKm;
        this.flightTime = calculateFlightTime(distanceInMiles);
        this.gate = gate;
        this.bookings = new ArrayList<>();
    }

    public String getFlightSchedule() { return flightSchedule; }
    public String getFlightNumber()   { return flightNumber; }
    public String getFromCity()       { return fromCity; }
    public String getToCity()         { return toCity; }
    public String getGate()           { return gate; }
    public double getDistanceInMiles() { return distanceInMiles; }
    public double getDistanceInKm()    { return distanceInKm; }
    public String getFlightTime()      { return flightTime; }
    public int getAvailableSeats()     { return availableSeats; }
    public List<Booking> getBookings() { return bookings; }

    void reserveSeats(int numSeats) {
        if (numSeats <= availableSeats) {
            availableSeats -= numSeats;
        }
    }
    void releaseSeats(int numSeats) {
        availableSeats += numSeats;
    }
    void addBooking(Booking booking) {
        bookings.add(booking);
    }
    void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public String calculateFlightTime(double distanceMiles) {
        double groundSpeed = 450.0;
        double hours = distanceMiles / groundSpeed;
        int totalMinutes = (int)Math.round(hours * 60 / 5) * 5;
        int hh = totalMinutes / 60;
        int mm = totalMinutes % 60;
        return String.format("%02d:%02d", hh, mm);
    }

    public String fetchArrivalTime() {
        if (flightSchedule == null || flightTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a ");
        LocalDateTime depTime = LocalDateTime.parse(flightSchedule, formatter);
        String[] timeParts = flightTime.split(":");
        int hh = Integer.parseInt(timeParts[0]);
        int mm = Integer.parseInt(timeParts[1]);
        LocalDateTime arrival = depTime.plusHours(hh).plusMinutes(mm);
        DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("EE, dd-MM-yyyy HH:mm a");
        return arrival.format(outputFmt);
    }

    @Override
    public String[] calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(degreeToRadian(lat1)) * Math.sin(degreeToRadian(lat2))
                + Math.cos(degreeToRadian(lat1)) * Math.cos(degreeToRadian(lat2)) * Math.cos(degreeToRadian(theta));
        dist = Math.acos(dist);
        dist = radianToDegree(dist);
        dist = dist * 60 * 1.1515;
        String[] result = new String[2];
        result[0] = String.format("%.2f", dist * 0.8684);
        result[1] = String.format("%.2f", dist * 1.609344);
        return result;
    }
    private double degreeToRadian(double deg) { return deg * Math.PI / 180.0; }
    private double radianToDegree(double rad) { return rad * 180.0 / Math.PI; }
}
