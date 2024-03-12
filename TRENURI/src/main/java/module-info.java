module com.example.trenuri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.trenuri to javafx.fxml;
    exports com.example.trenuri;

    opens com.example.trenuri.Service to javafx.fxml;
    exports com.example.trenuri.Service;

    opens com.example.trenuri.Domain to javafx.fxml;
    exports com.example.trenuri.Domain;

    opens com.example.trenuri.Repo to javafx.fxml;
    exports com.example.trenuri.Repo;

    opens com.example.trenuri.Controller to javafx.fxml;
    exports com.example.trenuri.Controller;

}