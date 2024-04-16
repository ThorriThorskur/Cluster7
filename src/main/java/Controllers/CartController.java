package Controllers;

import EngineStuff.Cart;
import Interface.InterfaceBooking;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class CartController {
    private Stage stage;
    private Scene scene;
    @FXML
    private ListView<InterfaceBooking> bookingListView;

    @FXML
    public void initialize() {
        ObservableList<InterfaceBooking> bookingObservableList = FXCollections.observableArrayList(Cart.getInstance().getBookings());
        bookingListView.setItems(bookingObservableList);

        Cart.getInstance().getBookings().addListener((ListChangeListener.Change<? extends InterfaceBooking> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    bookingObservableList.addAll(change.getAddedSubList());
                } else if (change.wasRemoved()) {
                    bookingObservableList.removeAll(change.getRemoved());
                }
            }
        });
    }
    public void handleOrder(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Booking.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("TravelApp");
        stage.setScene(scene);
        stage.show();

        //debug
        System.out.println("handleOrder");
    }


}
