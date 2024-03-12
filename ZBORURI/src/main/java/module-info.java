module com.example.zboruri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.zboruri to javafx.fxml;
    exports com.example.zboruri;


    opens com.example.zboruri.Service to javafx.fxml;
    exports com.example.zboruri.Service;

    opens com.example.zboruri.Repo to javafx.fxml;
    exports com.example.zboruri.Repo;

    opens com.example.zboruri.Controller to javafx.fxml;
    exports com.example.zboruri.Controller;

    opens com.example.zboruri.Domain to javafx.fxml;
    exports com.example.zboruri.Domain;
}