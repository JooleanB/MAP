package com.example.restaurant.Repository;

import com.example.restaurant.Domain.Order;
import com.example.restaurant.Domain.OrderStatus;
import com.example.restaurant.Domain.Table;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderRepo {
    private List<Order> orders = new ArrayList<>();

    private String url;
    private String username;
    private String password;
    private String tableName;


    public OrderRepo(String url, String username, String password, String tableName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
    }

    public Iterable<Order> findAll() {
        List<Order> orders_db = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int table_id = resultSet.getInt("table_id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");

                List<Integer> id_menu_items = fetchMenuItemsForOrder(connection, id);

                Order order = new Order(id, table_id, id_menu_items, date, OrderStatus.valueOf(status));
                orders_db.add(order);
            }
            this.orders = orders_db;
            return this.orders;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.orders;
    }

    private List<Integer> fetchMenuItemsForOrder(Connection connection, int orderId) {
        List<Integer> id_menu_items = new ArrayList<>();
        String query = "SELECT menuitem_id FROM OrderItems WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    id_menu_items.add(resultSet.getInt("menuitem_id"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching menu items for order: " + e);
        }
        return id_menu_items;
    }





    public void save(Order c) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String orderInsertQuery = "INSERT INTO " + this.tableName + " VALUES (?, ?, ?, ?);";
            try (PreparedStatement orderStatement = connection.prepareStatement(orderInsertQuery)) {
                orderStatement.setInt(1, c.getId());
                orderStatement.setInt(2, c.getTable_id());
                orderStatement.setTimestamp(3, Timestamp.valueOf(c.getDate()));
                orderStatement.setString(4, c.getStatus().toString());
                orderStatement.executeUpdate();
                insertMenuItemsForOrder(connection, c.getId(), c.getMenuitems());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void insertMenuItemsForOrder(Connection connection, int orderId, List<Integer> menuItems) {
        try (PreparedStatement itemStatement = connection.prepareStatement("INSERT INTO OrderItems  VALUES (?, ?);")) {
            for (int menuItemId : menuItems) {
                itemStatement.setInt(1, orderId);
                itemStatement.setInt(2, menuItemId);
                itemStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error inserting menu items for order: " + e);
        }
    }



    public void modifyStatus(int id, OrderStatus newStatus) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String updateQuery = "UPDATE " + this.tableName + " SET status = ? WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, newStatus.toString());
                statement.setInt(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e);
        }
    }


}
