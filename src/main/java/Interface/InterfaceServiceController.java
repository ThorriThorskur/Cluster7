package Interface;

import java.util.Collection;

public interface InterfaceServiceController {
    public Collection search(String query);
    public void addService(InterfaceService service);
    public void removeService(InterfaceService service);
    // public Booking book(specialNeeds sn, Service service, User user);
}
