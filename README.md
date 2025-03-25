## Key Structural Changes & Design Improvements
## Single Responsibility Principle (SRP):
Each class now has a clear purpose. For example, Customer is a simple data model for passenger info, and all input/output or list management logic was moved out. We introduced a new Booking class to represent a flight reservation (linking a Customer and a Flight with a ticket count) so that managing tickets is encapsulated in one place rather than parallel arrays. Service classes (CustomerService, FlightService, ReservationService) handle application logic like registering users, scheduling flights, booking and canceling, while the AirlineReservationSystem main class handles user interaction and menu flow. This separation makes each class simpler and easier to maintain or extend.
## Open/Closed Principle (OCP): 
The design uses abstraction to allow extension without modifying core classes. We defined interfaces for persistence (CustomerRepository, FlightRepository) and provided in-memory implementations. If future requirements need a database or different storage, we can add a new implementation of those interfaces without changing the services that use them. Likewise, the abstract class FlightDistance remains as a hook for alternative distance-calculation strategies, and Flight extends it so we could override methods if needed without altering existing code.
##  Liskov Substitution Principle (LSP): 
I removed improper inheritance and ensured subclasses can substitute for their base classes without issue. The original design had RolesAndPermissions extending User (the main class), which was inappropriate. We replaced this with an AuthService class that contains admin authentication logic, avoiding misuse of inheritance. Now, any object of a subclass (e.g. any implementation of FlightRepository or CustomerRepository) can be used wherever the interface is expected, without breaking functionality. The Flight class properly extends the FlightDistance base and upholds the expected behaviors of that abstract class.
##  Interface Segregation Principle (ISP): 
I broke down duties into smaller, focused interfaces and classes. The prior DisplayClass interface (which combined several display operations) is eliminated in favor of more specific methods in service classes (displayFlightsByUser, displayPassengersForFlight, etc.), each handling a single task. We introduced CustomerRepository and FlightRepository interfaces to segregate data access responsibilities from other logic. Each interface has a narrow purpose – for example, FlightRepository only defines flight storage operations. This makes implementations simpler and clients (services) depend only on the methods they actually use.
## Dependency Inversion Principle (DIP): 
High-level modules (like CustomerService and ReservationService) depend on abstractions, not concrete lists or utility classes. For instance, CustomerService and FlightService interact with CustomerRepository and FlightRepository interfaces rather than directly manipulating array lists. This inversion makes the system more flexible and testable – we could swap in a different repository implementation without changing service code. Similarly, AuthService no longer relies on a static admin credential array in the User class; it uses an internal Map for credentials, and the main program calls its methods for authentication. This change decouples authentication logic from the UI and data storage details.
## Eliminating Duplication & Unused Code: 
The refactoring consolidates repeated logic. All ticket-booking operations now funnel through ReservationService.bookFlight, which uses the Booking class to update both the flight’s passenger list and the customer’s flight list in one step. This replaces the old parallel list updates and flightIndex lookups, reducing complexity and ensuring consistency. Unused or redundant code was removed (for example, the placeholder displayArtWork calls and the isUniqueData loop bug were corrected or streamlined). We also standardized formatting and messages across the application. All output tables are generated using consistent helper methods (see printCustomerTableHeader/printCustomerAsRow in CustomerService for passenger listings, and similar formatting in FlightService.displayFlightSchedule and ReservationService for bookings). This makes the output easier to read while preserving the original information and flow.


Overall, these structural improvements adhere to SOLID principles and make the airline reservation system easier to understand, extend, and maintain. The core functionality and user flow remain the same, but the code is now organized into logical components. This means new features (such as additional user roles, different storage backends, or enhanced validation rules) can be added with minimal changes to existing code, improving the system’s flexibility and robustness.
