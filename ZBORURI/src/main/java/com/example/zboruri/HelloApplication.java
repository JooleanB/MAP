package com.example.zboruri;

import com.example.zboruri.Controller.Start;
import com.example.zboruri.Repo.ClientRepo;
import com.example.zboruri.Repo.FlightRepo;
import com.example.zboruri.Repo.TicketRepo;
import com.example.zboruri.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Service service;

    @Override
    public void start(Stage primaryStage) throws IOException {


        String URL = "jdbc:postgresql://localhost:5432/zboruri";
        String username = "postgres";
        String password = "fansteau2003";


        ClientRepo menuItemRepo = new ClientRepo(URL, username, password, "clients");
        FlightRepo orderRepo = new FlightRepo(URL, username, password, "flights");
        TicketRepo tableRepo = new TicketRepo(URL, username, password, "ticket");
        this.service = new Service(menuItemRepo, orderRepo, tableRepo);
        initView();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView() throws IOException {
        // Load and show the staff view
        FXMLLoader staffLoader = new FXMLLoader();
        staffLoader.setLocation(getClass().getResource("start.fxml"));
        AnchorPane staffLayout = staffLoader.load();
        Start startController = staffLoader.getController();
        startController.setService(this.service);

        Stage staffStage = new Stage();
        staffStage.setTitle("LOGIN");
        staffStage.setScene(new Scene(staffLayout));
        staffStage.show();

    }
}