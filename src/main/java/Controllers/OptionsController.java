package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class OptionsController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button fxToggleTheme;

    private boolean isDarkTheme = false;

    @FXML
    public void toggleTheme() {
        if (isDarkTheme) {
            rootPane.getScene().getStylesheets().clear();
            rootPane.getScene().getStylesheets().add(getClass().getResource("/light-theme.css").toExternalForm());
            fxToggleTheme.setText("Switch to Dark Theme");
        } else {
            rootPane.getScene().getStylesheets().clear();
            rootPane.getScene().getStylesheets().add(getClass().getResource("/dark-theme.css").toExternalForm());
            fxToggleTheme.setText("Switch to Light Theme");
        }
        isDarkTheme = !isDarkTheme;
    }
}
