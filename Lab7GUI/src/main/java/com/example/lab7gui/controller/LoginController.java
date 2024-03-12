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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    private ServiceDB service;

    private Stage stage;

    private Stage stage_start;

    @FXML
    private TextField email = new TextField("Email");
    @FXML
    private TextField password = new TextField("Password");

    public void setServiceLogin(ServiceDB serv,Stage stage,Stage stage_start_controller){
        this.service=serv;
        this.stage=stage;
        this.stage_start = stage_start_controller;
    }

    @FXML
    public void login(){
        String e=email.getText();
        String p=password.getText();
        if(!Objects.equals(e, "") && !Objects.equals(p, ""))
        {
            Iterable<Utilizator> utilizatori = this.service.getAllUtilizatori();
            try {
                byte[] secretKeyBytes = "CrazyNebunTarePePumn2003".getBytes();
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                byte[] dataToEncrypt = p.getBytes();
                byte[] encryptedData = cipher.doFinal(dataToEncrypt);
                String encrypted_password = Arrays.toString(encryptedData);
                boolean x = false;
                for(Utilizator u : utilizatori){
                    if(Objects.equals(u.getEmail(), e) && Objects.equals(u.getPassword(), encrypted_password)){
                        try {
                            x=true;
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
                            this.service.set_limit_offset(100,0);
                            UserController userController = new_loader.getController();
                            userController.setService(this.service, dialogStage,u);
                            dialogStage.show();
                            this.stage.close();
                            this.stage_start.close();

                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        break;
                    }
                }
                if(!x)
                    UserAlert.showErrorMessage(null,"Email si/sau parola gresite!");
            }
            catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
