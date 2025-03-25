import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, String> adminCredentials = new HashMap<>();

    public AuthService() {
        // Default admin credentials with limited privileges
        adminCredentials.put("root", "root");
    }

    /**
     * Authenticates an admin user.
     * @return 0 if default admin (root), 1 if privileged admin, -1 if not found.
     */
    public int authenticateAdmin(String username, String password) {
        if (username.equals("root") && password.equals("root")) {
            return 0;
        }
        if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
            return 1;
        }
        return -1;
    }

    /**
     * Registers a new admin.
     * @return true if registration successful, false if username already exists.
     */
    public boolean registerAdmin(String username, String password) {
        if (adminCredentials.containsKey(username)) {
            return false;
        }
        adminCredentials.put(username, password);
        return true;
    }

    /**
     * Authenticates a passenger by email and password.
     * @return the Customer if credentials match, or null if not found.
     */
    public Customer authenticatePassenger(String email, String password, CustomerRepository customerRepo) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            return customer;
        }
        return null;
    }

    /** Checks if an admin username is already taken. */
    public boolean isAdminUsernameTaken(String username) {
        return adminCredentials.containsKey(username);
    }
}
