package com.example.soferi.Domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Comanda extends Entity<Long>{
    private Persoana persoana;
    private Sofer taximetrist;
    private LocalDateTime data;

    private String locatie;

    private String time;

    private Status status;

    public Comanda(Long id,Persoana persoana, LocalDateTime data) {
        super.setId(id);
        this.persoana = persoana;
        this.data = data;
        this.status=Status.START;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Persoana getPersoana() {
        return persoana;
    }

    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }

    public Sofer getTaximetrist() {
        return taximetrist;
    }

    public void setTaximetrist(Sofer taximetrist) {
        this.taximetrist = taximetrist;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Comanda comanda = (Comanda) o;
        return Objects.equals(persoana, comanda.persoana) && Objects.equals(taximetrist, comanda.taximetrist) && Objects.equals(data, comanda.data) && Objects.equals(locatie, comanda.locatie) && Objects.equals(time, comanda.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), persoana, taximetrist, data, locatie, time);
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "persoana=" + persoana +
                ", taximetrist=" + taximetrist +
                ", data=" + data +
                ", locatie='" + locatie + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

}
