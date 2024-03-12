package com.example.zboruri.Repo;

import com.example.zboruri.Domain.Client;
import com.example.zboruri.Domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepo {

    private List<Flight> clienti = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public FlightRepo(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Flight> findAll() {
        List<Flight> clientArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String from = resultSet.getString("from_location");
                String to = resultSet.getString("to_location");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landing_time").toLocalDateTime();
                int seats = resultSet.getInt("seats");
                Flight c = new Flight(id,from,to,departureTime,landingTime,seats);
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
