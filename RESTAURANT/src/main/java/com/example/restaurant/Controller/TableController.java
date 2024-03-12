package com.example.restaurant.Controller;

import com.example.restaurant.Domain.MenuItem;
import com.example.restaurant.Domain.Order;
import com.example.restaurant.Domain.OrderStatus;
import com.example.restaurant.Repository.MenuItemRepo;
import com.example.restaurant.Service.ServiceDB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

public class TableController {
    public VBox tablesContainer;
    private ServiceDB service;

    private int id;

    private List<TableView<MenuItem>> lista_tabele = new ArrayList<>();

    private List<ObservableList<MenuItem>> lista_modele = FXCollections.observableArrayList();


    public void setService(ServiceDB service,int id) {
        this.service=service;
        this.id=id;
        this.service.addTableController(this);
        this.initModel();
    }


    public void initModel() {
        Iterable<MenuItem> allMenuItems = this.service.getAllMenuItems();
        Set<String> uniqueCategories = new HashSet<>();

        for (MenuItem item : allMenuItems) {
            uniqueCategories.add(item.getCategory());
        }

        for (String category : uniqueCategories) {
            List<MenuItem> filteredItems = new ArrayList<>();
            for (MenuItem item : allMenuItems) {
                if (item.getCategory().equals(category)) {
                    filteredItems.add(item);
                }
            }

            Label categoryLabel = new Label(category);
            categoryLabel.setFont(new Font("Arial", 20));
            this.tablesContainer.getChildren().add(categoryLabel);

            TableView<MenuItem> tableView = new TableView<>();

            // Set preferred size for the TableView
            tableView.setPrefSize(300, 200); // Adjust these values as needed

            ObservableList<MenuItem> model = FXCollections.observableArrayList();
            model.setAll(filteredItems);
            TableColumn<MenuItem, String> itemColumn = new TableColumn<>("Item");
            itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

            // Adjust column width as needed
            itemColumn.setPrefWidth(100); // Example width

            TableColumn<MenuItem, String> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData -> {
                MenuItem menuItem = cellData.getValue();
                String price = String.valueOf(menuItem.getPrice());
                String currency = menuItem.getCurrency();
                return new ReadOnlyStringWrapper(price + " " + currency);
            });

            // Adjust column width as needed
            priceColumn.setPrefWidth(100); // Example width

            tableView.getColumns().add(itemColumn);
            tableView.getColumns().add(priceColumn);
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableView.setItems(model);
            this.lista_tabele.add(tableView);
            this.lista_modele.add(model);

            this.tablesContainer.getChildren().add(tableView);
        }

        this.tablesContainer.setPadding(new Insets(10, 10, 10, 10));
    }

    public void make_an_order(){
        int count=0;
        List<Integer> id_menu_items = new ArrayList<>();
        for(TableView<MenuItem> tabel : this.lista_tabele){
            ObservableList<MenuItem> selectii = tabel.getSelectionModel().getSelectedItems();
            for(MenuItem mi : selectii) {
                if (mi != null) {
                    count++;
                    id_menu_items.add(mi.getId());
                }
            }
        }
        if(count!=0){
            Random r = new Random();
            int x = r.nextInt();
            Order order = new Order(x,this.id,id_menu_items, LocalDateTime.now(), OrderStatus.PLACED);
            this.service.save_order(order);
            this.service.notify_controllers();
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ORDER PLACED!");
            message.setContentText("YOUR ORDER HAS BEEN PLACED");
            message.showAndWait();
        }
        else{
            Alert message= new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("ERROR!");
            message.setContentText("YOU MUST SELECT AT LEAST AN ITEM FROM THE MENU IN ORDER TO PLACE AN ORDER!");
            message.showAndWait();
        }
    }
}
