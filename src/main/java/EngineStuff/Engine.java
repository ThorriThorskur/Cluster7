package EngineStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class Engine {
    ServiceController[] controllers;

    Engine(ServiceController[] controllers) {
        this.controllers = controllers;
    }

    public Service[] search(String str) {
        ArrayList<Service> result = new ArrayList<>();

        for (ServiceController sc : controllers) {
            Collection<?> rawResults = sc.search(str);
            for (Object o : rawResults) {
                if (o instanceof Service) {
                    result.add((Service) o);
                }
            }
        }
        return result.toArray(new Service[0]);
    }

    public Collection<?> search(ServiceController controller, String str) {
        return controller.search(str);
    }

    public Service[] sort(Service[] services, SortingParam sortingParam, boolean reverse) {
        Comparator<Service> comp = null;

        switch (sortingParam) {
            case ID:
                comp = Comparator.comparing(Service::getId);
                break;
            case PRICE:
                comp = Comparator.comparingDouble(Service::getPrice);
                break;
        }

        if (comp != null) {
            if (reverse) {
                comp = comp.reversed();
            }
            Arrays.sort(services, comp);
        }

        return services;
    }

    public Service[] filter(Service[] services, Filter[] filters) {
        ArrayList<Service> filteredServices = new ArrayList<>();
        for (Service s : services) {
            boolean pass = true;
            for (Filter f : filters) {
                pass = pass && f.filter(s);
                if (!pass) {
                    break; // Break early if any filter fails
                }
            }
            if (pass) {
                filteredServices.add(s);
            }
        }
        return filteredServices.toArray(new Service[0]);
    }

    public Service[] recommend(Trip trip) {
        // This needs to be implemented in accordance with your application logic
        return new Service[0];
    }

    public void contactAdministrators(String message) {
        // This needs to be implemented in accordance with your application logic
    }

    enum SortingParam {
        ID,
        PRICE
    }

    // Assuming Service, Filter, and Trip are defined elsewhere
}

