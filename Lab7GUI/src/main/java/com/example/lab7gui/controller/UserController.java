package com.example.lab7gui.controller;

import com.example.lab7gui.domain.FriendRequest;
import com.example.lab7gui.domain.Prietenie;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.service.ServiceDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.lab7gui.domain.FriendRequest.*;

public class UserController {
    private ServiceDB service;
    private Stage stage;
    private Utilizator user;

    @FXML
    public ObservableList<Utilizator> model_utilizator = FXCollections.observableArrayList();

    @FXML
    public ObservableList<Prietenie> model_friendships = FXCollections.observableArrayList();

    @FXML
    public ObservableList<Prietenie> model_friendrequests = FXCollections.observableArrayList();
    @FXML
    public TableView<Utilizator> userTableView = new TableView<>();

    @FXML
    public TableColumn<Utilizator, String> userFirstName = new TableColumn<>();
    @FXML
    public TableColumn<Utilizator, String> userLastName = new TableColumn<>();

    @FXML
    public TableColumn<Utilizator, String> userEmail = new TableColumn<>();

    @FXML
    public TableView<Prietenie> friendRequestsTableView = new TableView<>();
    @FXML
    public TableColumn<Prietenie, String> from = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, FriendRequest> request = new TableColumn<>();

    @FXML
    public TableView<Prietenie> friendshipTableView = new TableView<>();
    @FXML
    public TableColumn<Prietenie, String> friend_email = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, LocalDateTime> friendshipDate = new TableColumn<>();

    public void setService(ServiceDB serv,Stage stage,Utilizator u){
        this.service=serv;
        this.stage=stage;
        this.user=u;
        initModel();
    }

    @FXML
    public void initialize() {
        this.userFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.userLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.userTableView.setItems(this.model_utilizator);
        this.from.setCellValueFactory(new PropertyValueFactory<>("user1_email"));
        this.request.setCellValueFactory(new PropertyValueFactory<>("request"));
        this.friendRequestsTableView.setItems(this.model_friendrequests);
        this.friend_email.setCellValueFactory(new PropertyValueFactory<>("user1_email"));
        this.friendshipDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.friendshipTableView.setItems(this.model_friendships);
    }

    public void initModel() {
        initialize();
        Iterable<Utilizator> allUsers = service.getAllUtilizatori();
        List<Utilizator> utilizatori = new ArrayList<>();
        for(Utilizator u : allUsers){
            if(!Objects.equals(u.getEmail(), this.user.getEmail())){
                utilizatori.add(u);
            }
        }
        Iterable<Prietenie> allFriendships = service.getAllPrietenii();
        List<Prietenie> friendrequests = new ArrayList<>();
        List<Prietenie> friendships = new ArrayList<>();
        for(Prietenie p : allFriendships) {
            if (Objects.equals(p.getUser2().getEmail(), this.user.getEmail()) || Objects.equals(p.getUser1().getEmail(), this.user.getEmail())) {
                if (Objects.equals(p.getRequest(), "ACCEPTED"))
                    friendships.add(p);
            }
            if(Objects.equals(p.getUser2().getEmail(), this.user.getEmail())){
                if(!Objects.equals(p.getRequest(), "ACCEPTED"))
                    friendrequests.add(p);
            }

        }
        this.model_utilizator.setAll(utilizatori);
        this.model_friendships.setAll(friendships);
        this.model_friendrequests.setAll(friendrequests);
    }

    public void send_fr(){
        Utilizator selected = userTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            this.service.creazaPrietenie(this.user.getEmail(),selected.getEmail());
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT AN USER");
        }

    }

    public void delete_fr(){
        Prietenie selected = friendshipTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            this.service.stergePrietenie(selected.getUser1_email(), selected.getUser2_email());
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT A FRIENDSHIP");
        }
    }

    public void accept_fr(){
        Prietenie selected = friendRequestsTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            selected.set_request(ACCEPTED);
            this.service.modificaPrietenie(selected);
            System.out.println("Emailurile sunt : " + selected.getUser1_email() + " si " + selected.getUser2_email());
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT A FRIEND REQUEST");
        }

    }

    public void decline_fr(){
        Prietenie selected = friendRequestsTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            selected.set_request(REJECTED);
            this.service.modificaPrietenie(selected);
            this.service.declineFriendRequest(selected.getUser1_email(), selected.getUser2_email());
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT A FRIEND REQUEST");
        }
    }

}
