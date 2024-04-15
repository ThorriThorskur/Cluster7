package HotelSystem;

import Interface.InterfaceService;
import EngineStuff.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hotel extends InterfaceService {
    private String name;
    private Location location;
    private double rating;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private UUID hotelId;

    public Hotel(String name, Location location, double rating, List<Room> rooms, List<Reservation> reservations, UUID hotelId) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.rooms = rooms;
        this.reservations = reservations;
        this.hotelId = UUID.randomUUID();
    }

    public void createReservation() {}
    public void updateReservation() {}
    public void checkRoomAvailability(String checkInDate, String checkOutDate) {}
    public List<Room> getRooms() {
        if (rooms == null) {
            rooms = new ArrayList<>(); // Initialize rooms if null
            // Alternatively, you could load the rooms from a database here,
            // but then you would need to handle SQLExceptions.
        }
        return new ArrayList<>(rooms); // Return a copy of the rooms list to avoid external modifications.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(String locationString) {
        this.location = new Location(0,0,locationString);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public UUID getId() {
        return hotelId;
    }

    public void setHotelId(UUID hotelId) {
        this.hotelId = hotelId;
    }
    @Override
    public String toString() {
        return name + " | " + rating + " Rating";
    }
}
