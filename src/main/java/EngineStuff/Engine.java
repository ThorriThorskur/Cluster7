package EngineStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import Interface.InterfaceService;
import Interface.InterfaceServiceController;

public class Engine {
    InterfaceServiceController[] controllers;

    Engine(InterfaceServiceController[] controllers) {
        this.controllers = controllers;
    }

    public InterfaceService[] search(String str) {
        ArrayList<InterfaceService> result = new ArrayList<>();

        for (InterfaceServiceController sc : controllers) {
            Collection<?> rawResults = sc.search(str);
            for (Object o : rawResults) {
                if (o instanceof InterfaceService) {
                    result.add((InterfaceService) o);
                }
            }
        }
        return result.toArray(new InterfaceService[0]);
    }

    public Collection<?> search(InterfaceServiceController controller, String str) {
        return controller.search(str);
    }

    public InterfaceService[] sort(InterfaceService[] services, SortingParam sortingParam, boolean reverse) {
        Comparator<InterfaceService> comp = null;

        switch (sortingParam) {
            case ID:
                comp = Comparator.comparing(InterfaceService::getId);
                break;
            case PRICE:
                comp = Comparator.comparingDouble(InterfaceService::getPrice);
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

    public InterfaceService[] filter(InterfaceService[] services, Filter[] filters) {
        ArrayList<InterfaceService> filteredServices = new ArrayList<>();
        for (InterfaceService s : services) {
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
        return filteredServices.toArray(new InterfaceService[0]);
    }

    public InterfaceService[] recommend(Trip trip) {
        // This needs to be implemented in accordance with your application logic
        return new InterfaceService[0];
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

