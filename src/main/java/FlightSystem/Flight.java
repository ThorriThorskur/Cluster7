package FlightSystem;

import EngineStuff.Service;

import java.util.ArrayList;
import java.util.Date;

public class Flight extends Service {
    private String destination;

    private String location;
    private Date departureDate;

    private Date arrivalDate;
    private String id;
    private ArrayList<Seat> seats;
    private String status;

    private int flightDuration;

    static final int KR_PER_MIN = 300;

    // Constructor
    public Flight(String location, String destination, Date departureDate, Date arrivalDate, String id, ArrayList<Seat> seats, String status) {
        this.destination = destination;
        this.location = location;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.id = id;
        this.seats = seats;
        this.status = status;
        this.flightDuration = calculateDuration();
    }

    @Override
    public String toString() {
        //return getId() + " at " + FlightDB.fetchTime(getId(), String.valueOf(getDate()));
        return getId() + " at " + getDepartureDate().toString();
    }

    public int calculateDuration(){
        long timeDifference = getArrivalDate().getTime() - getDepartureDate().getTime();
        long minutes = timeDifference/(1000*60);
        return (int) minutes;
    }

    // Getters and Setters
    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDepartureDate() {
        return this.departureDate;
    }

    public int getDuration() {
        return this.flightDuration;
    }

    public void setDepartureDate(Date date) {
        this.departureDate = date;
        this.flightDuration = this.calculateDuration();
    }

    public Date getArrivalDate(){ return this.arrivalDate;}

    public void setArrivalDate(Date date){
        this.arrivalDate = date;
        this.flightDuration = this.calculateDuration();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Seat> getSeats() {
        return this.seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Other methods
    public ArrayList<Seat> getAvailableSeats() {
        ArrayList<Seat> availableSeats = new ArrayList<Seat>();
        for (Seat s : this.seats) {
            if (!s.getIsOnHold() && !s.getBooked()) {
                availableSeats.add(s);
            }
        }
        return availableSeats;
    }

    public Seat getSeat(String seatName){
        for (Seat seat : seats){
            if (seat.getSeatName().equals(seatName)){
                return seat;
            }
        }
        return null;
    }

    public int getStartingPrice() {
        return this.getDuration() * KR_PER_MIN;
    }

    // TODO: implement updateFlightStatus method
}
