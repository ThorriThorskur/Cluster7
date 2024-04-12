package FlightSystem;

import java.util.UUID;

public class BookingFlight {
    private Flight bookedFlight;
    private Seat seat;
    private Passenger passenger;
    private int bookingNumber;
    private boolean isPaidFor;
    private int bags;
    private int finalPrice;
    final int KR_PER_BAG = 5000;
    private UUID id;

    public BookingFlight(Flight bookedFlight, Seat seat, Passenger passenger, boolean isPaidFor, int bags) {
        this.bookedFlight = bookedFlight;
        this.seat = seat;
        this.passenger = passenger;
        this.bags = bags;
        this.isPaidFor = isPaidFor;
        // Set seat's status to on hold or booked
        if (!this.isPaidFor) {
            this.seat.setIsonHold(true);
            this.seat.setBooked(false);
        } else {
            this.seat.setIsonHold(false);
            this.seat.setBooked(true);
        }
        this.finalPrice = bookedFlight.getStartingPrice() + bags*KR_PER_BAG;
        this.id = UUID.randomUUID();
    }

    // Getters and setters
    public Flight getBookedFlight() {
        return this.bookedFlight;
    }

    public void setBookedFlight(Flight bookedFlight) {
        this.bookedFlight = bookedFlight;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Passenger getPassenger() {
        return this.passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public int getBookingNumber() {
        return this.bookingNumber;
    }

    public void setBookingNumber(int bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public boolean isPaidFor() {
        return this.isPaidFor;
    }

    public void setPaidFor(boolean isPaidFor) {
        this.isPaidFor = isPaidFor;
    }

    public int getBags() {
        return this.bags;
    }

    public void setBags(int bags) {
        this.bags = bags;
    }

    public String getId(){
        return this.id.toString();
    }

    // Other methods
    public void payForFlight() {
        this.isPaidFor = true;
        //this.seat.setIsOnHold(false);
        this.seat.setBooked(true);
    }

    public int getFinalPrice(){
        return finalPrice;
    }

    // Not implementing cancel method for now as TA mentioned it doesn't make sense
}
