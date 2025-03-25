import java.util.Scanner;

public class AirlineReservationSystem {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        CustomerRepository customerRepo = new InMemoryCustomerRepository();
        FlightRepository flightRepo = new InMemoryFlightRepository();
        CustomerService customerService = new CustomerService(customerRepo);
        FlightService flightService = new FlightService(flightRepo);
        ReservationService reservationService = new ReservationService(flightRepo, customerRepo);

        // Generate initial flight schedule
        flightService.generateFlightSchedule(15);

        Scanner inputScanner = new Scanner(System.in);
        System.out.println("\n\t\t\t\t\t+++++++++++++ Welcome to BAV AirLines +++++++++++++\n\nTo Further Proceed, Please enter a value.");
        System.out.println("\n***** Default Username && Password is root-root ***** Using Default Credentials will restrict you to just view the list of Passengers....\n");
        displayMainMenu();
        int desiredOption = inputScanner.nextInt();
        while (desiredOption < 0 || desiredOption > 8) {
            System.out.print("ERROR!! Please enter value between 0 - 4. Enter the value again :\t");
            desiredOption = inputScanner.nextInt();
        }

        do {
            Scanner lineScanner = new Scanner(System.in);
            if (desiredOption == 1) {
                // Admin login
                System.out.print("\nEnter the UserName to login to the Management System :     ");
                String username = lineScanner.nextLine();
                System.out.print("Enter the Password to login to the Management System :    ");
                String password = lineScanner.nextLine();
                System.out.println();
                int authResult = authService.authenticateAdmin(username, password);
                if (authResult == -1) {
                    System.out.printf("\n%20sERROR!!! Unable to login. Cannot find user with the entered credentials.... Try creating new credentials or register by pressing 4....%n", "");
                } else if (authResult == 0) {
                    System.out.println("You've standard/default privileges to access the data... You can just view customers data... Can't perform any actions on them....");
                    customerService.displayAllCustomers();
                } else {
                    System.out.printf("%-20sLogged in Successfully as \"%s\"..... For further proceedings, enter a value from below....%n", "", username);
                    int adminChoice;
                    do {
                        System.out.printf("%n%n%-60s+++++++++ 2nd Layer Menu +++++++++%50sLogged in as \"%s\"%n", "", "", username);
                        System.out.printf("%-30s (a) Enter 1 to add new Passenger....%n", "");
                        System.out.printf("%-30s (b) Enter 2 to search a Passenger....%n", "");
                        System.out.printf("%-30s (c) Enter 3 to update the Data of the Passenger....%n", "");
                        System.out.printf("%-30s (d) Enter 4 to delete a Passenger....%n", "");
                        System.out.printf("%-30s (e) Enter 5 to Display all Passengers....%n", "");
                        System.out.printf("%-30s (f) Enter 6 to Display all flights registered by a Passenger...%n", "");
                        System.out.printf("%-30s (g) Enter 7 to Display all registered Passengers in a Flight...%n", "");
                        System.out.printf("%-30s (h) Enter 8 to Delete a Flight....%n", "");
                        System.out.printf("%-30s (i) Enter 0 to Go back to the Main Menu/Logout....%n", "");
                        System.out.print("Enter the desired Choice :   ");
                        adminChoice = inputScanner.nextInt();
                        if (adminChoice == 1) {
                            customerService.registerNewCustomer();
                        } else if (adminChoice == 2) {
                            customerService.displayAllCustomers();
                            System.out.print("Enter the CustomerID to Search :\t");
                            String customerID = lineScanner.nextLine();
                            System.out.println();
                            customerService.searchCustomer(customerID);
                        } else if (adminChoice == 3) {
                            customerService.displayAllCustomers();
                            System.out.print("Enter the CustomerID to Update its Data :\t");
                            String customerID = lineScanner.nextLine();
                            customerService.editCustomerInfo(customerID);
                        } else if (adminChoice == 4) {
                            customerService.displayAllCustomers();
                            System.out.print("Enter the CustomerID to Delete its Data :\t");
                            String customerID = lineScanner.nextLine();
                            customerService.deleteCustomer(customerID);
                        } else if (adminChoice == 5) {
                            customerService.displayAllCustomers();
                        } else if (adminChoice == 6) {
                            customerService.displayAllCustomers();
                            System.out.print("\n\nEnter the ID of the user to display all flights registered by that user... ");
                            String id = lineScanner.nextLine();
                            reservationService.displayFlightsByUser(id);
                        } else if (adminChoice == 7) {
                            System.out.print("Do you want to display Passengers of all flights or a specific flight? 'Y/y' for all flights, 'N/n' for a specific flight: ");
                            char choice = lineScanner.nextLine().charAt(0);
                            if (choice == 'y' || choice == 'Y') {
                                reservationService.displayAllPassengersInAllFlights();
                            } else if (choice == 'n' || choice == 'N') {
                                flightService.displayFlightSchedule();
                                System.out.print("Enter the Flight Number to display the list of passengers registered in that flight... ");
                                String flightNum = lineScanner.nextLine();
                                reservationService.displayPassengersForFlight(flightNum);
                            } else {
                                System.out.println("Invalid Choice...No Response...!");
                            }
                        } else if (adminChoice == 8) {
                            flightService.displayFlightSchedule();
                            System.out.print("Enter the Flight Number to delete the flight : ");
                            String flightNum = lineScanner.nextLine();
                            flightService.deleteFlight(flightNum);
                        } else if (adminChoice == 0) {
                            System.out.println("Logging out of admin account...");
                        } else {
                            System.out.println("Invalid Choice...Looks like you're a robot...Entering values randomly... You have to login again...");
                            adminChoice = 0;
                        }
                    } while (adminChoice != 0);
                }
            } else if (desiredOption == 2) {
                // Register new admin
                System.out.print("\nEnter the UserName to Register :    ");
                String newAdminUser = lineScanner.nextLine();
                System.out.print("Enter the Password to Register :     ");
                String newAdminPass = lineScanner.nextLine();
                while (authService.isAdminUsernameTaken(newAdminUser)) {
                    System.out.print("ERROR!!! Admin with same UserName already exists. Enter new UserName:   ");
                    newAdminUser = lineScanner.nextLine();
                    System.out.print("Enter the Password Again:   ");
                    newAdminPass = lineScanner.nextLine();
                }
                authService.registerAdmin(newAdminUser, newAdminPass);
            } else if (desiredOption == 3) {
                // Passenger login
                System.out.print("\n\nEnter the Email to Login : \t");
                String loginEmail = lineScanner.nextLine();
                System.out.print("Enter the Password : \t");
                String loginPass = lineScanner.nextLine();
                Customer loggedInCustomer = authService.authenticatePassenger(loginEmail, loginPass, customerRepo);
                if (loggedInCustomer != null) {
                    String userName = loggedInCustomer.getEmail();
                    String userId = loggedInCustomer.getUserID();
                    int userChoice;
                    System.out.printf("%n%n%-20sLogged in Successfully as \"%s\"..... For further proceedings, enter a value from below....%n", "", userName);
                    do {
                        System.out.printf("%n%n%-60s+++++++++ 3rd Layer Menu +++++++++%50sLogged in as \"%s\"%n", "", "", userName);
                        System.out.printf("%-40s (a) Enter 1 to Book a flight....%n", "");
                        System.out.printf("%-40s (b) Enter 2 to update your Data....%n", "");
                        System.out.printf("%-40s (c) Enter 3 to delete your account....%n", "");
                        System.out.printf("%-40s (d) Enter 4 to Display Flight Schedule....%n", "");
                        System.out.printf("%-40s (e) Enter 5 to Cancel a Flight....%n", "");
                        System.out.printf("%-40s (f) Enter 6 to Display all flights registered by \"%s\"....%n", "", userName);
                        System.out.printf("%-40s (g) Enter 0 to Go back to the Main Menu/Logout....%n", "");
                        System.out.print("Enter the desired Choice :   ");
                        userChoice = inputScanner.nextInt();
                        if (userChoice == 1) {
                            flightService.displayFlightSchedule();
                            System.out.print("\nEnter the desired flight number to book :\t ");
                            String flightToBook = lineScanner.nextLine();
                            System.out.print("Enter the Number of tickets for " + flightToBook + " flight :   ");
                            int numTickets = inputScanner.nextInt();
                            while (numTickets > 10) {
                                System.out.print("ERROR!! You can't book more than 10 tickets at a time for a single flight.... Enter number of tickets again : ");
                                numTickets = inputScanner.nextInt();
                            }
                            reservationService.bookFlight(flightToBook, numTickets, userId);
                        } else if (userChoice == 2) {
                            customerService.editCustomerInfo(userId);
                        } else if (userChoice == 3) {
                            System.out.print("Are you sure to delete your account? It's an irreversible action... Enter Y/y to confirm... ");
                            char confirm = lineScanner.nextLine().charAt(0);
                            if (confirm == 'Y' || confirm == 'y') {
                                customerService.deleteCustomer(userId);
                                System.out.printf("User %s's account deleted Successfully...!!!%n", userName);
                                userChoice = 0;
                            } else {
                                System.out.println("Action has been cancelled...");
                            }
                        } else if (userChoice == 4) {
                            flightService.displayFlightSchedule();
                            // Display measurement instructions after schedule
                            new Flight(){}.displayMeasurementInstructions();
                        } else if (userChoice == 5) {
                            reservationService.cancelFlight(userId);
                        } else if (userChoice == 6) {
                            reservationService.displayFlightsByUser(userId);
                        } else {
                            if (userChoice != 0) {
                                System.out.println("Invalid Choice...Looks like you're a robot...Entering values randomly... You have to login again...");
                            }
                            userChoice = 0;
                        }
                    } while (userChoice != 0);
                } else {
                    System.out.printf("%n%20sERROR!!! Unable to login. Cannot find user with the entered credentials.... Try creating a new account by pressing 4....%n", "");
                }
            } else if (desiredOption == 4) {
                // Register new passenger
                customerService.registerNewCustomer();
            } else if (desiredOption == 5) {
                // Display user manual
                manualInstructions();
            }
            displayMainMenu();
            desiredOption = inputScanner.nextInt();
            while (desiredOption < 0 || desiredOption > 8) {
                System.out.print("ERROR!! Please enter value between 0 - 4. Enter the value again :\t");
                desiredOption = inputScanner.nextInt();
            }
        } while (desiredOption != 0);
        System.out.println("Thanks for Using BAV Airlines Ticketing System...!!!");
    }

    static void displayMainMenu() {
        System.out.println("\n\n\t\t(a) Press 0 to Exit.");
        System.out.println("\t\t(b) Press 1 to Login as admin.");
        System.out.println("\t\t(c) Press 2 to Register as admin.");
        System.out.println("\t\t(d) Press 3 to Login as Passenger.");
        System.out.println("\t\t(e) Press 4 to Register as Passenger.");
        System.out.println("\t\t(f) Press 5 to Display the User Manual.");
        System.out.print("\t\tEnter the desired option:    ");
    }

    static void manualInstructions() {
        Scanner read = new Scanner(System.in);
        System.out.printf("%n%n%50s %s Welcome to BAV Airlines User Manual %s", "", "+++++++++++++++++", "+++++++++++++++++");
        System.out.println("\n\n\t\t(a) Press 1 to display Admin Manual.");
        System.out.println("\t\t(b) Press 2 to display User Manual.");
        System.out.print("\nEnter the desired option :    ");
        int choice = read.nextInt();
        while (choice < 1 || choice > 2) {
            System.out.print("ERROR!!! Invalid entry...Please enter a value either 1 or 2....Enter again....");
            choice = read.nextInt();
        }
        if (choice == 1) {
            System.out.println("\n\n(1) Admin has access to all users data. Admin can add, update, delete, and search for any customer.\n");
            System.out.println("(2) To access the admin module, register by pressing 2 at the main menu.\n");
            System.out.println("(3) Provide the required details (username, password). Once registered, press 1 to login as an admin.\n");
            System.out.println("(4) Once logged in, the 2nd layer menu is displayed. From here, select from various options.\n");
            System.out.println("(5) Option \"1\" adds a new Passenger. Provide required details to add the passenger.\n");
            System.out.println("(6) Option \"2\" searches for any passenger by ID (from the displayed list of passengers).\n");
            System.out.println("(7) Option \"3\" updates any passenger's data given their user ID.\n");
            System.out.println("(8) Option \"4\" deletes any passenger given their ID.\n");
            System.out.println("(9) Option \"5\" displays all registered passengers.\n");
            System.out.println("(10) Option \"6\" displays flights registered by a specific passenger (enter their ID when prompted).\n");
            System.out.println("(11) Option \"7\" displays passengers of all flights (or a specific flight if chosen).\n");
            System.out.println("(12) Option \"8\" deletes a flight given its flight number.\n");
            System.out.println("(13) Option \"0\" logs out of the admin menu to the main menu.\n");
        } else {
            System.out.println("\n\n(1) A passenger can only access their own data and cannot modify other users' data.\n");
            System.out.println("(2) To use passenger features, register by pressing 4 at the main menu.\n");
            System.out.println("(3) Provide the details asked to be added to the users list. Once registered, press 3 to login as a passenger.\n");
            System.out.println("(4) Once logged in, the 3rd layer menu is displayed. From here, proceed to manage your bookings and information.\n");
            System.out.println("(5) Option \"1\" displays the available flight schedule. To book a flight, enter the flight number and number of tickets (max 10 at a time).\n");
            System.out.println("(6) Option \"2\" allows you to update your own data.\n");
            System.out.println("(7) Option \"3\" deletes your account.\n");
            System.out.println("(8) Option \"4\" displays the flight schedule for the current session.\n");
            System.out.println("(9) Option \"5\" allows you to cancel a flight you have booked (enter flight number and tickets to cancel).\n");
            System.out.println("(10) Option \"6\" displays all flights you have booked.\n");
            System.out.println("(11) Option \"0\" logs you out to the main menu. You can log in again anytime during this session.\n");
        }
    }
}
