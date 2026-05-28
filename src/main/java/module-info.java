module com.hospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens com.hospital.controllers to javafx.fxml;
    opens com.hospital.models to javafx.base;

    exports com.hospital;
}
