package com.example.zboruri.Service;

import com.example.zboruri.Controller.ClientController;
import com.example.zboruri.Domain.Client;
import com.example.zboruri.Domain.Flight;
import com.example.zboruri.Domain.Ticket;
import com.example.zboruri.Repo.ClientRepo;
import com.example.zboruri.Repo.FlightRepo;
import com.example.zboruri.Repo.TicketRepo;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private ClientRepo clientRepo;
    private FlightRepo flightRepo;
    private TicketRepo ticketRepo;

    private List<ClientController> clientControllers= new ArrayList<>();

    public Service(ClientRepo clientRepo, FlightRepo flightRepo, TicketRepo ticketRepo) {
        this.clientRepo = clientRepo;
        this.flightRepo = flightRepo;
        this.ticketRepo = ticketRepo;
    }

    public Iterable<Client> findAll_client(){
        return this.clientRepo.findAll();
    }

    public Iterable<Flight> findAll_flight(){
        return this.flightRepo.findAll();
    }

    public Iterable<Ticket> findAll_ticket(){
        return this.ticketRepo.findAll();
    }

    public void save(Ticket t){
        this.ticketRepo.save(t);
    }

    public void add_controller(ClientController c){
        this.clientControllers.add(c);
    }


}
