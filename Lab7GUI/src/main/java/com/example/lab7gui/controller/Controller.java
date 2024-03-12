package com.example.lab7gui.controller;

import com.example.lab7gui.domain.FriendRequest;
import com.example.lab7gui.domain.Prietenie;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.service.ServiceDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class Controller {

    @FXML
    private TextField number_of_entities;
    @FXML
    public ObservableList<Utilizator> model_utilizator = FXCollections.observableArrayList();

    @FXML
    public ObservableList<Prietenie> model_friendships = FXCollections.observableArrayList();
    @FXML
    public TableView<Utilizator> userTableView = new TableView<>();

    @FXML
    public TableColumn<Utilizator, String> userFirstName = new TableColumn<>();
    @FXML
    public TableColumn<Utilizator, String> userLastName = new TableColumn<>();

    @FXML
    public TextField textSearchedEmail = new TextField();

    @FXML
    public TableColumn<Utilizator, String> userEmail = new TableColumn<>();

    @FXML
    public TableColumn<Utilizator, String> userPassword = new TableColumn<>();
    @FXML
    public TableView<Prietenie> friendshipTableView = new TableView<>();
    @FXML
    public TableColumn<Prietenie, String> user1_email = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, String> user2_email = new TableColumn<>();
    @FXML
    public TableColumn<Prietenie, LocalDateTime> friendshipDate = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, FriendRequest> request = new TableColumn<>();

    private ServiceDB service;


    public void setService(ServiceDB service) {
        this.service = service;
        initModel();
    }

    @FXML
    public void initialize() {
        this.userFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.userLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.userPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        this.userTableView.setItems(this.model_utilizator);
        this.user1_email.setCellValueFactory(new PropertyValueFactory<>("user1_email"));
        this.user2_email.setCellValueFactory(new PropertyValueFactory<>("user2_email"));
        this.friendshipDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.request.setCellValueFactory(new PropertyValueFactory<>("request"));
        this.friendshipTableView.setItems(this.model_friendships);
    }

    public void initModel() {
        initialize();
        Iterable<Utilizator> allUsers = service.getUtilizatoriLimitOffset();
        Iterable<Prietenie> allFriendships = service.getAllPrietenii();

        List<Utilizator> usersList = StreamSupport.stream(allUsers.spliterator(), false)
                .collect(Collectors.toList());
        List<Prietenie> friendshipsList = StreamSupport.stream(allFriendships.spliterator(), false)
                .collect(Collectors.toList());
        this.model_friendships.setAll(friendshipsList);
        this.model_utilizator.setAll(usersList);
    }


    public void handlePreviousUsers() {
        int o = this.service.get_offset_users();
        int l = this.service.get_limit();
        if (o - l >= 0) {
            this.service.set_offset_users(o - l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no previous users!");
        }
    }

    public void handlePreviousFriendships() {
        int o = this.service.get_offset_friendships();
        int l = this.service.get_limit();
        if (o - l >= 0) {
            this.service.set_offset_friendships(o - l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no previous friendships!");
        }
    }

    public void handleNextUsers() {
        int o = this.service.get_offset_users();
        int l = this.service.get_limit();
        int max = this.service.get_number_of_users();
        if (o + l < max) {
            this.service.set_offset_users(o + l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no more users!");
        }
    }

    public void handleNextFriendships() {
        int o = this.service.get_offset_friendships();
        int l = this.service.get_limit();
        int max = this.service.get_number_of_friendships();
        if (o + l < max) {
            this.service.set_offset_friendships(o + l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no more friendships!");
        }
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        Utilizator selected = userTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.sterge_utilizator(selected.getEmail());
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "User deleted successfully!");
            initModel();
        } else UserAlert.showErrorMessage(null, "You must select an user!");
    }

    @FXML
    public void handleUpdateUser(ActionEvent actionEvent) {
        Utilizator selected = userTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showUserEditDialog(selected);
        } else UserAlert.showErrorMessage(null, "You must select an user!");
    }

    public void handleRequestFriendship(ActionEvent actionEvent) {
        Prietenie selected = friendshipTableView.getSelectionModel().getSelectedItem();
        showFriendshipEdit(selected);
    }

    public void handleDeleteFriendship(ActionEvent actionEvent) {
        Prietenie selected = friendshipTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.stergePrietenie(selected.getUser1_email(), selected.getUser2_email());
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Friendship deleted successfully!");
            initModel();
        } else UserAlert.showErrorMessage(null, "You must select a friendship!");
    }

    public void handleAddUser(ActionEvent actionEvent) {
        showUserEditDialog(null);
    }


    public void handleAddFriendship() {
        showFriendshipEdit(null);
    }


    public void showFriendshipEdit(Prietenie p) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader new_loader = new FXMLLoader();
            new_loader.setLocation(getClass().getResource("/com/example/lab7gui/edit-friendship.fxml"));


            AnchorPane root = new_loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Friendship");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditFriendshipController editMessageViewController = new_loader.getController();
            editMessageViewController.setService(this.service, dialogStage, this, p);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSearchUser(ActionEvent actionEvent) {
        Utilizator searchedUser = service.getUtilizatorEmail(textSearchedEmail.getText());
        if (searchedUser == null) {
            UserAlert.showErrorMessage(null, "User doesn't exist!");
        } else {
            showUserEditDialog(searchedUser);
        }
    }


    public void showUserEditDialog(Utilizator user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader new_loader = new FXMLLoader();
            new_loader.setLocation(getClass().getResource("/com/example/lab7gui/edit-user.fxml"));


            AnchorPane root = new_loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editMessageViewController = new_loader.getController();
            editMessageViewController.setService(this.service, dialogStage, user, this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMessages(ActionEvent actionEvent) {
        showMessageEditDialog();
    }

    public void showMessageEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader new_loader = new FXMLLoader();
            new_loader.setLocation(getClass().getResource("/com/example/lab7gui/messages.fxml"));


            AnchorPane root = new_loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Messages");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MessageController editMessageViewController = new_loader.getController();
            editMessageViewController.setService(this.service, dialogStage, this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showController() {
        String n = this.number_of_entities.getText();
        try {
            int x = Integer.parseInt(n);
            if (x > 0) {
                this.service.set_limit_offset(x, 0);
                initModel();
            }
        } catch (NumberFormatException eroare) {
            UserAlert.showErrorMessage(null, "YOU NEED TO SELECT A NUMBER OF USERS ON PAGE");
        }
    }
}

