module com.example.lab7gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jshell;





    opens com.example.lab7gui.service to javafx.fxml;
    exports com.example.lab7gui.service;

    exports com.example.lab7gui.controller;
    opens com.example.lab7gui.controller to javafx.fxml;

    opens com.example.lab7gui.repository to javafx.fxml;
    exports com.example.lab7gui.repository;

    opens com.example.lab7gui.domain to javafx.fxml;
    exports com.example.lab7gui.domain;

//    opens com.example.lab7gui.Utils.Observer to javafx.fxml;
//    exports com.example.lab7gui.Utils.Observer;
//
//    exports com.example.lab7gui.Utils.Events;
//    opens com.example.lab7gui.Utils.Events to javafx.fxml;

    exports com.example.lab7gui.validators;
    opens com.example.lab7gui.validators to javafx.fxml;

    opens com.example.lab7gui to javafx.fxml;
    exports com.example.lab7gui;
}