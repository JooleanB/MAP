module com.example.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.restaurant to javafx.fxml;
    exports com.example.restaurant;


    opens com.example.restaurant.Domain to javafx.fxml;
    exports com.example.restaurant.Domain;

    opens com.example.restaurant.Controller to javafx.fxml;
    exports com.example.restaurant.Controller;

    opens com.example.restaurant.Service to javafx.fxml;
    exports com.example.restaurant.Service;

    opens com.example.restaurant.Repository to javafx.fxml;
    exports com.example.restaurant.Repository;
}