package HotelSystem;

import Interface.InterfaceBooking;
import Interface.InterfaceService;

import java.time.LocalDate;
import java.util.Date;
public class Reservation extends InterfaceBooking {
    private int reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalCost;
    private int guestId;
    private int roomId;

    // Constructor
    public Reservation(int reservationId, int roomId, LocalDate checkInDate, LocalDate checkOutDate, Double totalCost) {
        super(InterfaceBooking.ServiceType.Hotel);
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = totalCost;
    }

    public double calculateTotalCost() {
        // Placeholder for total cost calculation logic
        // This needs to be implemented based on specific rules
        return totalCost;
    }

    public int getReservationID() {
        return reservationId;
    }

    public void setReservationID(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Room Reservation " + reservationId +
                " - " + (int) totalCost + " kr";
    }
}
