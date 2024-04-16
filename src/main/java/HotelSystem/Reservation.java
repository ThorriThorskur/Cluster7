package HotelSystem;

import Interface.InterfaceBooking;
import java.time.LocalDate;
public class Reservation extends InterfaceBooking {
    private int reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalCost;
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

    @Override
    public String getType() {
        return "Hotel";
    }

    @Override
    public String getDetails() {
        return "Reservation ID: " + reservationId + ", Room ID: " + roomId +
                ", Check-in: " + checkInDate + ", Check-out: " + checkOutDate;
    }

    @Override
    public double getPrice() {
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

    public int getRoomId() {
        return roomId;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
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
