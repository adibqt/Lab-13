import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String userID;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private int age;
    private List<Booking> bookings;

    public Customer(String name, String email, String password, String phone, String address, int age, String userID) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.bookings = new ArrayList<>();
    }

    public String getUserID() { return userID; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }
    public String getPassword() { return password; }
    public String getPhone()  { return phone; }
    public String getAddress() { return address; }
    public int getAge()       { return age; }
    public List<Booking> getBookings() { return bookings; }

    public void setName(String name)     { this.name = name; }
    public void setEmail(String email)   { this.email = email; }
    public void setPhone(String phone)   { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setAge(int age)         { this.age = age; }

    // Add a booking to this customer's list
    void addBooking(Booking booking) {
        bookings.add(booking);
    }

    // Remove a booking from this customer's list
    void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
}
