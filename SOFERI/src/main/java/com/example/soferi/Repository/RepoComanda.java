package com.example.soferi.Repository;

import com.example.soferi.Domain.Comanda;
import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Sofer;
import com.example.soferi.Domain.Status;
import javafx.beans.value.ObservableNumberValue;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class RepoComanda implements Repository<Long,Comanda>{


    private RepoPersoane repoPersoane;
    private RepoSoferi repoSoferi;


    public void setRepoPersoane(RepoPersoane repoPersoane) {
        this.repoPersoane = repoPersoane;
    }

    public void setRepoSoferi(RepoSoferi repoSoferi) {
        this.repoSoferi = repoSoferi;
    }

    private List<Comanda> comenzi = new ArrayList<>();
    private String url;
    private String username;
    private String password;

    private String tableName;




    public RepoComanda(String url, String username, String password, String tablename){
        this.url=url;
        this.username=username;
        this.password=password;
        this.tableName=tablename;
    }


    @Override
    public Iterable<Comanda> findAll() {
        if(this.comenzi.isEmpty()){
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";");
                 ResultSet resultSet = statement.executeQuery();) {
                 while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Long persoana_id =resultSet.getLong("persoana_id");
                    Long sofer_id =resultSet.getLong("taximetrist_id");
                    String locatie = resultSet.getString("locatie");
                    LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
                    String status = resultSet.getString("status");
                    Persoana p = this.repoPersoane.findOne(persoana_id);
                    Sofer s = this.repoSoferi.findOne(sofer_id);
                    Comanda c = new Comanda(id,p,data);
                    c.setStatus(Status.valueOf(status));
                    c.setLocatie(locatie);
                    c.setTaximetrist(s);
                    this.comenzi.add(c);
                }
                return this.comenzi;

            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return this.comenzi;
    }

    public void save(Comanda comanda){
        this.comenzi.add(comanda);
    }

    public void comanda_preluata(Long id, Sofer taximetrist, String timp){
        for(Comanda c : this.comenzi){
            if(Objects.equals(c.getId(), id)){
                    c.setTime(timp);
                    c.setTaximetrist(taximetrist);
                    c.setStatus(Status.TAKEN);
                    System.out.println(c);
                    break;
            }
        }
    }


    public void comanda_canceled(Long id){
        for(Comanda c : this.comenzi){
            if(Objects.equals(c.getId(), id)){
                c.setStatus(Status.CANCELED);
                break;
            }
        }
    }





    public void save_DB(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?);")) {

            for (Comanda c : comenzi) {
                if (Objects.equals(c.getId(), id)) {
                    c.setStatus(Status.ACCEPTED);
                    statement.setLong(1, c.getId());
                    statement.setLong(2, c.getPersoana().getId());
                    statement.setLong(3, c.getTaximetrist().getId());
                    statement.setString(4, c.getLocatie());
                    statement.setTimestamp(5, Timestamp.valueOf(c.getData()));
                    statement.setString(6,c.getStatus().toString());
                    statement.executeUpdate();
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Comanda findOne(Long id) {
        for (Comanda comanda : this.comenzi) {
            if (Objects.equals(comanda.getId(), id))
                return comanda;
        }
        return null;
    }

}
