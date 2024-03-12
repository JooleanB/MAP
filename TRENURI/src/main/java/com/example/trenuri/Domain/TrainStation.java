package com.example.trenuri.Domain;

import java.util.Objects;

public class TrainStation {
    private int trainID;
    private int departureCityID;

    private int destinationCityID;

    public TrainStation(int trainID, int departureCityID, int destinationCityID) {
        this.trainID = trainID;
        this.departureCityID = departureCityID;
        this.destinationCityID = destinationCityID;
    }

    public int getTrainID() {
        return trainID;
    }

    public void setTrainID(int trainID) {
        this.trainID = trainID;
    }

    public int getDepartureCityID() {
        return departureCityID;
    }

    public void setDepartureCityID(int departureCityID) {
        this.departureCityID = departureCityID;
    }

    public int getDestinationCityID() {
        return destinationCityID;
    }

    public void setDestinationCityID(int destinationCityID) {
        this.destinationCityID = destinationCityID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainStation that = (TrainStation) o;
        return trainID == that.trainID && departureCityID == that.departureCityID && destinationCityID == that.destinationCityID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainID, departureCityID, destinationCityID);
    }

    @Override
    public String toString() {
        return "TrainStation{" +
                "trainID=" + trainID +
                ", departureCityID=" + departureCityID +
                ", destinationCityID=" + destinationCityID +
                '}';
    }
}
