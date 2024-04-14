package DayTours;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private ArrayList<DayTourBooking> bookings;
    private UUID id;
    private String name;
    private String email;
    private String userType;
    private String location;

    public ArrayList<DayTourBooking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<DayTourBooking> bookings) {
        this.bookings = bookings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Constructor
    public User(UUID id, String name, String email) {
        this.id = id;
        this.bookings = new ArrayList<>();
        this.name = name;
        this.email = email;


    }

    // Method to update user profile
    public void updateProfile() {
    }

    public UUID getId() {
        return id;
    }
}
