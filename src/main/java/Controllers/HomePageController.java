package Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private Label myLabel;

    @FXML
    private StackPane contentPane;

    @FXML
    private StackPane cartPane;

    String[] options = {"Home", "Flights", "Hotels", "Day tours"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuListView.getItems().addAll(options);

        // Set the preferred height of the ListView
        updateListViewHeight();

        menuListView.getSelectionModel().select("Home");
        loadView("Home");
        loadCart();

        // Listener for changes in the list selection
        menuListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            myLabel.setText(newValue);
            loadView(newValue);
        });
    }

    private void updateListViewHeight() {
        double height = 0;
        int itemsCount = menuListView.getItems().size();
        double itemHeight = 24; // Assuming each item's height is 24 pixels; adjust as necessary

        // Calculating total height for all items
        height = itemsCount * itemHeight;

        // Set padding or extra spacing if needed
        double padding = 2; // Extra space at the bottom or top as needed
        height += padding;

        // Setting the preferred height of the ListView
        menuListView.setPrefHeight(height);
    }



    private void loadView(String viewName) {
        Node node = null;
        try {
            switch (viewName) {
                case "Home":
                    node = FXMLLoader.load(getClass().getResource("/Controllers/Home.fxml"));
                    break;
                case "Flights":
                    node = FXMLLoader.load(getClass().getResource("/Controllers/Flights.fxml"));
                    break;
                case "Hotels":
                    node = FXMLLoader.load(getClass().getResource("/Controllers/Hotels.fxml"));
                    break;
                case "Day tours":
                    node = FXMLLoader.load(getClass().getResource("/Controllers/DayTours.fxml"));
                    break;
                default:
                    System.out.println("No panel available for this selection.");
                    break;
            }
            if (node != null) {
                contentPane.getChildren().setAll(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCart() {
        try {
            Node cartContent = FXMLLoader.load(getClass().getResource("/Controllers/Cart.fxml"));
            cartPane.getChildren().setAll(cartContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
