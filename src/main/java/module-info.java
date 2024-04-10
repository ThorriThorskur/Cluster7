module team.t.hugb {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens Controllers to javafx.fxml;
    exports Controllers;
}