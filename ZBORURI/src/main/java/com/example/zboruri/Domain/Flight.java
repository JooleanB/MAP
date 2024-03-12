package com.example.zboruri.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight extends Entity<Long>{
    private String to,from;
    private LocalDateTime departureTime,landingTime;

    private int seats;


    public Flight(Long id,String from, String to, LocalDateTime departureTime, LocalDateTime landingTime,int seats) {
        super.setId(id);
        this.to = to;
        this.from = from;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.seats=seats;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Flight flight = (Flight) o;
        return Objects.equals(to, flight.to) && Objects.equals(from, flight.from) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(landingTime, flight.landingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), to, from, departureTime, landingTime);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", departureTime=" + departureTime +
                ", landingTime=" + landingTime +
                '}';
    }
}
