package DayTours;

import EngineStuff.Location;
import Interface.InterfaceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Tour extends InterfaceService {

    private UUID id;
    private String name;
    private String description;

    private Float price;

    @Override
    public Location getLocation() {
        return location;
    }

    public String getLocationName() {
        return location.getName();
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private String locationName;

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    private Location location;
    private Language[] languages;
    private boolean childSafe;
    private boolean available;

    private String category;
    private boolean wheelchairAccessible;
    private float rating;

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getTimeDateTour() {
        return timeDateTour;
    }

    public void setTimeDateTour(LocalDateTime timeDateTour) {
        this.timeDateTour = timeDateTour;
    }

    private LocalDateTime timeDateTour;

    public LocalDate getDateOnly() {
        return timeDateTour.toLocalDate();
    }

    public void setDateOnly(LocalDate dateOnly) {
        this.dateOnly = dateOnly;
    }

    private LocalDate dateOnly;

    private int distance;

    private boolean availability;
    private int capacity;

    enum Language {
        ICELANDIC,
        ENGLISH,
        FRENCH,
        GERMAN,
        POLISH
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(boolean wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean getChildSafe() {
        return childSafe;
    }

    public void setChildSafe(boolean childSafe) {
        this.childSafe = childSafe;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public Tour(UUID id, String name, String description, String category, float price, int capacity, LocalDateTime timeDateTour, String location, float rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.wheelchairAccessible = true;
        this.rating = rating;
        this.timeDateTour = timeDateTour;
        this.location = new Location(0f, 0f, location);
        this.distance = 0;
        this.childSafe = true;
        this.availability = true;
    }

    public void updateTourDetails() {
    }

    public void rateTour(float rating) {
        this.rating = rating;
    }

    public void updateSpotsLeft() {
    }

    public UUID getTourId() {
        return id;
    }

    @Override
    public Float getPrice() {
        return price;
    }

    @Override
    public void setPrice(Float price) {
        this.price = price;
    }
}
