import java.util.List;
public interface FlightRepository {
    List<Flight> getAllFlights();
    Flight findByNumber(String flightNumber);
    void addFlight(Flight flight);
    boolean removeFlight(String flightNumber);
}
