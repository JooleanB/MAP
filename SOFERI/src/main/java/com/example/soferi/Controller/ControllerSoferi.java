package com.example.soferi.Controller;

import com.example.soferi.Domain.Comanda;
import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Status;
import com.example.soferi.Service.ServiceDB;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.soferi.Domain.Sofer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerSoferi {
    public DatePicker datePicker = new DatePicker();
    private ServiceDB service;
    private Stage stage;
    private Sofer sofer;

    private int limit=100;
    private int offset=0;
    @FXML
    private ObservableList<Comanda> model_comenzi = FXCollections.observableArrayList();
    @FXML
    private TableView<Comanda> comandaTableView = new TableView<>();

    @FXML
    private TextField timp = new TextField("TIMP ASTEPTARE");
    @FXML
    private TextField nr_clienti_per_pagina = new TextField("NR CLIENTI PER PAGINA");
    @FXML
    private ObservableList<Persoana> model_persoane = FXCollections.observableArrayList();
    @FXML
    private TableView<Persoana> clienti = new TableView<>();

    @FXML
    private TableColumn<Persoana, String> nume = new TableColumn<>();

    @FXML
    private TableColumn<Persoana, Double> varsta = new TableColumn<>();

    @FXML
    private TableColumn<Comanda, String> nume_client = new TableColumn<>();

    @FXML
    private TableColumn<Comanda, String> locatie = new TableColumn<>();


    public void setService(ServiceDB serv, Stage stage,Sofer s){
        this.service=serv;
        this.service.add_sofer(this);
        this.stage=stage;
        this.sofer=s;
        this.stage.setOnCloseRequest(event -> {
            this.service.remove_sofer(this);
        });
        this.service.setSofer_RepoPersoane(this.sofer.getId());
        this.service.setLimit(100);
        this.service.setOffset(0);
        this.initModel();
    }

    @FXML
    public void initialize() {
        this.nume_client.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getPersoana().getNume()));
        this.locatie.setCellValueFactory(new PropertyValueFactory<>("locatie"));
        this.nume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        this.varsta.setCellValueFactory(new PropertyValueFactory<>("varsta"));
        this.comandaTableView.setItems(this.model_comenzi);
        this.clienti.setItems(this.model_persoane);
    }

    public void initModel() {
        initialize();
        Iterable<Comanda> allComenzi = this.service.getComenzi();
        List<Comanda> lista_comenzi = new ArrayList<>();
        Iterable<Persoana> clienti_paginati = this.service.getClientiPaginat();
        for(Comanda c:allComenzi){
            if(c.getStatus()!= Status.ACCEPTED && c.getStatus()!=Status.TAKEN){
                lista_comenzi.add(c);
            }
        }
        List<Persoana> lista_clienti= StreamSupport.stream(clienti_paginati.spliterator(), false)
                .collect(Collectors.toList());
        this.model_comenzi.setAll(lista_comenzi);
        this.model_persoane.setAll(lista_clienti);
    }


    @FXML
    public void onoreaza_comanda(){
        Comanda comanda = this.comandaTableView.getSelectionModel().getSelectedItem();
        String timp = this.timp.getText();
        if(comanda == null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST SELECT A RIDE REQUEST!");
            message.showAndWait();
        }

        else if(timp==null || timp.isEmpty()){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST INPUT WAIT TIME!");
            message.showAndWait();
        }

        else{
            this.service.modifica(comanda.getId(),this.sofer.getId(),timp);
            this.service.init_model_all_controllers();
        }
    }

    @FXML
    public void media_comenzilor(){
        Iterable<Comanda> comenzi = this.service.getComenzi();
        List<Comanda> lista = new ArrayList<>();
        LocalDateTime data = LocalDateTime.now();
        for (Comanda c : comenzi){
            if(Objects.equals(c.getTaximetrist().getId(), this.sofer.getId())  && c.getStatus()==Status.ACCEPTED){
                lista.add(c);
            }
        }

        //&& c.getData().isAfter(data.minusMonths(3))
        long daysBetween = ChronoUnit.DAYS.between(data.minusMonths(3),data);
        long nr_comenzi=lista.size();
        double media = (double)  nr_comenzi/daysBetween;
        Alert message= new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("MEDIA DE COMENZI PE ZI");
        message.setContentText("MEDIA DE COMENZI PE ZI ESTE: " + media);
        message.showAndWait();
    }

    @FXML
    public void client_fidel(){
        Iterable<Comanda> comenzi = this.service.getComenzi();

        Map<Persoana, Integer> clientAppearances = new HashMap<>();

        for (Comanda comanda : comenzi) {
            if (comanda.getTaximetrist().equals(this.sofer)) {
                Persoana client = comanda.getPersoana();
                clientAppearances.put(client, clientAppearances.getOrDefault(client, 0) + 1);
            }
        }

        Persoana mostFrequentClient = null;
        int maxAppearances = 0;

        for (Map.Entry<Persoana, Integer> entry : clientAppearances.entrySet()) {
            if (entry.getValue() > maxAppearances) {
                mostFrequentClient = entry.getKey();
                maxAppearances = entry.getValue();
            }
        }
        if(mostFrequentClient!=null) {
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("CEL MAI FIDEL CLIENT");
            message.setContentText("CEL MAI FIDEL CLIENT ESTE: " + mostFrequentClient);
            message.showAndWait();
        }
        else{
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("NU EXISTA NICIUN CLIENT!");
            message.showAndWait();
        }
    }

    public void paginare(){
        String nr_clienti= this.nr_clienti_per_pagina.getText();
        try{
            int x =Integer.parseInt(nr_clienti);
            if(x>0){
                this.limit=x;
                this.offset=0;
                this.service.setLimit(x);
                this.service.setOffset(0);
                this.initModel();
            }
        }
        catch(NumberFormatException eroare){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE");
            message.setContentText("TREBUIE INTRODUS UN NUMAR");
            message.showAndWait();
        }
    }


    public void next(){

        int max = this.service.get_count();
        System.out.println("MAXIMUL ESTE : " +max);
        if (this.offset + this.limit < max) {
            this.service.setOffset(this.offset + this.limit);
            this.offset=this.offset+this.limit;
            initModel();
        } else {
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE");
            message.setContentText("NU MAI SUNT CLIENTI");
            message.showAndWait();
        }
    }

    public void previous(){
        if (this.offset - this.limit >= 0) {
            int diferenta = this.offset- this.limit;
            System.out.println("NOUL OFFSET ESTE: " + diferenta);
            this.service.setOffset(this.offset - this.limit);
            this.offset=this.offset-this.limit;
            initModel();
        } else {
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE");
            message.setContentText("NU MAI SUNT CLIENTI");
            message.showAndWait();
        }
    }

    public void comenzi_date(){
        LocalDateTime data_de_verificat = this.datePicker.getValue().atStartOfDay();

        Iterable<Comanda> toate_comenzile = this.service.getComenzi();
        List<Comanda> comenzi = new ArrayList<>();
        for(Comanda c : toate_comenzile){
            if(Objects.equals(c.getTaximetrist().getId(), this.sofer.getId())){
                if(c.getData().getMonth()==data_de_verificat.getMonth() &&
                    c.getData().getYear()==data_de_verificat.getYear() &&
                    c.getData().getDayOfMonth()==data_de_verificat.getDayOfMonth()
                    ) {
                    comenzi.add(c);
                }
            }
        }

        Alert message= new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("LISTA COMENZILOR");
        StringBuilder builder = new StringBuilder();
        for(Comanda c : comenzi){
            builder.append(c.getId());
            builder.append(", ");
            builder.append(c.getData());
            builder.append("\n");
        }
        message.setContentText("LISTA COMENZILOR DIN DATA ALEASA ESTE: " + builder);
        message.showAndWait();
    }



}
