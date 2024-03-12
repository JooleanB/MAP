package com.example.soferi;

import com.example.soferi.Controller.Login;
import com.example.soferi.Repository.RepoComanda;
import com.example.soferi.Repository.RepoPersoane;
import com.example.soferi.Repository.RepoSoferi;
import com.example.soferi.Service.ServiceDB;
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
    private ServiceDB service;

    @Override
    public void start(Stage primaryStage) throws IOException {


        System.out.println(getClass().getResource("client.fxml"));
        System.out.println(getClass().getResource("sofer.fxml"));

        String URL = "jdbc:postgresql://localhost:5432/taximetrie";
        String username = "postgres";
        String password = "fansteau2003";


        try {
            Connection conn = DriverManager.getConnection(URL, username, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sofer LIMIT 1"); // Test query

            if (rs.next()) {
                System.out.println("Connection successful and table exists.");
            } else {
                System.out.println("Connection successful but table is empty or does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }


        RepoPersoane repoPersoane = new RepoPersoane(URL,username,password,"persoana");
        RepoSoferi repoSoferi = new RepoSoferi(URL,username,password,"sofer");
        RepoComanda repoComanda = new RepoComanda(URL,username,password,"comanda");
        this.service = new ServiceDB(repoPersoane,repoSoferi,repoComanda);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("login.fxml"));
        AnchorPane userLayout = Loader.load();
        primaryStage.setScene(new Scene(userLayout));

        Login startController = Loader.getController();
        startController.setService(this.service);
    }

}