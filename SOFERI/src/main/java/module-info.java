module com.example.soferi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.soferi to javafx.fxml;
    exports com.example.soferi;


    opens com.example.soferi.Controller to javafx.fxml;
    exports com.example.soferi.Controller;

    opens com.example.soferi.Domain to javafx.fxml;
    exports com.example.soferi.Domain;

    opens com.example.soferi.Service to javafx.fxml;
    exports com.example.soferi.Service;

    opens com.example.soferi.Repository to javafx.fxml;
    exports com.example.soferi.Repository;
}