package com.example.trenuri;


import com.example.trenuri.Controller.Start;
import com.example.trenuri.Repo.RepoCities;
import com.example.trenuri.Repo.RepoTicket;
import com.example.trenuri.Repo.RepoTrainStation;
import com.example.trenuri.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private Service service;

    @Override
    public void start(Stage primaryStage) throws IOException {


        System.out.println(getClass().getResource("client.fxml"));
        System.out.println(getClass().getResource("start.fxml"));

        String URL = "jdbc:postgresql://localhost:5432/trains";
        String username = "postgres";
        String password = "fansteau2003";

        try {
            Connection conn = DriverManager.getConnection(URL, username, password);
            System.out.println(conn);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cities LIMIT 1"); // Test query

            if (rs.next()) {
                System.out.println("Connection successful and table exists.");
            } else {
                System.out.println("Connection successful but table is empty or does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }



        RepoCities citiesRepo = new RepoCities(URL,username,password,"cities");
        RepoTrainStation repoTrainStation = new RepoTrainStation(URL,username,password,"trainstation");
        RepoTicket repoTicket = new RepoTicket(URL,username,password,"Tickets");
        this.service = new Service(citiesRepo,repoTrainStation,repoTicket);
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
        staffStage.setTitle("START");
        staffStage.setScene(new Scene(staffLayout));
        staffStage.show();
    }


}