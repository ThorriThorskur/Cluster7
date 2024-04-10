package EngineStuff;

import java.util.Collection;

public interface ServiceController {
    public Collection search(String query);
    public Service[] recommend(Service[] trip);
    public void addService(Service service);
    public void removeService(Service service);
    // public Booking book(specialNeeds sn, Service service, User user);
}
