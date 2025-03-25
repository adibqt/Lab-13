import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryFlightRepository implements FlightRepository {
    private final List<Flight> flights = new ArrayList<>();

    @Override
    public List<Flight> getAllFlights() {
        return flights;
    }

    @Override
    public Flight findByNumber(String flightNumber) {
        for (Flight f : flights) {
            if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    @Override
    public boolean removeFlight(String flightNumber) {
        Iterator<Flight> it = flights.iterator();
        while (it.hasNext()) {
            Flight f = it.next();
            if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
