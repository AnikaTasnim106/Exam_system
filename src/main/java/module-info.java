module com.buet.edutrack {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.buet.edutrack to javafx.fxml;
    opens com.buet.edutrack.controllers to javafx.fxml;

    exports com.buet.edutrack;
    exports com.buet.edutrack.controllers;
}