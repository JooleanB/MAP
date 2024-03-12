package com.example.lab7gui.repository;



import com.example.lab7gui.domain.Entity;
import com.example.lab7gui.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    public Optional<E> findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }


    public Optional<E> save(E entity) {
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return Optional.empty();
        }
        else entities.put(entity.getId(),entity);
        return Optional.of(entity);
    }


    public Optional<E> delete(ID id) {
        entities.remove(id);
        return Optional.empty();
    }

    public Optional<E> update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return Optional.of(entity);
    }

}
