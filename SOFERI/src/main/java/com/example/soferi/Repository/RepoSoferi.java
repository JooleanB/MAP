package com.example.soferi.Repository;

import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Sofer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RepoSoferi implements Repository<Long, Sofer>{

    List<Sofer> soferi = new ArrayList<>();
    private String url;
    private String username;
    private String password;

    private String tableName;

    public RepoSoferi(String url, String username, String password, String tablename){
        this.url=url;
        this.username=username;
        this.password=password;
        this.tableName=tablename;
    }


    public Iterable<Sofer> getAllSofer(){
        if(this.soferi.isEmpty()){
            return this.findAll();
        }
        else{
            return this.soferi;
        }
    }


    @Override
    public Iterable<Sofer> findAll() {

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName +";");
            ResultSet resultSet = statement.executeQuery();) {

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                double varsta = resultSet.getDouble("varsta");
                String indicativMasina = resultSet.getString("indicativMasina");

                Sofer sofer = new Sofer(id, nume, varsta, indicativMasina);
                soferi.add(sofer);
            }
            return soferi;
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        return soferi;
    }

    public Sofer findOne(Long id){
        for(Sofer sofer : this.soferi) {
            if (Objects.equals(sofer.getId(), id))
                return sofer;
        }
        return null;
    }

}
