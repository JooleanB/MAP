package com.example.lab7gui.service;


import com.example.lab7gui.domain.Utilizator;

@FunctionalInterface
public interface ServiceInterface {
    Iterable<Utilizator> mostSociableCommunity();
}
