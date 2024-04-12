package Controllers;

import EngineStuff.Service;
import EngineStuff.ServiceController;

import java.util.Collection;
import java.util.List;

public class FlightController implements ServiceController {
    @Override
    public Collection search(String query) {
        return List.of();
    }

    @Override
    public Service[] recommend(Service[] trip) {
        return new Service[0];
    }

    @Override
    public void addService(Service service) {

    }

    @Override
    public void removeService(Service service) {

    }
}
