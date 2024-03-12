package com.example.trenuri.Controller;

import com.example.trenuri.HelloApplication;
import com.example.trenuri.Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Start {
    private Service service;
    public void setService(Service service) {
        this.service=service;
    }

    @FXML
    public void connect_client(){
        try {
            FXMLLoader new_loader = new FXMLLoader(HelloApplication.class.getResource("client.fxml"));
            Scene root =new Scene( new_loader.load());

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("CONNECTED");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            dialogStage.setScene(root);

            Client clientiController = new_loader.getController();
            clientiController.setService(this.service);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
