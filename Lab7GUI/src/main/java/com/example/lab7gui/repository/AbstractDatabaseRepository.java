package com.example.lab7gui.repository;



import com.example.lab7gui.domain.Entity;
import com.example.lab7gui.validators.Validator;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String url;
    protected String username;
    protected String password;
    protected String tableName;


    public AbstractDatabaseRepository(String url, String username, String password, String tableName, Validator<E> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
        loadData();
    }

    public abstract void loadData();


    protected abstract Iterable<E> findAll_DB();

//    public abstract Optional<E> delete(UUID id);

}