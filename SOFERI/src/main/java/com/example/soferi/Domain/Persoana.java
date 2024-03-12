package com.example.soferi.Domain;

import java.util.Objects;

public class Persoana extends Entity<Long>{
    private String nume;
    private double varsta;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getVarsta() {
        return varsta;
    }

    public void setVarsta(double varsta) {
        this.varsta = varsta;
    }


    public Persoana(Long id,String nume, double varsta) {
        this.nume = nume;
        this.varsta = varsta;
        super.setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Persoana persoana = (Persoana) o;
        return Double.compare(varsta, persoana.varsta) == 0 && Objects.equals(nume, persoana.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, varsta);
    }


    @Override
    public String toString() {
        return "Persoana{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                '}';
    }
}
