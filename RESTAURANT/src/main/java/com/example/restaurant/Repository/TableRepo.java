package com.example.restaurant.Repository;

import com.example.restaurant.Domain.MenuItem;
import com.example.restaurant.Domain.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableRepo {

    private int count=0;
    private List<Table> tables = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public int getCount(){
        if(this.count==0){
            this.findAll();
        }
        return this.count;
    }
    public TableRepo(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Table> findAll(){
        if(this.tables.isEmpty())
        {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
                 ResultSet resultSet = statement.executeQuery();) {
                int nr=0;
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Table table = new Table(id);
                    this.tables.add(table);
                    nr++;
                }
                this.count=nr;
                return this.tables;
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return this.tables;
    }


}
