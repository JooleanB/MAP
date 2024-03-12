package com.example.restaurant;


import com.example.restaurant.Controller.StaffController;
import com.example.restaurant.Controller.TableController;
import com.example.restaurant.Repository.MenuItemRepo;
import com.example.restaurant.Repository.OrderRepo;
import com.example.restaurant.Repository.TableRepo;
import com.example.restaurant.Service.ServiceDB;
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


        MenuItemRepo menuItemRepo = new MenuItemRepo(URL,username,password,"MenuItem");
        OrderRepo orderRepo = new OrderRepo(URL,username,password,"Orders");
        TableRepo tableRepo = new TableRepo(URL,username,password,"_Table");
        this.service = new ServiceDB(menuItemRepo,orderRepo,tableRepo);
        initView();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView() throws IOException {
        // Load and show the staff view
        FXMLLoader staffLoader = new FXMLLoader();
        staffLoader.setLocation(getClass().getResource("staff.fxml"));
        AnchorPane staffLayout = staffLoader.load();
        StaffController staffController = staffLoader.getController();
        staffController.setService(this.service);

        Stage staffStage = new Stage();
        staffStage.setTitle("STAFF");
        staffStage.setScene(new Scene(staffLayout));
        staffStage.show();
        for (int i = 0; i < service.get_count(); i++) {
            FXMLLoader tableLoader = new FXMLLoader();
            tableLoader.setLocation(getClass().getResource("table.fxml"));
            AnchorPane tableLayout = tableLoader.load();
            TableController tableController = tableLoader.getController();
            tableController.setService(this.service,i);

            Stage tableStage = new Stage();
            tableStage.setTitle("Table "+i);
            tableStage.setScene(new Scene(tableLayout));
            tableStage.show();
        }
    }


}