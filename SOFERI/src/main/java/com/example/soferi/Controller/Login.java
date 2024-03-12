package com.example.soferi.Controller;

import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Sofer;
import com.example.soferi.HelloApplication;
import com.example.soferi.Service.ServiceDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Login {

    private ServiceDB service;


    @FXML
    private TextField username;

    public Login() {
    }

    public void setService(ServiceDB serv){
        this.service=serv;
    }
    @FXML
    public void showController() {
        Iterable<Persoana> persoane = this.service.getClienti();
        Iterable<Sofer> soferi = this.service.getSoferi();
        String nume = this.username.getText();
        if(nume==null || nume.isEmpty()){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST INPUT AN UNSERNAME!");
            message.showAndWait();
        }
        for(Persoana p : persoane){
            if(Objects.equals(p.getNume(), nume)){
                Alert message= new Alert(Alert.AlertType.INFORMATION);
                message.setTitle("CLIENT CONECTAT");
                message.setContentText("MUIEEEEEEE ");
                message.showAndWait();
                try {
                    FXMLLoader new_loader = new FXMLLoader(HelloApplication.class.getResource("client.fxml"));
                    Scene root =new Scene( new_loader.load());

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Log In");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    //dialogStage.initOwner(primaryStage);
                    dialogStage.setScene(root);

                    ControllerClienti clientiController = new_loader.getController();
                    clientiController.setService(this.service, dialogStage,p);

                    dialogStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        for(Sofer s : soferi){
            if(Objects.equals(s.getNume(), nume)){
                Alert message= new Alert(Alert.AlertType.INFORMATION);
                message.setTitle("SOFER CONECTAT");
                message.setContentText("MUIEEEEEE");
                message.showAndWait();
                try {
                    FXMLLoader new_loader = new FXMLLoader(HelloApplication.class.getResource("sofer.fxml"));
                    Scene root =new Scene( new_loader.load());

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Log In");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    //dialogStage.initOwner(primaryStage);
                    dialogStage.setScene(root);

                    ControllerSoferi soferiController = new_loader.getController();
                    soferiController.setService(this.service, dialogStage,s);

                    dialogStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }


}

