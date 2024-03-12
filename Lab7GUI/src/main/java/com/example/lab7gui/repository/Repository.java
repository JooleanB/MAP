package com.example.lab7gui.repository;

import com.example.lab7gui.domain.Entity;

import java.util.Optional;

@FunctionalInterface
public interface Repository<ID, E extends Entity<ID>> {
    Iterable<E> findAll();
}
