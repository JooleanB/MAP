package com.example.zboruri.Controller;

import com.example.zboruri.Domain.Client;
import com.example.zboruri.HelloApplication;
import com.example.zboruri.Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Start {
    private Service service;

    @FXML
    private TextField username = new TextField("Enter username");
    public void setService(Service service) {
        this.service=service;
    }

    public void logIN(){
        String username_string = this.username.getText();
        if(username_string==null || username_string.isEmpty()){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST ENTER AN USERNAME!");
            message.showAndWait();
        }
        Iterable<Client> lista_clienti = this.service.findAll_client();
        for(Client c:lista_clienti){
            if(Objects.equals(c.getUsername(), username_string)){
                Alert message= new Alert(Alert.AlertType.INFORMATION);
                message.setTitle("DONE!");
                message.setContentText("USER LOGGED IN!");
                message.showAndWait();
                try {
                    FXMLLoader new_loader = new FXMLLoader(HelloApplication.class.getResource("client.fxml"));
                    Scene root =new Scene( new_loader.load());

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle(c.getUsername()+" LOGGED IN");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    //dialogStage.initOwner(primaryStage);
                    dialogStage.setScene(root);

                    ClientController clientiController = new_loader.getController();
                    clientiController.setService(this.service,c);

                    dialogStage.show();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Alert message= new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("EROARE!");
        message.setContentText("USERNAME NOT FOUND!");
        message.showAndWait();
    }
}
