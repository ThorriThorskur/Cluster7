package HotelSystem;

import Interface.InterfaceService;
import EngineStuff.Location;
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


    public UUID getId() {
        return hotelId;
    }

    @Override
    public String toString() {
        return name + " | " + rating + " Rating";
    }
}
