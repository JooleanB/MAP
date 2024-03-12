package com.example.lab7gui.controller;


//import com.example.lab7gui.Utils.Events.ChangeEventType;
//import com.example.lab7gui.Utils.Events.UserChangeEvent;
import com.example.lab7gui.domain.FriendRequest;
import com.example.lab7gui.domain.Prietenie;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.validators.ValidationException;
import com.example.lab7gui.service.ServiceDB;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;

public class EditFriendshipController extends Controller{
    @FXML
    private TextField textFieldEmail1;
    @FXML
    private TextField textFieldEmail2;

    private ServiceDB service;
    Stage dialogStage;

    Controller controller;

    Prietenie p;


    public void setService(ServiceDB service, Stage stage,Controller controller,Prietenie p) {
        this.service = service;
        this.dialogStage = stage;
        this.controller=controller;
        this.p=p;
        if(this.p!=null){
            textFieldEmail1.setText(p.getUser1_email());
            textFieldEmail2.setText(p.getUser2_email());
        }
    }

    @FXML
    public void setRequestACCEPTED(){
        p.set_request(FriendRequest.ACCEPTED);
        service.modificaPrietenie(p);
        this.controller.initModel();
    }

    @FXML
    public void setRequestREJECTED(){
        p.set_request(FriendRequest.REJECTED);
        service.modificaPrietenie(p);
        this.controller.initModel();
    }

    @FXML
    public void handleSave() {
        String email1 = textFieldEmail1.getText();
        String email2 = textFieldEmail2.getText();
        savePrietenie(email1,email2);
        controller.initModel();
    }


    private void savePrietenie(String email1,String email2 ) {
        try {
            if (email1 == null || email2==null)  {
                dialogStage.close();
            }
            this.service.creazaPrietenie(email1,email2);
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friendship saved", "The Friendship has been saved");

        } catch (RuntimeException e) {
            UserAlert.showErrorMessage(null,"OBJECT IS EMPTY");
        }
        dialogStage.close();
    }



    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}

