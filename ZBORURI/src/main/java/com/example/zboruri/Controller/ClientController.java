package com.example.zboruri.Controller;

import com.example.zboruri.Domain.Client;
import com.example.zboruri.Domain.Flight;
import com.example.zboruri.Domain.Ticket;
import com.example.zboruri.Service.Service;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.random.*;

public class ClientController {

    private Service service;
    private Client client;


    @FXML
    private ComboBox<String> from_combo_box = new ComboBox<>();
    @FXML
    private ComboBox<String> to_combo_box = new ComboBox<>();


    @FXML
    private ObservableList<String> model_to_combo = FXCollections.observableArrayList();

    @FXML
    private ObservableList<String> model_from_combo = FXCollections.observableArrayList();

    @FXML
    private DatePicker datePicker = new DatePicker();

    @FXML
    private ObservableList<Ticket> model_bilete_cumparate = FXCollections.observableArrayList();
    @FXML
    private TableView<Ticket> tabel_bilete_cumparate = new TableView<>();


    @FXML
    private ObservableList<Flight> model_zboruri = FXCollections.observableArrayList();
    @FXML
    private TableView<Flight> tabel_zboruri = new TableView<>();


    @FXML
    private TableColumn<Ticket, String> from = new TableColumn<>();
    @FXML
    private TableColumn<Flight, String> to = new TableColumn<>();


    @FXML
    private TableColumn<Flight, LocalDateTime> departureTime = new TableColumn<>();

    @FXML
    private TableColumn<Flight, LocalDateTime> landingTime = new TableColumn<>();


    @FXML
    private ObservableList<Ticket> model_bilete_data = FXCollections.observableArrayList();
    @FXML
    private TableView<Ticket> tabel_bilete_data = new TableView<>();


    @FXML
    private TableColumn<Ticket, Long> flight_id_bilete_cumparate = new TableColumn<>();

    @FXML
    private TableColumn<Ticket, Long> flight_id_bilete_data = new TableColumn<>();

    @FXML
    private TableColumn<Ticket, String> username_bilete_cumparate = new TableColumn<>();

    @FXML
    private TableColumn<Ticket, String> username_bilete_data = new TableColumn<>();

    @FXML
    private TableColumn<Ticket, LocalDateTime> purchase_time_cumparate = new TableColumn<>();

    @FXML
    private TableColumn<Ticket, LocalDateTime> purchase_time_data = new TableColumn<>();

    public void setService(Service service, Client c) {
        this.service=service;
        this.client=c;
        this.service.add_controller(this);
        initModel();
    }

    @FXML
    public void initialize() {
        this.username_bilete_cumparate.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.username_bilete_data.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.flight_id_bilete_cumparate.setCellValueFactory(new PropertyValueFactory<>("flightID"));
        this.flight_id_bilete_data.setCellValueFactory(new PropertyValueFactory<>("flightID"));
        this.purchase_time_cumparate.setCellValueFactory(new PropertyValueFactory<>("purchaseTime"));
        this.purchase_time_data.setCellValueFactory(new PropertyValueFactory<>("purchaseTime"));
        this.tabel_bilete_cumparate.setItems(this.model_bilete_cumparate);
        this.tabel_bilete_data.setItems(this.model_bilete_data);
    }

    public void initModel() {
        initialize();
        Set<String> uniqueFrom = new HashSet<>();
        Set<String> uniqueTo = new HashSet<>();

        for (Flight f : this.service.findAll_flight()) {
            uniqueFrom.add(f.getFrom());
            uniqueTo.add(f.getTo());
        }

        this.model_from_combo.addAll(uniqueFrom);
        this.model_to_combo.addAll(uniqueTo);


        this.from_combo_box.setItems(model_from_combo);
        this.to_combo_box.setItems(model_to_combo);

        Iterable<Ticket> allTicket = this.service.findAll_ticket();
        List<Ticket> lista_bilete_cumparate = new ArrayList<>();
        List<Ticket> lista_bilete_data = new ArrayList<>();
        for(Ticket t :allTicket){
            String username = t.getUsername();
            LocalDateTime data = t.getPurchaseTime();
            if(Objects.equals(username, this.client.getUsername())){
                lista_bilete_cumparate.add(t);
            }
            if(data.getMonth()== Month.JANUARY && data.getDayOfMonth()==24){
                lista_bilete_data.add(t);
            }
        }
        this.model_bilete_cumparate.setAll(lista_bilete_cumparate);
        this.model_bilete_data.setAll(lista_bilete_data);
    }

    public void find_flight(){
        Iterable<Flight> allFlights = this.service.findAll_flight();
        String flight_to = this.to_combo_box.getSelectionModel().getSelectedItem();
        String flight_from = this.from_combo_box.getSelectionModel().getSelectedItem();
        List<Flight> lista_tabel = new ArrayList<>();
        LocalDateTime data = this.datePicker.getValue().atStartOfDay();
        for(Flight f: allFlights){
            if(Objects.equals(f.getTo(), flight_to) && Objects.equals(f.getFrom(),flight_from)){
                LocalDateTime data_zbor = f.getDepartureTime();
                if(data_zbor.getMonth()==data.getMonth() && data_zbor.getDayOfMonth()==data.getDayOfMonth()){
                    lista_tabel.add(f);
                }
            }
        }
        this.to.setCellValueFactory(new PropertyValueFactory<>("to"));
        this.from.setCellValueFactory(new PropertyValueFactory<>("from"));
        this.departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        this.landingTime.setCellValueFactory(new PropertyValueFactory<>("landingTime"));
        this.model_zboruri.setAll(lista_tabel);
        this.tabel_zboruri.setItems(this.model_zboruri);
        initModel();
    }

    public void buy_ticket(){
        Flight f = this.tabel_zboruri.getSelectionModel().getSelectedItem();
        if(f==null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST SELECT A FLIGHT!");
            message.showAndWait();
        }
        else{
            Random r = new Random();
            Long id = r.nextLong();
            Ticket t = new Ticket(id,this.client.getUsername(),f.getId(),LocalDateTime.now());
            this.service.save(t);
            initModel();
        }
    }

}
