package com.example.soferi.Repository;
import com.example.soferi.Domain.Entity;
import com.example.soferi.Domain.Persoana;

public interface Repository<ID,E extends Entity<ID>>{


    Iterable<E> findAll();

}
