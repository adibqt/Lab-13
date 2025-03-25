import java.util.List;
public interface CustomerRepository {
    List<Customer> getAllCustomers();
    Customer findById(String userId);
    Customer findByEmail(String email);
    void addCustomer(Customer customer);
    void removeCustomer(Customer customer);
}
