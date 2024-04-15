package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class BookFlightController {
   @FXML
    public void handleCancel(ActionEvent actionEvent) {
       Node source = (Node) actionEvent.getSource();
       Stage stage = (Stage) source.getScene().getWindow();
       stage.close();
    }
}
