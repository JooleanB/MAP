package com.example.restaurant.Repository;

import com.example.restaurant.Domain.MenuItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepo {
    private List<MenuItem> menuItems = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public MenuItemRepo(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }


    public MenuItem findOne(int id){
        for(MenuItem m : menuItems){
            if(m.getId()==id){
                return m;
            }
        }
        return null;
    }



    public Iterable<MenuItem> findAll(){
        if(this.menuItems.isEmpty())
        {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
                 ResultSet resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String category =resultSet.getString("category");
                    String item =resultSet.getString("item");
                    float price = resultSet.getFloat("price");
                    String currency = resultSet.getString("currency");
                    MenuItem menuItem = new MenuItem(id,category,item,price,currency);
                    this.menuItems.add(menuItem);
                }
                return this.menuItems;

            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return this.menuItems;
    }


}
