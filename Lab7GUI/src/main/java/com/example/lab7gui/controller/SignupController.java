package com.example.lab7gui.controller;

import com.example.lab7gui.domain.Utilizator;
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

public class SignupController {
    private ServiceDB service;

    private Stage stage;

    private Stage stage_start;

    @FXML
    private TextField firstName = new TextField("Firstname");


    @FXML private TextField lastName = new TextField("Lastname") ;

    @FXML
    private TextField email = new TextField("Email");
    @FXML
    private TextField password = new TextField("Password");

    public void setService(ServiceDB serv,Stage stage,Stage stage_start_controller){
        this.service=serv;
        this.stage=stage;
        this.stage_start = stage_start_controller;
    }

    @FXML
    public void signup(){
        String f,l,e,p;
        f=firstName.getText();
        l= lastName.getText();
        e=email.getText();
        p=password.getText();
        if(!Objects.equals(f, "") && !Objects.equals(l, "") && !Objects.equals(e, "") && !Objects.equals(p, ""))
        {
            Utilizator u = new Utilizator(f,l,e,p);
            this.service.adaugare_utilizator(u);
            try {
                // create a new stage for the popup dialog.
                FXMLLoader new_loader = new FXMLLoader();
                new_loader.setLocation(getClass().getResource("/com/example/lab7gui/connected.fxml"));


                AnchorPane root = new_loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Social Network");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                //dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                UserController userController = new_loader.getController();
                this.service.set_limit_offset(100,0);
                userController.setService(this.service, dialogStage,u);
                dialogStage.show();
                this.stage.close();
                this.stage_start.close();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        else{
            UserAlert.showErrorMessage(null,"Niciun field nu poate sa fie gol!");
        }
    }
}
