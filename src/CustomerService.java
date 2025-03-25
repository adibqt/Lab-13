import java.util.Scanner;

public class CustomerService {
    private final CustomerRepository customerRepo;
    private final RandomGenerator randomGenerator;

    public CustomerService(CustomerRepository repo) {
        this.customerRepo = repo;
        this.randomGenerator = new RandomGenerator();
    }

    /** Registers a new customer by taking input and adding to the repository. */
    public void registerNewCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%n%n%n%60s ++++++++++++++ Welcome to the Customer Registration Portal ++++++++++++++%n", "");
        System.out.print("Enter your name :\t");
        String name = scanner.nextLine();
        System.out.print("Enter your email address :\t");
        String email = scanner.nextLine();
        // Ensure email is unique
        while (customerRepo.findByEmail(email) != null) {
            System.out.println("ERROR!!! User with the same email already exists... Use a new email or login with previous credentials.");
            System.out.print("Enter your email address :\t");
            email = scanner.nextLine();
        }
        System.out.print("Enter your Password :\t");
        String password = scanner.nextLine();
        System.out.print("Enter your Phone number :\t");
        String phone = scanner.nextLine();
        System.out.print("Enter your address :\t");
        String address = scanner.nextLine();
        System.out.print("Enter your age :\t");
        int age = scanner.nextInt();
        // Generate a unique userID for the customer
        randomGenerator.randomIDGen();
        String userId = randomGenerator.getRandomNumber();
        Customer newCustomer = new Customer(name, email, password, phone, address, age, userId);
        customerRepo.addCustomer(newCustomer);
        System.out.println();
    }

    /** Updates the information of an existing customer by user ID. */
    public void editCustomerInfo(String userId) {
        Customer customer = customerRepo.findById(userId);
        if (customer == null) {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!%n", "", userId);
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the new name of the Passenger:\t");
        customer.setName(scanner.nextLine());
        System.out.print("Enter the new email address of Passenger " + customer.getName() + ":\t");
        customer.setEmail(scanner.nextLine());
        System.out.print("Enter the new Phone number of Passenger " + customer.getName() + ":\t");
        customer.setPhone(scanner.nextLine());
        System.out.print("Enter the new address of Passenger " + customer.getName() + ":\t");
        customer.setAddress(scanner.nextLine());
        System.out.print("Enter the new age of Passenger " + customer.getName() + ":\t");
        customer.setAge(scanner.nextInt());
        displayAllCustomers();
    }

    /** Deletes a customer from the system by ID. */
    public void deleteCustomer(String userId) {
        Customer customer = customerRepo.findById(userId);
        if (customer == null) {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!%n", "", userId);
            return;
        }
        customerRepo.removeCustomer(customer);
        System.out.printf("%n%-50sPrinting all Customer data after deleting Customer with the ID %s...%n", "", userId);
        displayAllCustomers();
    }

    /** Searches for a customer by ID and displays their information if found. */
    public void searchCustomer(String userId) {
        Customer customer = customerRepo.findById(userId);
        if (customer != null) {
            System.out.printf("%-50sCustomer Found...!!! Here is the Full Record...!!!%n%n%n", "");
            printCustomerTableHeader();
            printCustomerAsRow(1, customer);
            printCustomerTableFooter();
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!%n", "", userId);
        }
    }

    /** Displays all registered customers in a formatted table. */
    public void displayAllCustomers() {
        printCustomerTableHeader();
        int i = 1;
        for (Customer c : customerRepo.getAllCustomers()) {
            printCustomerAsRow(i++, c);
        }
        printCustomerTableFooter();
    }

    // Helper to print table header for customer data
    private void printCustomerTableHeader() {
        System.out.println();
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+%n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Name                  | Age     | EmailID                      | Home Address                       | Phone Number            |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+%n", "");
    }
    // Helper to print a customer's data as a formatted table row
    private void printCustomerAsRow(int index, Customer c) {
        // Insert space in userID for readability (e.g., "920191" -> "920 191")
        String formattedId = c.getUserID();
        if (formattedId.length() > 3) {
            formattedId = formattedId.substring(0,3) + " " + formattedId.substring(3);
        }
        System.out.printf("%10s| %-10d | %-10s | %-32s | %-7d | %-27s | %-35s | %-23s |%n", "",
                index, formattedId, c.getName(), c.getAge(), c.getEmail(), c.getAddress(), c.getPhone());
        System.out.printf("%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+%n", "");
    }
    // Helper to print table footer (just adds a final line break)
    private void printCustomerTableFooter() {
        System.out.println();
    }
}
