package com.example.trenuri.Repo;

import com.example.trenuri.Domain.TrainStation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepoTrainStation {
    private List<TrainStation> TrainStationList = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;

    public RepoTrainStation(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<TrainStation> findAll() {
        List<TrainStation> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                int trainID = resultSet.getInt("trainID");
                int departureCityID = resultSet.getInt("departureCityID");
                int destinationCityID = resultSet.getInt("destinationCityID");
                TrainStation c = new TrainStation(trainID,departureCityID,destinationCityID);
                list.add(c);
            }
            this.TrainStationList=list;
            return this.TrainStationList;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.TrainStationList;
    }

}
