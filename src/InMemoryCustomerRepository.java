import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final List<Customer> customers = new ArrayList<>();

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }

    @Override
    public Customer findById(String userId) {
        for (Customer c : customers) {
            if (c.getUserID().equals(userId)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
}
