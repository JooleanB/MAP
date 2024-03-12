package com.example.trenuri.Repo;

import com.example.trenuri.Domain.Cities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepoCities {
    private List<Cities> citiesList = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public RepoCities(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Cities> findAll() {
        List<Cities> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                int id = resultSet.getInt("city_id");
                String nume = resultSet.getString("city_name");
                Cities c = new Cities(id,nume);
                list.add(c);
            }
            this.citiesList=list;
            return this.citiesList;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.citiesList;
    }

}
