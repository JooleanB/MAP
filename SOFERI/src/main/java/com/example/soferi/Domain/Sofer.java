package com.example.soferi.Domain;

import java.util.Objects;

public class Sofer extends Persoana{
    private String indicativMasina;


    public Sofer(Persoana p,String indicativ_masina){
        super(p.getId(),p.getNume(),p.getVarsta());
        this.indicativMasina=indicativ_masina;
    }
    public Sofer(Long id,String nume, double varsta,String indicativMasina) {
        super(id,nume, varsta);
        this.indicativMasina=indicativMasina;
    }

    public String getIndicativMasina() {
        return indicativMasina;
    }

    public void setIndicativMasina(String indicativMasina) {
        this.indicativMasina = indicativMasina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sofer sofer = (Sofer) o;
        return Objects.equals(indicativMasina, sofer.indicativMasina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), indicativMasina);
    }
}
