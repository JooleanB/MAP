package com.example.zboruri.Repo;

import com.example.zboruri.Domain.Client;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientRepo {
    private List<Client> clienti = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public ClientRepo(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Client> findAll() {
        List<Client> clientArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username_client = resultSet.getString("username");
                String name = resultSet.getString("name");
                Client c = new Client(id,username_client,name);
                clientArrayList.add(c);
            }
            this.clienti = clientArrayList;
            return this.clienti;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.clienti;
    }

}
