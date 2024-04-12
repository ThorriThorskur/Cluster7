package FlightSystem;

import Interface.InterfaceService;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import EngineStuff.Location;

public class Flight extends InterfaceService {
    private UUID id;
    private String name;
    private String description;
    private Float price;
    private Location location;
    private Language[] languages;
    private boolean childSafe;
    private boolean available;

    private Location destination;
    private Date departureDate;
    private Date arrivalDate;
    private ArrayList<Seat> seats;
    private String status;
    private int flightDuration;
    static final int KR_PER_MIN = 300;

    enum Language {
        ICELANDIC,
        ENGLISH,
        FRENCH,
        GERMAN,
        POLISH
    }

    // Constructor
    public Flight(String location, String destination, Date departureDate, Date arrivalDate, String name, ArrayList<Seat> seats, String status) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = "Flight from " + location + " to " + destination + " on " + departureDate.toString();
        this.location = new Location(0f, 0f, location); // TODO: Add real coordinates
        this.destination = new Location(0f, 0f, destination); // TODO: Add real coordinates
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;

        this.description = "Flight from " + location + " to " + destination + " on " + departureDate.toString();
        this.seats = seats;
        this.status = status;
        this.flightDuration = this.calculateDuration();
        this.price = (float) this.getStartingPrice();

        this.languages = Language.values();
        this.childSafe = true;
        this.available = this.getAvailableSeats().size() > 0;
    }

    @Override
    public String toString() {
        //return getId() + " at " + FlightDB.fetchTime(getId(), String.valueOf(getDate()));
        return getId() + " at " + getDepartureDate().toString();
    }

    @Override
    public boolean getAvailable() {
        return this.getAvailableSeats().size() > 0;
    }

    public int calculateDuration(){
        long timeDifference = this.getArrivalDate().getTime() - this.getDepartureDate().getTime();
        long minutes = timeDifference/(1000*60);
        return (int) minutes;
    }

    public Location getDestination() {
        return this.destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public int getDuration() {
        return this.flightDuration;
    }

    public Date getArrivalDate() {
        return this.arrivalDate;
    }

    public Date getDepartureDate() {
        return this.departureDate;
    }

    public void setDepartureDate(Date date) {
        this.departureDate = date;
        this.flightDuration = this.calculateDuration();
    }

    public void setArrivalDate(Date date){
        this.arrivalDate = date;
        this.flightDuration = this.calculateDuration();
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
}
