package com.example.lab7gui.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Message extends Entity<UUID>{
    private final UUID id;
    private String from;
    private List<   String> to;
    private String message;
    private LocalDateTime data;

    private List<UUID> replies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) && Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(message, message1.message) && Objects.equals(data, message1.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, message, data);
    }

    public Message(String from, List<String> to, String message) {
        this.id = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = LocalDateTime.now();
        this.replies= new ArrayList<>();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public void addReply(Message m){
            replies.add(m.getId());
    }

    public void addReply(UUID m){
        replies.add(m);
    }


    public List<UUID> getReplies() {
        return replies;
    }

    public void setReplies(List<UUID> replies) {
        this.replies = replies;
    }
}
