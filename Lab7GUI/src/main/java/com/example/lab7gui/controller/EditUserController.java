package com.example.lab7gui.controller;



//
//import com.example.lab7gui.Utils.Events.ChangeEventType;
//import com.example.lab7gui.Utils.Events.UserChangeEvent;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.validators.ValidationException;
import com.example.lab7gui.service.ServiceDB;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class EditUserController extends Controller{
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;

    private ServiceDB service;
    Stage dialogStage;
    Utilizator user;

    Controller controller;



    public void setService(ServiceDB service, Stage stage, Utilizator u,Controller controller) {
        this.service = service;
        this.dialogStage = stage;
        this.user = u;
        this.controller=controller;
//        if (null != u) {
//            setFields(u);
//            textFieldEmail.setEditable(false);
//        }
    }

    @FXML
    public void handleSave() {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();
        if(Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(email, "")){
            UserAlert.showErrorMessage(null,"OBJECT IS EMPTY");
            return;
        }
        Utilizator u = new Utilizator (firstName, lastName, email, password);
        if (null == this.user)
            saveUser(u);
        controller.initModel();
    }

    @FXML
    public void handleUpdate(){
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        if(Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(email, "")){
            UserAlert.showErrorMessage(null,"OBJECT IS EMPTY");
            return;
        }
        updateUser(user,lastName,firstName,email);
        controller.initModel();
    }

    @FXML
    private void updateUser(Utilizator u,String n,String l, String e) {
        try {
            this.service.modifica_utilizator(u,n,l,e);
            if (u == null)  {
                dialogStage.close();
            }
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "User updated", "The user has been updated");

        } catch (RuntimeException el) {
            UserAlert.showErrorMessage(null,"OBJECT IS EMPTY");
        }
        dialogStage.close();
    }

    private void saveUser(Utilizator u) {
        try {
            this.service.adaugare_utilizator(u);
            if (u == null)  {
                dialogStage.close();
            }
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "User saved", "The user has been saved");

        } catch (RuntimeException e) {
            UserAlert.showErrorMessage(null,"OBJECT IS EMPTY");
        }
        dialogStage.close();
    }

    private void clearFields() {
        textFieldEmail.clear();
        textFieldLastName.clear();
        textFieldFirstName.clear();
    }
    private void setFields(Utilizator u) {
        textFieldFirstName.setText(u.getFirstName());
        textFieldLastName.setText(u.getLastName());
        textFieldEmail.setText(u.getEmail());
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
