package DayTours;

import java.time.LocalDate;
import java.time.LocalTime;

public class DayTourBooking {
    private Tour tour;
    private User user;
    private LocalDate date;
    private LocalTime time;
    private String pickUpLocation;

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public DayTourBooking(Tour tour, User user, LocalDate date, LocalTime time, String pickUpLocation) {
        this.tour = tour;
        this.user = user;
        this.date = date;
        this.time = time;
        this.pickUpLocation = pickUpLocation;
    }

    public void updatePickUpLocation(String location) {
        this.pickUpLocation = location;
    }

    // Getters and setters for each field
    // ...

    public String viewBookingDetail(){
        return this.pickUpLocation + this.user.getName();
    }
}
