package com.example.trenuri.Domain;

import java.util.Objects;

public class Cities extends Entity<Integer>{
    private String name;

    public Cities(int id,String name) {
        super.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cities cities = (Cities) o;
        return Objects.equals(name, cities.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "Cities{" +
                "name='" + name + '\'' +
                '}';
    }
}
