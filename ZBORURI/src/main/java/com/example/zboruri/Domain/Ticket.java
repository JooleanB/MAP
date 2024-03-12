package com.example.zboruri.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket extends Entity<Long>{
    private String username;
    private Long flightID;

    LocalDateTime purchaseTime;


    public Ticket(Long id,String username, Long flightID, LocalDateTime purchaseTime) {
        super.setId(id);
        this.username = username;
        this.flightID = flightID;
        this.purchaseTime = purchaseTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getFlightID() {
        return flightID;
    }

    public void setFlightID(Long flightID) {
        this.flightID = flightID;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(username, ticket.username) && Objects.equals(flightID, ticket.flightID) && Objects.equals(purchaseTime, ticket.purchaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, flightID, purchaseTime);
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "username='" + username + '\'' +
                ", flightID=" + flightID +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}
