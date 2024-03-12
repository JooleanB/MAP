package com.example.lab7gui.controller;

import com.example.lab7gui.service.ServiceDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartController {

    private ServiceDB service;

    private Stage stage;

    @FXML
    private TextField number_of_entities;

    public StartController() {
    }

    public void setService(ServiceDB serv,Stage stage){
        this.service=serv;
        this.stage=stage;
    }
    public void showController() {
        String n = this.number_of_entities.getText();
        try
        {
            int x =Integer.parseInt(n);
            if(x>0){
                try {
                    this.service.set_limit_offset(x,0);
                    // create a new stage for the popup dialog.
                    FXMLLoader new_loader = new FXMLLoader();
                    new_loader.setLocation(getClass().getResource("/com/example/lab7gui/user.fxml"));

                    AnchorPane root = new_loader.load();

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("ADMIN MODE");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    //dialogStage.initOwner(primaryStage);
                    Scene scene = new Scene(root);
                    dialogStage.setScene(scene);

                    Controller controller = new_loader.getController();

                    controller.setService(this.service);


                    dialogStage.show();
                    this.stage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch(NumberFormatException eroare){
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT A NUMBER OF USERS ON PAGE");
        }
    }

    public void signup() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader new_loader = new FXMLLoader();
            new_loader.setLocation(getClass().getResource("/com/example/lab7gui/signup.fxml"));


            AnchorPane root = new_loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Signup");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SignupController signupController = new_loader.getController();
            signupController.setService(this.service, dialogStage, this.stage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader new_loader = new FXMLLoader();
            new_loader.setLocation(getClass().getResource("/com/example/lab7gui/login.fxml"));


            AnchorPane root = new_loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log In");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LoginController loginController = new_loader.getController();
            loginController.setServiceLogin(this.service, dialogStage, this.stage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
