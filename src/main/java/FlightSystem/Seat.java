package FlightSystem;

public class Seat {
    private String seatName;
    private String Tier;
    private Boolean isBooked;
    private Boolean isonHold;

    public Seat(String seatName, Boolean isBooked, Boolean isonHold)   {
        this.seatName = seatName;
        // make tier be calculated and its value set here in the seat class.
        //this.Tier = Tier;
        this.isBooked = isBooked;
        this.isonHold = isonHold;
    }

    public Boolean getIsonHold() {
        return isonHold;
    }
    //will need to be set to false by default
    public void setIsonHold(Boolean onHold) {
        this.isonHold = onHold;
    }

    public Boolean getBooked() {
        return isBooked;
    }
    //will need to be set to false by default
    public void setBooked(Boolean booked) {
        isBooked = booked;
    }

    public String getTier() {
        return Tier;
    }

    public void setTier(String tier) {
        Tier = tier;
    }

    public String getSeatName() {
        return seatName;
    }
    public void setSeatName(String seatname) {
        seatName = seatName;
    }

    @Override
    public String toString(){
        return seatName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return this.seatName != null ? seatName.equals(seat.seatName) : seat.seatName == null;
    }

    // Hey Brynjar, had to add this method because the program wouldn't run. ~ Cat
    public boolean getIsOnHold() {
        // just putting some value here so the program will run.
        return false;
    }
}
