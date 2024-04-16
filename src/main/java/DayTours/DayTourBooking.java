package DayTours;

import Interface.InterfaceBooking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class DayTourBooking extends InterfaceBooking {
    private Tour tour;
    private User user;
    private LocalDate date;
    private LocalTime time;
    private String pickUpLocation;

    private UUID id;

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

    public DayTourBooking(UUID uuid, Tour tour, User user, LocalDate date, LocalTime time, String pickUpLocation) {
        super(InterfaceBooking.ServiceType.Tour);

        this.id = uuid;
        this.tour = tour;
        this.user = user;
        this.date = date;
        this.time = time;
        this.pickUpLocation = pickUpLocation;
    }

    @Override
    public String toString() {
        return getUser().getName() + " - " + getTour().getName() + " - " + getTour().getPrice() + " kr";
    }
    public void updatePickUpLocation(String location) {
        this.pickUpLocation = location;
    }

    public String viewBookingDetail(){
        return this.pickUpLocation + this.user.getName();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return "Tour";
    }

    @Override
    public String getDetails() {
        return getTour().getName()+ " - " + getTour().getDescription();
    }

    @Override
    public double getPrice() {
        return getTour().getPrice();
    }
}
