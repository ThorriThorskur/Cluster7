package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalTime;

public class HomeController {
    @FXML
    private Label fxWelcomeLabel;

    @FXML
    public void initialize() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON)) {
            fxWelcomeLabel.setText("Good morning!");
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            fxWelcomeLabel.setText("Good afternoon!");
        } else {
            fxWelcomeLabel.setText("Good evening!");
        }
    }
}
