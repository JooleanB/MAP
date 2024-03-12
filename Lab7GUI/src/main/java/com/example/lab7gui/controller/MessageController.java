package com.example.lab7gui.controller;

import com.example.lab7gui.domain.Message;
import com.example.lab7gui.domain.Prietenie;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.service.ServiceDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageController extends Controller{
    @FXML
    ObservableList<Message> model_message = FXCollections.observableArrayList();

    @FXML
    public TableView<Message> messageTableView = new TableView<>();

    @FXML
    public TableColumn<Message, String> messageID = new TableColumn<>();
    @FXML
    public TableColumn<Message, String> messageFrom = new TableColumn<>();

    @FXML
    public TableColumn<Message, String> messageTo = new TableColumn<>();

    public TableColumn<Message, String> messageText = new TableColumn<>();
    @FXML
    public TableColumn<Message, String> messageDate = new TableColumn<>();

    @FXML
    public TableColumn<Message, String> messageReplies = new TableColumn<>();

    @FXML
    private TextField textFieldFrom = new TextField();
    @FXML
    private TextField textFieldTo = new TextField();
    @FXML
    private TextField textFieldText = new TextField();

    @FXML
    private TextField textFieldUser1 = new TextField();

    @FXML
    private TextField textFieldUser2 = new TextField();

    private ServiceDB service;

    private Controller controller;

    private Stage dialogStage = new Stage();

    public MessageController() {
    }

    public void setService(ServiceDB service, Stage stage,Controller controller) {
        this.service = service;
        this.dialogStage = stage;
        this.controller=controller;
        this.initModel();
    }

    @FXML
    public void initialize() {
        this.messageID.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.messageFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        this.messageTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        this.messageText.setCellValueFactory(new PropertyValueFactory<>("message"));
        this.messageDate.setCellValueFactory(new PropertyValueFactory<>("data"));
        this.messageReplies.setCellValueFactory(new PropertyValueFactory<>("replies"));
        this.messageTableView.setItems(this.model_message);
    }

    public void initModel() {
        initialize();
        Iterable<Message> allMessages = this.service.getAllMessages();
        List<Message> messageList = StreamSupport.stream(allMessages.spliterator(), false)
                .collect(Collectors.toList());
        this.model_message.setAll(messageList);
    }

    @FXML
    public void handleSave() {
        String from = textFieldFrom.getText();
        if(!verificare_email(from)) {
            UserAlert.showErrorMessage(null, "NO USER WITH THE EMAIL IN FROM WAS FOUND!");
            return;
        }
        String to = textFieldTo.getText();
        String[] to_string_list = to.split(",");
        List<String> to_list = new ArrayList<>();
        Collections.addAll(to_list, to_string_list);
        for(String s : to_list){
            if(!verificare_email(s)) {
                UserAlert.showErrorMessage(null, "AT LEAST AN USER WITH THE EMAIL IN TO WAS NOT FOUND!");
                return;
            }
        }
        String text = textFieldText.getText();
        if(Objects.equals(text, "") || Objects.equals(from, "") || Objects.equals(to,"")){
            UserAlert.showErrorMessage(null,"OBJECT IS EMPTY");
            return;
        }
        Message u = new Message (from, to_list, text);
        this.service.adaugare_mesaj(u);
        this.initModel();
        UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Messagge saved", "The message has been saved");
    }


    public boolean verificare_email(String email){
        Iterable<Utilizator> utilizators = this.service.getAllUtilizatori();
        for(Utilizator u : utilizators){
            if(Objects.equals(u.getEmail(), email)){
                return true;
            }
        }
        return false;
    }
    @FXML
    public void handleSaveReply() {
        Message selected = messageTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String email_from = textFieldFrom.getText();
            if (!selected.getTo().contains(email_from)){
                UserAlert.showErrorMessage(null, "YOU CAN'T REPLY TO A MESSAGE YOU DID NOT RECEIVE!");
                return;
            }
            if(!verificare_email(email_from)) {
                UserAlert.showErrorMessage(null, "NO USER WITH THE EMAIL IN FROM WAS FOUND!");
                return;
            }
            String to = textFieldTo.getText();
            String[] to_string_list = to.split(",");
            List<String> to_list = new ArrayList<>();
            Collections.addAll(to_list, to_string_list);
            for(String s : to_list){
                if(!verificare_email(s)) {
                    UserAlert.showErrorMessage(null, "AT LEAST AN USER WITH THE EMAIL IN TO WAS NOT FOUND!");
                    return;
                }
            }
            String text = textFieldText.getText();
            if(Objects.equals(text, "") || Objects.equals(to,"") || Objects.equals(email_from,"")){
                UserAlert.showErrorMessage(null,"OBJECTS IS EMPTY");
                return;
            }
            Message u = new Message (email_from, to_list, text);
            this.service.adaugare_mesaj(u);
            this.service.modifica_mesaj(selected,u);
            this.initModel();
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Reply saved", "The reply has been saved");

        } else UserAlert.showErrorMessage(null, "You must select a message to reply to!");
    }

    public void handlePreviousMessages(){
        int o = this.service.get_offset_messages();
        int l = this.service.get_limit();
        if(o-l>=0){
            this.service.set_offset_messages(o-l);
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null, "There are no previous messages!");
        }
    }

    public void handleNextMessages(){
        int o = this.service.get_offset_messages();
        int l = this.service.get_limit();
        int max = this.service.get_number_of_messages();
        if(o+l<max){
            this.service.set_offset_messages(o+l);
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null, "There are no more messages!");
        }
    }


    @FXML
    public void showMessagesUsers() {
        initialize();
        String email_user1 = textFieldUser1.getText();
        if(!verificare_email(email_user1)) {
            UserAlert.showErrorMessage(null, "USER1 WAS NOT FOUND!");
            return;
        }
        String email_user2 = textFieldUser2.getText();
        if(!verificare_email(email_user2)) {
            UserAlert.showErrorMessage(null, "USER2 WAS NOT FOUND!");
            return;
        }
        Iterable<Message> allMessages = service.getAllMessages();
        List<Message> messageList = new ArrayList<>();
        for(Message m : allMessages){
            if(Objects.equals(m.getFrom(), email_user1)){
                List<String> lista_id = m.getTo();
                if(lista_id.contains(email_user2)){
                    messageList.add(m);
                }
            }
            else if(Objects.equals(m.getFrom(), email_user2)){
                List<String > lista_id = m.getTo();
                if(lista_id.contains(email_user1)){
                    messageList.add(m);
                }
            }
        }
        this.model_message.setAll(messageList);
        UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "DONE!", "MESSAGE TABLE HAS BEEN UPDATED");
    }

    @FXML
    public void showMessages(){
        this.initModel();
        UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "DONE!", "MESSAGE TABLE HAS BEEN UPDATED");
    }

    @FXML
    public void populate_reply(){
        Message selected = messageTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            this.textFieldFrom.setText(selected.getFrom());
            this.textFieldTo.setText(selected.getTo().toString());
            this.textFieldText.setText(selected.getMessage());
        }
    }

}
