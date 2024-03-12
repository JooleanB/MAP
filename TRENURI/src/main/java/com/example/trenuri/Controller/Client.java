package com.example.trenuri.Controller;

import com.example.trenuri.Domain.Cities;
import com.example.trenuri.Domain.Ticket;
import com.example.trenuri.Domain.TrainStation;
import com.example.trenuri.Service.Service;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private Service service;
    private ObservableList<String> citiesList = FXCollections.observableArrayList();

    private ObservableList<Integer> train_id_observable_list = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> departureCity = new ComboBox<>();
    @FXML
    private ComboBox<String> destinationCity = new ComboBox<>();

    @FXML
    private DatePicker datePicker = new DatePicker();

    @FXML
    private ComboBox<Integer> trainID_combo_box = new ComboBox<>();

    @FXML
    public ObservableList<Ticket> model_ticket= FXCollections.observableArrayList();
    @FXML
    public TableView<Ticket> ticketTableView = new TableView<>();

    @FXML
    public TableColumn<Ticket, String> city_name_column = new TableColumn<>();


    @FXML
    public TableColumn<Ticket, Integer> trainID_column = new TableColumn<>();

    @FXML
    public TableColumn<Ticket, Integer> no_tickets_column = new TableColumn<>();


    @FXML
    private CheckBox checkBox = new CheckBox("Direct Routes Only");

    @FXML
    private Label rute = new Label("RUTE");

    @FXML
    private Label nr_clienti_aceeasi_ruta=new Label("N OTHER USERS ARE LOOKING AT THE SAME ROUTE");

    public void setService(Service service) {
        this.service = service;
        this.service.add_client(this);
        initModel();
    }

    public String getDepartureCity() {
        return departureCity.getSelectionModel().getSelectedItem();
    }

    public void set_number_of_clients_looking_for_the_same_route() {
        if (this.departureCity.getSelectionModel().getSelectedItem() != null && this.destinationCity.getSelectionModel().getSelectedItem() != null) {
            int nr = this.service.get_count_clients(this.departureCity.getSelectionModel().getSelectedItem(), this.destinationCity.getSelectionModel().getSelectedItem());
            if(nr>1)
                this.nr_clienti_aceeasi_ruta.setText(nr-1 + " OTHER CLIENTS ARE LOOKING AT THE SAME ROUTE");
        }
    }

    public String getDestinationCity() {
        return destinationCity.getSelectionModel().getSelectedItem();
    }


    public void initModel() {
        for (Cities city : this.service.getCities()) {
            this.citiesList.add(city.getName());
        }
        Set<Integer> set_id_tren = new HashSet<>();
        for(TrainStation t : this.service.getTrainStations()){
            set_id_tren.add(t.getTrainID());
        }
        this.train_id_observable_list.setAll(set_id_tren);
        this.trainID_combo_box.setItems(this.train_id_observable_list);
        this.departureCity.setItems(this.citiesList);
        this.destinationCity.setItems(this.citiesList);
        departureCity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            set_number_of_clients_looking_for_the_same_route();
            this.service.change_label_clients();
        });
        destinationCity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            set_number_of_clients_looking_for_the_same_route();
            this.service.change_label_clients();
        });
    }



    @FXML
    private void show() {
        String departure = this.departureCity.getSelectionModel().getSelectedItem();
        String destination = this.destinationCity.getSelectionModel().getSelectedItem();
        List<String> routes = findRoutes(departure, destination);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher;
        //String routesString = String.join("\n", routes);
        List<String> rute_directe = new ArrayList<>();
        for(String ruta : routes){
            Set<String> uniqueNumbers = new HashSet<>();
            matcher = pattern.matcher(ruta);
            while (matcher.find()) {
                uniqueNumbers.add(matcher.group());
            }
            if(uniqueNumbers.size()==1){
                rute_directe.add(ruta);
            }
        }
        if(this.checkBox.isSelected()){
            String routesString = String.join("\n", rute_directe);
            if(routesString.isEmpty()){
                this.rute.setText("NO ROUTES FOUND!");
            }
            else{
                this.rute.setText(routesString);
            }
        }
        else{
            String routesString = String.join("\n", routes);
            if(routesString.isEmpty()){
                this.rute.setText("NO ROUTES FOUND!");
            }
            else{
                this.rute.setText(routesString);
            }
        }
    }

    private List<String> findRoutes(String departure, String destination) {
        List<String> routes = new ArrayList<>();
        backtrack(new ArrayList<>(), departure, destination, routes);
        return routes;
    }

    private void backtrack(List<String> path, String currentCity, String destination, List<String> routes) {
        if (currentCity.equals(destination)) {
            path.add(destination);
            String route = pathToString(path);
            routes.add(route);
            return;
        }

        Iterable<TrainStation> nextStations = getNextStations(currentCity);
        for (TrainStation station : nextStations) {
            Cities dep = this.service.get_city_by_id(station.getDepartureCityID());
            Cities dest = this.service.get_city_by_id(station.getDestinationCityID());
            path.add(dep.getName() + " - " + station.getTrainID());
            backtrack(path, dest.getName(), destination, routes);
            path.remove(path.size() - 1);
        }
    }


    private String pathToString(List<String> path) {
        return String.join(" -> ", path);
    }

    private Iterable<TrainStation> getNextStations(String city) {
        Iterable <TrainStation> statii = this.service.getTrainStations();
        List<TrainStation> lista_statii = new ArrayList<>();
        for(TrainStation s : statii){
            Cities oras = this.service.get_city_by_id(s.getDepartureCityID());
            if(Objects.equals(oras.getName(), city)){
                lista_statii.add(s);
            }
        }
        return lista_statii;
    }



    @FXML
    public void buy_ticket(){
        Integer trainID = this.trainID_combo_box.getSelectionModel().getSelectedItem();
        if(trainID==null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("YOU MUST SELECT A TRAIN ID");
            message.showAndWait();
            return;
        }
        LocalDate data = this.datePicker.getValue();
        if(data==null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("YOU MUST SELECT A DATA!");
            message.showAndWait();
            return;
        }
        LocalDateTime data_bilet = data.atStartOfDay();
        String city_name = this.departureCity.getSelectionModel().getSelectedItem();
        if(city_name==null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("YOU MUST SELECT A CITY!");
            message.showAndWait();
            return;
        }
        Iterable<Cities> lista_orase = this.service.getCities();
        int id_oras = 0;
        for(Cities c :lista_orase){
            if(Objects.equals(c.getName(), city_name)){
                id_oras=c.getId();
            }
        }
        Ticket t = new Ticket(trainID,id_oras,data_bilet);
        Iterable<TrainStation> statii = this.service.getTrainStations();
        boolean ok = false;
        for(TrainStation station : statii){
            if(station.getTrainID()==trainID && station.getDepartureCityID()==id_oras){
               ok = true;
            }
        }


        if(ok)
            this.service.save(t);
        else{
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("THE TRAIN SELECTED DOESN'T HAVE A STATION IN THIS CITY");
            message.showAndWait();
            return;
        }

        Iterable<Ticket> lista_bilete = this.service.getTicket();
        Set<Ticket> lista_bilete_tabel = new HashSet<>();
        for(Ticket bilett: lista_bilete){
            int id_oras_bilet = bilett.getDepartureCityID();
            LocalDateTime data_bilet_lista = bilett.getData();
            if(id_oras_bilet==id_oras && data_bilet_lista.getMonth()==data_bilet.getMonth() &&
                data_bilet_lista.getYear()==data_bilet.getYear() && data_bilet_lista.getDayOfMonth()== data_bilet.getDayOfMonth()
                && data_bilet_lista.getDayOfYear() == data_bilet.getDayOfYear()){
                lista_bilete_tabel.add(t);
            }
        }
        this.trainID_column.setCellValueFactory(new PropertyValueFactory<>("traindID"));
        this.city_name_column.setCellValueFactory(cellData -> {
            Ticket ticket = cellData.getValue();
            int cityId = ticket.getDepartureCityID(); // This gets the city ID from the ticket
            Cities c = this.service.get_city_by_id(cityId);
            String cityName = c.getName();
            return new ReadOnlyStringWrapper(cityName);
        });
        this.no_tickets_column.setCellValueFactory(cellData -> {
            Ticket ticket = cellData.getValue();
            int cityId = ticket.getDepartureCityID();
            int iddd_tren = ticket.getTraindID();
            Iterable<Ticket> lista_bilete_pentru_coloana = this.service.getTicket();
            int count =0;
            for(Ticket bilets : lista_bilete_pentru_coloana){
                if(bilets.getTraindID()==ticket.getTraindID() &&
                    bilets.getDepartureCityID()==ticket.getDepartureCityID()&&
                    bilets.getData().getDayOfYear() == ticket.getData().getDayOfYear()
                    && bilets.getData().getYear()== ticket.getData().getYear()){
                    count++;
                }
            }
            int count_ticket = this.service.get_number_of_tickets(iddd_tren,cityId);
            return new ReadOnlyObjectWrapper<>(count);
        });
        this.model_ticket.setAll(lista_bilete_tabel);
        this.ticketTableView.setItems(this.model_ticket);

    }


    @FXML
    public void most_tickets_sold(){
        Iterable<Ticket> lista_bilete = this.service.getTicket();
        int max=0;
        Ticket bilet = null;
        for(Ticket t : lista_bilete){
            int nr_bilete = this.service.get_number_of_tickets(t.getTraindID(),t.getDepartureCityID());
            if(nr_bilete>max){
                max=nr_bilete;
                bilet=t;
            }
        }
        Alert message= new Alert(Alert.AlertType.INFORMATION);
        message.setTitle("TRENUL SI DATA");
        message.setContentText("TRAIN ID : " + bilet.getTraindID() + " \nDATE : " + bilet.getData());
        message.showAndWait();
    }


}
