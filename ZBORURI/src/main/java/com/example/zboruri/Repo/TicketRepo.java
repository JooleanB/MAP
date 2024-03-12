package com.example.zboruri.Repo;

import com.example.zboruri.Domain.Flight;
import com.example.zboruri.Domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketRepo {
    private List<Ticket> clienti = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public TicketRepo(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Ticket> findAll() {
        List<Ticket> clientArrayList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username_ticket = resultSet.getString("username");
                Long id_flight = resultSet.getLong("idflight");
                LocalDateTime purchaseTime = resultSet.getTimestamp("data").toLocalDateTime();
                Ticket c = new Ticket(id,username_ticket,id_flight,purchaseTime);
                clientArrayList.add(c);
            }
            this.clienti = clientArrayList;
            return this.clienti;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.clienti;
    }


    public void save(Ticket t) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String orderInsertQuery = "INSERT INTO " + this.tableName + " VALUES (?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = connection.prepareStatement(orderInsertQuery)) {
                preparedStatement.setLong(1,t.getId());
                preparedStatement.setString(2,t.getUsername());
                preparedStatement.setLong(3,t.getFlightID());
                preparedStatement.setTimestamp(4,Timestamp.valueOf(t.getPurchaseTime()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
