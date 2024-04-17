package Controllers;

import DayTours.DayTourBooking;
import DayTours.Tour;
import DayTours.User;
import EngineStuff.Cart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class BookDayTourController {

    @FXML
    private TextField fxTourName;
    @FXML
    private Label fxPrice;

    @FXML
    private TextField fxTourEmail;
    @FXML
    private TextField fxPhoneNumber;

    private Tour selectedTour;
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Databases/tours.db";

    @FXML
    private Label fxtitle;

    @FXML
    private Label fxErrorMessage;
    private DayTourController dayTourController;




    @FXML
    private void handleConfirmBooking() throws ClassNotFoundException, SQLException {
        User currentUser = new User(UUID.randomUUID(), fxTourName.getText(), fxTourEmail.getText());
        DayTourBooking bookingTour = new DayTourBooking(UUID.randomUUID(), selectedTour, currentUser, LocalDate.now(), LocalTime.now(), selectedTour.getLocationName());
        if (selectedTour.getCapacity() > 0) {
            if (!fxTourName.getText().isBlank() && !fxPhoneNumber.getText().isBlank() && !fxPhoneNumber.getText().matches(".*[a-zA-Z]+.*") && fxTourEmail.getText().contains("@")) {
                insertUser(currentUser);

                String sqlTour = "UPDATE Tours SET capacity = ?, availability = ? WHERE tourID = ?";
                String sqlcap = "SELECT capacity FROM Tours WHERE tourID = ?";;


                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement pstmtTour = conn.prepareStatement(sqlTour);
                     PreparedStatement pstmcap = conn.prepareStatement(sqlcap);
                ) {
                    String tourIdToSearch = selectedTour.getTourId().toString();
                    pstmcap.setString(1, tourIdToSearch);
                    ResultSet capres = pstmcap.executeQuery();
                    if (capres.next()) {
                        int newCapacity = capres.getInt(1);
                        newCapacity = newCapacity - 1;
                        boolean newAvailability = newCapacity > 0;
                        pstmtTour.setInt(1, newCapacity);
                        pstmtTour.setBoolean(2, newAvailability);
                        pstmtTour.setString(3, tourIdToSearch);
                        pstmtTour.executeUpdate(); // Perform the update
                        selectedTour.setCapacity(newCapacity);
                    } else {
                        System.out.println("No data found for the tour ID: " + tourIdToSearch);
                    }
                } catch (SQLException e) {
                    System.out.println("Error occurred while adding the booking: " + e.getMessage());
                }

                Cart.getInstance().addBooking(bookingTour);
                initData(selectedTour, dayTourController);
                /* update();*/
                clearFields();
                Stage stage = (Stage) fxTourName.getScene().getWindow();
                stage.close();
            }
            else {
                fxErrorMessage.setText("Sorry, Please enter the correct details.");
            }
        }
        else {
            fxErrorMessage.setText("Sorry, this tour is fully booked.");
        }
    }

    public void increaseCapacity(DayTourBooking book){
        String sqlTour = "UPDATE Tours SET capacity = ?, availability = ? WHERE tourID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtTour = conn.prepareStatement(sqlTour)) {

            int newCapacity = book.getTour().getCapacity() + 1;
            boolean newAvailability = newCapacity > 0;
            pstmtTour.setInt(1, newCapacity);
            pstmtTour.setBoolean(2, newAvailability);
            pstmtTour.setString(3, book.getTour().getTourId().toString());
            pstmtTour.executeUpdate();
            book.getTour().setCapacity(newCapacity);

        } catch (SQLException e) {
            System.out.println("Error occurred while adding the booking: " + e.getMessage());
        }

    }

    private void clearFields(){
        fxTourEmail.clear();
        fxTourName.clear();
        fxPhoneNumber.clear();
        fxErrorMessage.setText("");
    }
    public void initData(Tour tour, DayTourController dayTourController) throws ClassNotFoundException, SQLException {
        this.selectedTour = tour;
        fxtitle.setText(selectedTour.getName() + " " + selectedTour.getDateOnly());
        fxPrice.setText(String.valueOf(selectedTour.getPrice()) + " kr.");
        this.dayTourController =dayTourController;
        ActionEvent event = new ActionEvent();
        dayTourController.searchClick(event);

    }

    public void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (userID, name, email, userType) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getId().toString());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, "Regular");

            pstmt.executeUpdate();
            System.out.println("User inserted: " + user.getName());
        } catch (SQLException e) {
            System.out.println("Error occurred while inserting the user: " + e.getMessage());
            throw e;
        }
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
