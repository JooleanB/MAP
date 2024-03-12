package com.example.soferi.Repository;

import com.example.soferi.Domain.Comanda;
import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Sofer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class RepoPersoane implements Repository<Long, Persoana> {

    private int COUNT;

    public int getCOUNT() {
        return COUNT;
    }

    private int limit;
    private int offset;

    private Long id_taximetrist;

    public Long getId_taximetrist() {
        return id_taximetrist;
    }

    public void setId_taximetrist(Long id_taximetrist) {
        this.id_taximetrist = id_taximetrist;
    }

    private List<Persoana> clienti = new ArrayList<>();
    private String url;
    private String username;
    private String password;

    private String tableName;


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    public RepoPersoane(String url, String username, String password, String tablename) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tablename;
        this.COUNT=0;
    }

    @Override
    public Iterable<Persoana> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                double varsta = resultSet.getDouble("varsta");
                Persoana persoana = new Persoana(id, nume, varsta);
                clienti.add(persoana);
            }
            return clienti;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return clienti;
    }

    public Iterable<Persoana> getAllPersoane() {
        if (this.clienti.isEmpty()) {
            return this.findAll();
        } else {
            return this.clienti;
        }
    }

    public Persoana findOne(Long id) {
        for (Persoana persoana : this.clienti) {
            if (Objects.equals(persoana.getId(), id))
                return persoana;
        }
        return null;
    }



    public Iterable<Persoana> findAll_Limit_Offset(){
        Set<Persoana> clienti = new HashSet<>();
        if(id_taximetrist==null)
            return null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT DISTINCT c.* " +
                            "FROM persoana c " +
                            "JOIN comanda r ON c.id = r.persoana_id " +
                            "WHERE r.taximetrist_id = (?) AND r.status = 'ACCEPTED' " +
                            "LIMIT (?) OFFSET (?)");
            PreparedStatement number_of_clients = connection.prepareStatement(
                    "SELECT COUNT(DISTINCT c.id) \n" +
                            "FROM persoana c \n" +
                            "JOIN comanda r ON c.id = r.persoana_id \n" +
                            "WHERE r.taximetrist_id = (?) AND r.status = 'ACCEPTED';");
            ) {
            number_of_clients.setLong(1,this.id_taximetrist);
            ResultSet nr_clients = number_of_clients.executeQuery();
            if (nr_clients.next()) {
                this.COUNT = nr_clients.getInt(1);
            }
            statement.setLong(1,this.id_taximetrist);
            statement.setInt(2,this.limit);
            statement.setInt(3,this.offset);
            this.offset+=this.limit;
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                double varsta = resultSet.getDouble("varsta");
                Persoana persoana = new Persoana(id, nume, varsta);
                clienti.add(persoana);
            }
            return clienti;
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return clienti;
    }










}
