package com.example.trenuri.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
    private int traindID;
    private int departureCityID;
    private LocalDateTime data;

    public int getTraindID() {
        return traindID;
    }

    public void setTraindID(int traindID) {
        this.traindID = traindID;
    }

    public int getDepartureCityID() {
        return departureCityID;
    }

    public void setDepartureCityID(int departureCityID) {
        this.departureCityID = departureCityID;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Ticket(int traindID, int departureCityID, LocalDateTime data) {
        this.traindID = traindID;
        this.departureCityID = departureCityID;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return traindID == ticket.traindID && departureCityID == ticket.departureCityID && Objects.equals(data, ticket.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traindID, departureCityID, data);
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "traindID=" + traindID +
                ", departureCityID=" + departureCityID +
                ", data=" + data +
                '}';
    }
}
