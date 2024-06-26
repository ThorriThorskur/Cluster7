package FlightSystem;

import Interface.InterfaceService;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import EngineStuff.Location;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Flight extends InterfaceService {
    private UUID id;
    private StringProperty name;
    private StringProperty locationProperty;
    private StringProperty destinationProperty;
    private StringProperty depTimeProperty;
    private StringProperty arrTimeProperty;
    private StringProperty priceProperty;
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

    private static final HashMap<String, float[]> coordinates = new HashMap<>();
    static {
        coordinates.put("Reykjavik Domestic Airport (RKV)", new float[] {64.1470f,-21.9408f});
        coordinates.put("Akureyri Domestic Airport (AEY)", new float[] {65.6586f,-18.0722f});
        coordinates.put("Egilsstaðir Domestic Airport (EGS)", new float[] {65.283333f,-14.401388f});
        coordinates.put("Bíldudalur Domestic Airport (BIU)", new float[] {65.6417f,-23.5465f});
        coordinates.put("Hornafjorður Airport (HFN)", new float[] {64.3022f,-15.2264f});
        coordinates.put("Húsavík Airport (HZK)", new float[] {65.9556f,-17.4192f});
        coordinates.put("Isafjorður Airport (IFJ)", new float[] {66.0581f,-23.1341f});
        coordinates.put("Sauðárkrókur Airport (SAK)", new float[] {65.7324f,-19.5736f});
        coordinates.put("Thorshofn Airport (THO)", new float[]{66.2204f,-15.3312f});
        coordinates.put("Vestmannaeyjar Airport (VEY)", new float[]{63.4252f,-20.2693f});
        coordinates.put("Grimsey Airport (GRY)", new float[] {66.5478f,-18.0208f});
        coordinates.put("Vopnafjorður Airport (VPN)", new float[] {65.7206f,-14.8506f});
        coordinates.put("Gjogur Airport (GJR)", new float[]{65.9951f,-21.3298f});
    }



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
        this.name = new SimpleStringProperty(name);
        this.description = "Flight from " + location + " to " + destination + " on " + departureDate.toString();
        this.location = new Location(getCoordinates(location)[0], getCoordinates(location)[1], location);
        this.locationProperty = new SimpleStringProperty(location);
        this.destination = new Location(getCoordinates(destination)[0], getCoordinates(destination)[1], destination); // TODO: Add real coordinates
        this.destinationProperty = new SimpleStringProperty(destination);
        this.departureDate = departureDate;
        this.depTimeProperty = new SimpleStringProperty(getTime(departureDate));
        this.arrivalDate = arrivalDate;
        this.arrTimeProperty = new SimpleStringProperty(getTime(arrivalDate));

        this.description = "Flight from " + location + " to " + destination + " on " + departureDate.toString();
        this.seats = seats;
        this.status = status;
        this.flightDuration = this.calculateDuration();
        this.price = (float) this.getStartingPrice();

        this.priceProperty = new SimpleStringProperty(String.valueOf(price));

        this.languages = Language.values();
        this.childSafe = true;
        this.available = this.getAvailableSeats().size() > 0;
    }

    public String getTime(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    public String getName(){
        return name.get();
    }

    public StringProperty nameProperty(){
        return name;
    }

    public String getLocationProperty(){
        return locationProperty.get();
    }

    public StringProperty locationProperty(){
        return locationProperty;
    }

    public String getDestinationProperty(){
        return destinationProperty.get();
    }

    public StringProperty destinationProperty(){
        return destinationProperty;
    }

    public String getDepTimeProperty(){
        return depTimeProperty.get();
    }

    public StringProperty depTimeProperty(){
        return depTimeProperty;
    }

    public String getArrTimeProperty(){
        return arrTimeProperty.get();
    }

    public StringProperty arrTimeProperty(){
        return arrTimeProperty;
    }

    public String getPriceProperty(){
        return priceProperty.get();
    }

    public StringProperty priceProperty(){
        return priceProperty;
    }


    private float [] getCoordinates(String location){
        return coordinates.get(location);
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
