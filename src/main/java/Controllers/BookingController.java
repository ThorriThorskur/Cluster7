package Controllers;

import Interface.InterfaceBooking;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;

public class BookingController {
    @FXML
    private TableView<InterfaceBooking> myTableView;
    @FXML
    private TableColumn<InterfaceBooking, String> typeColumn;
    @FXML
    private TableColumn<InterfaceBooking, String> detailsColumn;
    @FXML
    private TableColumn<InterfaceBooking, Double> priceColumn;

    private Stage stage;
    private Scene scene;

    @FXML
    public void initialize() {
        // Setup columns
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("TravelApp");
        stage.setScene(scene);
        stage.show();

        //debug
        System.out.println("handleBack");
    }

    public void setBookingItems(ObservableList<InterfaceBooking> items) {
        myTableView.setItems(items);
    }
}
