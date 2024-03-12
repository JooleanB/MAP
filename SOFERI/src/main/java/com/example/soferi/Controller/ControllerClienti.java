package com.example.soferi.Controller;

import com.example.soferi.Domain.Comanda;
import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Sofer;
import com.example.soferi.Domain.Status;
import com.example.soferi.Service.ServiceDB;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ControllerClienti {

    private ServiceDB service;

    private Stage stage;
    private Persoana persoana;

    @FXML
    private TextField adresa = new TextField("Adresa");

    @FXML
    public ObservableList<Comanda> model_comenzi = FXCollections.observableArrayList();
    @FXML
    public TableView<Comanda> comandaTableView = new TableView<>();

    @FXML
    public TableColumn<Comanda, String> indicativ = new TableColumn<>();


    @FXML
    public TableColumn<Comanda, String> timp_asteptare = new TableColumn<>();




    public void setService(ServiceDB serv, Stage stage, Persoana p){
        this.service=serv;
        this.service.add_client(this);
        this.stage=stage;
        this.persoana=p;
        this.stage.setOnCloseRequest(event -> {
            this.service.remove_client(this);
        });
        this.initModel();
    }


    @FXML
    public void initialize() {
        this.indicativ.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTaximetrist().getIndicativMasina()));
        this.timp_asteptare.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.comandaTableView.setItems(this.model_comenzi);
    }

    public void initModel() {
        initialize();
        Iterable<Comanda> allComenzi = this.service.getComenzi();
        List<Comanda> lista_comenzi = new ArrayList<>();
        for(Comanda c:allComenzi){
            if(Objects.equals(c.getPersoana().getId(), this.persoana.getId()) && c.getStatus()==Status.TAKEN && c.getTaximetrist()!=null){
                lista_comenzi.add(c);
            }
        }
        this.model_comenzi.setAll(lista_comenzi);
    }



    @FXML
    public void cauta(){
        String adresa = this.adresa.getText();
        if(adresa==null || adresa.isEmpty()){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST INPUT AN ADDRESS!");
            message.showAndWait();
        }
        Random random = new Random();
        Long id = random.nextLong();
        Comanda comanda = new Comanda(id,this.persoana, LocalDateTime.now());
        comanda.setLocatie(adresa);
        this.service.adaugaComanda(comanda);
        this.service.init_model_all_controllers();
    }

    @FXML
    public void cancel(){
        Comanda comanda = this.comandaTableView.getSelectionModel().getSelectedItem();
        if(comanda == null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST SELECT A RIDE REQUEST!");
            message.showAndWait();
        }
        else{
            this.service.cancel(comanda.getId());
            this.service.init_model_all_controllers();
        }
    }

    @FXML
    public void accept(){
        Comanda comanda = this.comandaTableView.getSelectionModel().getSelectedItem();
        if(comanda == null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("EROARE!");
            message.setContentText("YOU MUST SELECT A RIDE REQUEST!");
            message.showAndWait();
        }
        else{
            this.service.adaugaComandaBD(comanda.getId());
            this.service.init_model_all_controllers();
        }
    }



}
