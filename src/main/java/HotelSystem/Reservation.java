package HotelSystem;

import Interface.InterfaceService;

import java.util.Date;
public class Reservation {
    private int reservationId;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalCost;
    private int guestId;
    private int roomId;

    // Constructor
    public Reservation(int reservationId,int guestId, int roomId, Date checkInDate, Date checkOutDate) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = 0.0;
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

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
