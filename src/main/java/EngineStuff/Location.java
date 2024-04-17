package EngineStuff;

public class Location {
    float latitude;
    float longitude;
    String name;

    public Location(float latitude, float longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}