package com.example.trenuri.Repo;

import com.example.trenuri.Domain.Cities;
import com.example.trenuri.Domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepoTicket {
    private List<Ticket> ticketList = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public RepoTicket(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Ticket> findAll() {
        List<Ticket> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                int trainID = resultSet.getInt("train_id");
                int departureCityID = resultSet.getInt("departure_city_id");
                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
                Ticket t = new Ticket(trainID,departureCityID,data);
                list.add(t);
            }
            this.ticketList=list;
            return this.ticketList;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.ticketList;
    }

    public void save(Ticket t){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String orderInsertQuery = "INSERT INTO " + this.tableName + " VALUES (?, ?, ?);";
            try (PreparedStatement orderStatement = connection.prepareStatement(orderInsertQuery)) {
                orderStatement.setInt(1, t.getTraindID());
                orderStatement.setInt(2, t.getDepartureCityID());
                orderStatement.setTimestamp(3, Timestamp.valueOf(t.getData()));
                orderStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public int get_number_of_tickets(int trainID, int departureCityID) {
        int count = 0;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String countQuery = "SELECT COUNT(*) FROM " + this.tableName + " WHERE train_id = ? AND departure_city_id = ?;";
            try (PreparedStatement countStatement = connection.prepareStatement(countQuery)) {
                countStatement.setInt(1, trainID);
                countStatement.setInt(2, departureCityID);

                try (ResultSet resultSet = countStatement.executeQuery()) {
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return count;
    }

}
