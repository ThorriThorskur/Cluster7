package EngineStuff;

import Interface.InterfaceBooking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
    private static Cart instance;
    private ObservableList<InterfaceBooking> bookings;

    private Cart() {
        bookings = FXCollections.observableArrayList();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public ObservableList<InterfaceBooking> getBookings() {
        return bookings;
    }

    public void addBooking(InterfaceBooking booking) {
        bookings.add(booking);
    }

    public void removeBooking(InterfaceBooking booking) {
        bookings.remove(booking);
    }
}