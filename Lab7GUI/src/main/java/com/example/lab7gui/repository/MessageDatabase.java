package com.example.lab7gui.repository;

import com.example.lab7gui.domain.Message;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDatabase extends AbstractDatabaseRepository<UUID, Message>{

    private int limit =0;
    private int offset =0;

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

    public MessageDatabase(String url, String username, String password, Validator<Message> validator) {
        super(url, username, password, "messages", validator);
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
    public Iterable<Message> findAll_DB() {
        Set<Message> mesaje = new HashSet<>();
        final int limit_value = this.limit;
        final int limit_offset = this.offset;
        System.out.println("IN MESSAGE DATABASE LIMIT VALUE ESTE : "+ limit_value);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT " + limit_value + " OFFSET " + limit_offset);
            ResultSet resultSet = statement.executeQuery();) {
            System.out.println(statement);
            while(resultSet.next()){
                UUID id = UUID.fromString(resultSet.getString("id"));
                String from =resultSet.getString("message_from");
                String list_to = resultSet.getString("message_to");
                String[] lista_string_id = list_to.split(",");
                List<String> to = new ArrayList<>();
                Collections.addAll(to, lista_string_id);
                String mesaj_text =resultSet.getString("message_text");
                LocalDateTime data = resultSet.getTimestamp("message_date").toLocalDateTime();
                String lista_reply_id = resultSet.getString("replies");
                List<UUID> replies = new ArrayList<>();
                if(lista_reply_id!=null) {
                    String[] lista_string_reply = lista_reply_id.split(",");
                    for (String s : lista_string_reply) {
                        UUID id_s = UUID.fromString(s);
                        replies.add(id_s);
                    }
                }
                Message mesaj = new Message(from,to,mesaj_text);
                mesaj.setId(id);
                mesaj.setData(data);
                mesaj.setReplies(replies);
                mesaje.add(mesaj);
            }
            return mesaje;
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return mesaje;
    }

    @Override
    public Optional<Message> save(Message entity){
        Optional<Message> u = super.save(entity);

        if (u != null){
            try ( Connection connection = DriverManager.getConnection(url, username, password)){
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?);");
                statement.setString(1, entity.getId().toString());
                statement.setString(2, entity.getFrom().toString());
                List<String> message_to = entity.getTo();
                StringBuilder string_message_to = new StringBuilder();
                for (int i = 0; i < message_to.size(); i++) {
                    string_message_to.append(message_to.get(i).toString());
                    if (i < message_to.size() - 1) {
                        string_message_to.append(",");
                    }
                }
                String result = string_message_to.toString();
                statement.setString(3, result);
                statement.setString(4, entity.getMessage());
                statement.setTimestamp(5, Timestamp.valueOf(entity.getData()));
                statement.executeUpdate();
            }
            catch(SQLException e){
                System.out.println(e);
            }
        }
        return u;
    }


    public void modifica(Message entity,Message reply){
        if (entity != null){
            try(Connection connection = DriverManager.getConnection(url, username, password)){
                List<UUID> list_reply = entity.getReplies();
                list_reply.add(reply.getId());
                StringBuilder string_reply = new StringBuilder();
                for(UUID id : list_reply){
                    string_reply.append(id.toString());
                    string_reply.append(",");
                }
                string_reply.deleteCharAt(string_reply.length()-1);
                String lista_reply_id = string_reply.toString();
                PreparedStatement statement = connection.prepareStatement("UPDATE " + tableName + " SET replies = (?) WHERE ID = (?);");
                statement.setString(1, lista_reply_id);
                statement.setString(2, entity.getId().toString());
                statement.executeUpdate();
            }
            catch(SQLException el){
                System.out.println(el);
            }
        }
    }
}


