module team.t.hugb {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens Controllers to javafx.fxml;
    exports Controllers;

    opens DayTours to javafx.base;
    opens FlightSystem to javafx.base;
    opens HotelSystem to javafx.base;
}