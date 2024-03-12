package com.example.restaurant.Service;

import com.example.restaurant.Controller.StaffController;
import com.example.restaurant.Controller.TableController;
import com.example.restaurant.Domain.MenuItem;
import com.example.restaurant.Domain.Order;
import com.example.restaurant.Domain.OrderStatus;
import com.example.restaurant.Domain.Table;
import com.example.restaurant.Repository.MenuItemRepo;
import com.example.restaurant.Repository.OrderRepo;
import com.example.restaurant.Repository.TableRepo;

import java.util.ArrayList;
import java.util.List;

public class ServiceDB {

    private StaffController staffController;
    private List<TableController> tableControllerList = new ArrayList<>();
    private MenuItemRepo menuItemRepo;
    private OrderRepo orderRepo;
    private TableRepo tableRepo;

    public ServiceDB(MenuItemRepo menuItemRepo, OrderRepo orderRepo, TableRepo tableRepo) {
        this.menuItemRepo = menuItemRepo;
        this.orderRepo = orderRepo;
        this.tableRepo = tableRepo;
    }

    public Iterable<Order> getAllOrders(){
        return this.orderRepo.findAll();
    }

    public Iterable<Table> getAllTables(){
        return this.tableRepo.findAll();
    }

    public Iterable<MenuItem> getAllMenuItems(){
        return this.menuItemRepo.findAll();
    }

    public void save_order(Order o){
        this.orderRepo.save(o);
    }


    public void modify_status(int id, OrderStatus status){
        this.orderRepo.modifyStatus(id,status);
    }

    public int get_count(){
        return this.tableRepo.getCount();
    }


    public void addTableController(TableController tc){
        this.tableControllerList.add(tc);
    }

    public MenuItem findOne(int id){
        return this.menuItemRepo.findOne(id);
    }

    public void notify_controllers(){
        for(TableController t : this.tableControllerList){
            t.initModel();
        }
        this.staffController.initModel();
    }

    public void setStaffController(StaffController sc){
        this.staffController=sc;
    }
}
