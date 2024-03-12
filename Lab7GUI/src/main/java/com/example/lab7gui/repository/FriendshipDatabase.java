package com.example.lab7gui.repository;


import java.sql.*;

import com.example.lab7gui.domain.FriendRequest;
import com.example.lab7gui.domain.Prietenie;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.repository.*;
import com.example.lab7gui.validators.PrietenieValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


//import static com.sun.tools.javac.util.Constants.format;

public class FriendshipDatabase extends AbstractDatabaseRepository<UUID, Prietenie> {

//    Repository<UUID, User> userRepo;
    //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private int limit;
    private int offset;

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public FriendshipDatabase(String url, String username, String password, PrietenieValidator validator){
        super(url, username, password, "friendships",validator);
        loadData();
    }


    @Override
    public void loadData() {
        findAll_DB().forEach(super::save);
    }

    public int get_count(){
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName + ";");
            ResultSet resultSet = statement.executeQuery();) {
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }

    @Override
    public Optional<Prietenie> findOne(UUID id){
        Prietenie friendship = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM" + tableName + "WHERE id = " + id.toString() + ";");
            ResultSet resultSet = statement.executeQuery();) {

            resultSet.next();
            UUID id_ = UUID.fromString(resultSet.getString("id"));
            UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
            UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
            LocalDateTime from = resultSet.getTimestamp("friendsfrom").toLocalDateTime();


            Utilizator u1 = retrieveUser(id_user1);
            Utilizator u2 = retrieveUser(id_user2);
            friendship = new Prietenie(u1, u2,from);
            friendship.setId(id_);

            return Optional.of(friendship);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return Optional.ofNullable(friendship);
    }
    @Override
    public Iterable<Prietenie> findAll_DB() {
        Set<Prietenie> friendships = new HashSet<>();
        final int limit_value = this.limit;
        final int limit_offset = this.offset;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT " + limit_value + " OFFSET " + limit_offset);
            ResultSet resultSet = statement.executeQuery();)
        {
            while(resultSet.next()){
                UUID id_ = UUID.fromString(resultSet.getString("id"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                LocalDateTime from = resultSet.getTimestamp("friendsfrom").toLocalDateTime();


                Utilizator u1 = retrieveUser(id_user1);
                Utilizator u2 = retrieveUser(id_user2);

                if(u1!=null && u2!=null){
                    Prietenie friendship = new Prietenie(u1, u2,from);
                    friendship.set_request(FriendRequest.valueOf(resultSet.getString("request")));
                    friendship.setId(id_);
                    friendships.add(friendship);
                }
            }
            return friendships;
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return friendships;
    }




    @Override
    public Optional<Prietenie> delete(UUID uuid) {
        System.out.println(uuid);
        Optional<Prietenie> f = super.delete(uuid);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = (?);");) {
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
                return f;
        }
        catch (SQLException e){
                System.out.println(e);
        }

        return f;
    }


    //tabelul friendships are 4 coloane: id, id_user1, id_user2, friendsFrom, toate sunt string-uri (varchar(100)).
    @Override
    public Optional<Prietenie> save (Prietenie entity){
        Optional<Prietenie> f = super.save(entity);

        if (f != null){
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?);");
                statement.setString(1, entity.getId().toString());
                statement.setString(2, entity.getUser1().getId().toString());
                statement.setString(3, entity.getUser2().getId().toString());
                statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
                statement.setString(5,entity.getRequest());
                statement.executeUpdate();
            }
            catch(SQLException e){
                System.out.println(e);
            }
        }
        return f;
    }

    public void modify(Prietenie entity){
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement("UPDATE friendships  SET request = (?) WHERE id = (?);");
            statement.setString(1, entity.getRequest());
            statement.setString(2,entity.getId().toString());
            statement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }



    private Utilizator retrieveUser(UUID userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?;");
        ) {
            statement.setString(1, userId.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                Utilizator user = new Utilizator(firstName, lastName, email);
                user.setId(id);

                return user;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

}

