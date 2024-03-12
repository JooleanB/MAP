package com.example.restaurant.Controller;

import com.example.restaurant.Domain.MenuItem;
import com.example.restaurant.Domain.Order;
import com.example.restaurant.Domain.OrderStatus;
import com.example.restaurant.Service.ServiceDB;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffController {

    private ServiceDB service;

    @FXML
    private ObservableList<Order> model_orders = FXCollections.observableArrayList();
    @FXML
    private TableView<Order> orderTableView = new TableView<>();

    @FXML
    private ObservableList<Order> model_orders_preparing = FXCollections.observableArrayList();
    @FXML
    private TableView<Order> orderTableView_preparing = new TableView<>();

    @FXML
    private TableColumn<Order, Integer>  id_table= new TableColumn<>();

    @FXML
    private TableColumn<Order, LocalDateTime> data = new TableColumn<>();

    @FXML
    private TableColumn<Order,String> nume_iteme = new TableColumn<>();


    @FXML
    private TableColumn<Order, Integer>  id_table_preparing= new TableColumn<>();

    @FXML
    private TableColumn<Order, LocalDateTime> data_preparing = new TableColumn<>();

    @FXML
    private TableColumn<Order,String> nume_iteme_preparing = new TableColumn<>();

    public void setService(ServiceDB service) {
        this.service=service;
        this.service.setStaffController(this);
        initModel();
    }

    @FXML
    public void initialize() {
        this.id_table.setCellValueFactory(new PropertyValueFactory<>("Table_id"));
        this.data.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.nume_iteme.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            List<Integer> menuItemIds = order.getMenuitems();
            System.out.println(menuItemIds);
            StringBuilder menuItemNames = new StringBuilder();
            for (Integer id : menuItemIds) {
                MenuItem menuItem = this.service.findOne(id);
                if (menuItem != null) {
                    if (!menuItemNames.isEmpty()) {
                        menuItemNames.append(", ");
                    }
                    menuItemNames.append(menuItem.getItem());
                }
            }
            return new ReadOnlyStringWrapper(menuItemNames.toString());
        });


        this.id_table_preparing.setCellValueFactory(new PropertyValueFactory<>("Table_id"));
        this.data_preparing.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.nume_iteme_preparing.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            List<Integer> menuItemIds = order.getMenuitems();
            System.out.println(menuItemIds);
            StringBuilder menuItemNames = new StringBuilder();
            for (Integer id : menuItemIds) {
                MenuItem menuItem = this.service.findOne(id);
                if (menuItem != null) {
                    if (!menuItemNames.isEmpty()) {
                        menuItemNames.append(", ");
                    }
                    menuItemNames.append(menuItem.getItem());
                }
            }
            return new ReadOnlyStringWrapper(menuItemNames.toString());
        });


        this.orderTableView_preparing.setItems(this.model_orders_preparing);
        this.orderTableView.setItems(this.model_orders);
    }

    public void initModel() {
        initialize();
        Iterable<Order> allOrders = this.service.getAllOrders();
        List<Order> lista_orders = new ArrayList<>();
        List<Order> lista_orders_preparing = new ArrayList<>();
        for(Order c : allOrders){
            if(c.getStatus()== OrderStatus.PLACED)
                lista_orders.add(c);
            else if(c.getStatus()==OrderStatus.PREPARING)
                lista_orders_preparing.add(c);
        }
        this.model_orders_preparing.setAll(lista_orders_preparing);
        this.model_orders.setAll(lista_orders);
    }


    public void prepare(){
        Order o = this.orderTableView.getSelectionModel().getSelectedItem();
        if(o==null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("YOUR MUST SELECT A PLACED ORDER!");
            message.showAndWait();
        }
        else{
            this.service.modify_status(o.getId(),OrderStatus.PREPARING);
            initModel();
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("SUCCESS!");
            message.setContentText("ORDER IS NOW BEING PREPARED!");
            message.showAndWait();
        }
    }

    public void deliver(){
        Order o = this.orderTableView_preparing.getSelectionModel().getSelectedItem();
        if(o==null){
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("YOUR MUST SELECT A PLACED ORDER!");
            message.showAndWait();
        }
        else{
            this.service.modify_status(o.getId(),OrderStatus.SERVED);
            initModel();
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("SUCCESS!");
            message.setContentText("ORDER IS NOW BEING PREPARED!");
            message.showAndWait();
        }
    }


}
