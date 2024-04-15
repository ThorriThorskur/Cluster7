package HotelSystem;

import java.util.List;

public class Room {
    private int roomNumber;
    private RoomType type;
    private double pricePerNight;
    private boolean isAvailable;

    public enum RoomType {
        Single,
        Double,
        Suite,
        Deluxe
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public String getRoomNumberAsString() {
        return String.valueOf(roomNumber); // Convert integer or other type to String
    }

    public String getRoomTypeAsString() {
        return type.toString(); // Assuming type is an enum or similar
    }

    public String getAvailabilityAsString() {
        return isAvailable ? "Available" : "Unavailable"; // Convert boolean to String
    }
}

