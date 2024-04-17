package Controllers;

import DayTours.DayTourBooking;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controllers/Booking.fxml"));
        Parent root = loader.load();
        BookingController bookingController = loader.getController();

        bookingController.setBookingItems(Cart.getInstance().getBookings());

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("TravelApp");
        stage.setScene(scene);
        stage.show();

        //debug
        System.out.println("handleOrder");
    }


    @FXML
    public void handleRemoveObject(ActionEvent event) {
        InterfaceBooking selectedBooking = bookingListView.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Remove this item from the cart?", ButtonType.YES, ButtonType.NO);
            confirmAlert.setHeaderText(null);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    Cart.getInstance().removeBooking(selectedBooking);
                    bookingListView.getItems().remove(selectedBooking);
                    if (selectedBooking instanceof DayTourBooking){
                        BookDayTourController controller = new BookDayTourController();
                        controller.increaseCapacity((DayTourBooking) selectedBooking);
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No item selected!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
}
