import java.util.*;

public class Customer {

    // ************************************************************ Fields
    // ************************************************************
    private final String userID;
    private String email;
    private String name;
    private String phone;
    private final String password;
    private String address;
    private int age;
    public List<Flight> flightsRegisteredByUser;
    public List<Integer> numOfTicketsBookedByUser;
    public static final List<Customer> customerCollection = User.getCustomersCollection();

    // ************************************************************
    // Behaviours/Methods
    // ************************************************************

    Customer() {
        this.userID = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.phone = null;
        this.address = null;
        this.age = 0;
    }

    /**
     * Registers new customer to the program. Obj of RandomGenerator(Composition) is
     * being used to assign unique userID to the newly created customer.
     *
     * @param name     name of the customer
     * @param email    customer's email
     * @param password customer's account password
     * @param phone    customer's phone-number
     * @param address  customer's address
     * @param age      customer's age
     */
    Customer(String name, String email, String password, String phone, String address, int age) {
        RandomGenerator random = new RandomGenerator();
        random.randomIDGen();
        this.name = name;
        this.userID = random.getRandomNumber();
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.flightsRegisteredByUser = new ArrayList<>();
        this.numOfTicketsBookedByUser = new ArrayList<>();
    }

    /**
     * Takes input for the new customer and adds them to programs memory.
     * isUniqueData() validates the entered email
     * and returns true if the entered email is already registered. If email is
     * already registered, program will ask the user
     * to enter new email address to get himself register.
     */
    public void addNewCustomer() {
        System.out.printf("\n\n\n%60s ++++++++++++++ Welcome to the Customer Registration Portal ++++++++++++++", "");
        Scanner read = new Scanner(System.in);
        System.out.print("\nEnter your name :\t");
        String name = read.nextLine();
        System.out.print("Enter your email address :\t");
        String email = read.nextLine();
        while (isUniqueData(email)) {
            System.out.println(
                    "ERROR!!! User with the same email already exists... Use new email or login using the previous credentials....");
            System.out.print("Enter your email address :\t");
            email = read.nextLine();
        }
        System.out.print("Enter your Password :\t");
        String password = read.nextLine();
        System.out.print("Enter your Phone number :\t");
        String phone = read.nextLine();
        System.out.print("Enter your address :\t");
        String address = read.nextLine();
        System.out.print("Enter your age :\t");
        int age = read.nextInt();
        customerCollection.add(new Customer(name, email, password, phone, address, age));
    }

    /**
     * Returns String consisting of customers data(name, age, email etc...) for
     * displaying.
     * randomIDDisplay() adds space between the userID for easy readability.
     *
     * @param i for serial numbers.
     * @return customers data in String
     */
    private String toString(int i) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-7s | %-27s | %-35s | %-23s |", "", i,
                randomIDDisplay(userID), name, age, email, address, phone);
    }

    /**
     * Searches for customer with the given ID and displays the customers' data if
     * found.
     *
     * @param ID of the searching/required customer
     */
    public void searchUser(String ID) {
        boolean isFound = false;
        Customer customerWithTheID = customerCollection.get(0);
        for (Customer c : customerCollection) {
            if (ID.equals(c.getUserID())) {
                System.out.printf("%-50sCustomer Found...!!!Here is the Full Record...!!!\n\n\n", " ");
                displayHeader();
                isFound = true;
                customerWithTheID = c;
                break;
            }
        }
        if (isFound) {
            System.out.println(customerWithTheID.toString(1));
            System.out.printf(
                    "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                    "");
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    /**
     * Returns true if the given emailID is already registered, false otherwise
     *
     * @param emailID to be checked in the list
     */
    public boolean isUniqueData(String emailID) {
        boolean isUnique = false;
        for (Customer c : customerCollection) {
            if (emailID.equals(c.getEmail())) {
                isUnique = true;
                break;
            }
        }
        return isUnique;
    }

    public void editUserInfo(String ID) {
        boolean isFound = false;
        Scanner read = new Scanner(System.in);
        for (Customer c : customerCollection) {
            if (ID.equals(c.getUserID())) {
                isFound = true;
                System.out.print("\nEnter the new name of the Passenger:\t");
                String name = read.nextLine();
                c.setName(name);
                System.out.print("Enter the new email address of Passenger " + name + ":\t");
                c.setEmail(read.nextLine());
                System.out.print("Enter the new Phone number of Passenger " + name + ":\t");
                c.setPhone(read.nextLine());
                System.out.print("Enter the new address of Passenger " + name + ":\t");
                c.setAddress(read.nextLine());
                System.out.print("Enter the new age of Passenger " + name + ":\t");
                c.setAge(read.nextInt());
                displayCustomersData(false);
                break;
            }
        }
        if (!isFound) {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    public void deleteUser(String ID) {
        boolean isFound = false;
        Iterator<Customer> iterator = customerCollection.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (ID.equals(customer.getUserID())) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            iterator.remove();
            System.out.printf("\n%-50sPrinting all  Customer's Data after deleting Customer with the ID %s.....!!!!\n",
                    "", ID);
            displayCustomersData(false);
        } else {
            System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", ID);
        }
    }

    /**
     * Shows the customers' data in formatted way.
     * 
     * @param showHeader to check if we want to print ascii art for the customers'
     *                   data.
     */
    public void displayCustomersData(boolean showHeader) {
        displayHeader();
        Iterator<Customer> iterator = customerCollection.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            Customer c = iterator.next();
            System.out.println(c.toString(i));
            System.out.printf(
                    "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                    "");
        }
    }

    /**
     * Shows the header for printing customers data
     */
    void displayHeader() {
        System.out.println();
        System.out.printf(
                "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                "");
        System.out.printf(
                "%10s| SerialNum  |   UserID   | Passenger Names                  | Age     | EmailID\t\t       | Home Address\t\t\t     | Phone Number\t       |%n",
                "");
        System.out.printf(
                "%10s+------------+------------+----------------------------------+---------+-----------------------------+-------------------------------------+-------------------------+\n",
                "");
        System.out.println();

    }

    /**
     * Adds space between userID to increase its readability
     * <p>
     * Example:
     * </p>
     * <b>"920 191" is much more readable than "920191"</b>
     *
     * @param randomID id to add space
     * @return randomID with added space
     */
    String randomIDDisplay(String randomID) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i <= randomID.length(); i++) {
            if (i == 3) {
                newString.append(" ").append(randomID.charAt(i));
            } else if (i < randomID.length()) {
                newString.append(randomID.charAt(i));
            }
        }
        return newString.toString();
    }

    /**
     * Associates a new flight with the specified customer
     *
     * @param f flight to associate
     */
    void addNewFlightToCustomerList(Flight f) {
        this.flightsRegisteredByUser.add(f);
        // numOfFlights++;
    }

    /**
     * Adds numOfTickets to already booked flights
     * 
     * @param index        at which flight is registered in the arraylist
     * @param numOfTickets how many tickets to add
     */
    void addExistingFlightToCustomerList(int index, int numOfTickets) {
        int newNumOfTickets = numOfTicketsBookedByUser.get(index) + numOfTickets;
        this.numOfTicketsBookedByUser.set(index, newNumOfTickets);
    }

    // ************************************************************ Setters &
    // Getters ************************************************************

    public List<Flight> getFlightsRegisteredByUser() {
        return flightsRegisteredByUser;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getNumOfTicketsBookedByUser() {
        return numOfTicketsBookedByUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }
}