module team.t.hugb {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens Controllers to javafx.fxml;
    exports Controllers;
}