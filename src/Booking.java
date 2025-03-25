public class Booking {
    private final Customer customer;
    private final Flight flight;
    private int tickets;

    public Booking(Customer customer, Flight flight, int tickets) {
        this.customer = customer;
        this.flight = flight;
        this.tickets = tickets;
    }
    public Customer getCustomer() { return customer; }
    public Flight getFlight()     { return flight; }
    public int getTickets()       { return tickets; }
    public void addTickets(int additionalTickets) {
        this.tickets += additionalTickets;
    }
    public void removeTickets(int ticketsToRemove) {
        this.tickets -= ticketsToRemove;
    }
}
