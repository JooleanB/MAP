package com.example.trenuri.Service;

import com.example.trenuri.Controller.Client;
import com.example.trenuri.Domain.Cities;
import com.example.trenuri.Domain.Ticket;
import com.example.trenuri.Domain.TrainStation;
import com.example.trenuri.Repo.RepoCities;
import com.example.trenuri.Repo.RepoTicket;
import com.example.trenuri.Repo.RepoTrainStation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Service {

    private List<Client> lista_clienti = new ArrayList<>();
    private RepoCities repoCities;
    private RepoTrainStation repoTrainStation;

    private RepoTicket repoTicket;

    public Service(RepoCities repoCities, RepoTrainStation repoTrainStation, RepoTicket repoTicket) {
        this.repoCities = repoCities;
        this.repoTrainStation = repoTrainStation;
        this.repoTicket=repoTicket;
    }

    public Iterable<Cities> getCities(){
        return this.repoCities.findAll();
    }

    public Iterable<TrainStation> getTrainStations(){
        return this.repoTrainStation.findAll();
    }


    public void add_client(Client c){
        this.lista_clienti.add(c);
    }


    public int get_count_clients(String dep,String dest){
        int nr=0;
        for(Client c : this.lista_clienti){
            if(Objects.equals(c.getDepartureCity(), dep) && Objects.equals(c.getDestinationCity(), dest)){
                nr++;
            }
        }
        return nr;
    }

    public void remove_client(Client c){
        this.lista_clienti.remove(c);
    }


    public void change_label_clients(){
        for(Client c : this.lista_clienti){
            c.set_number_of_clients_looking_for_the_same_route();
        }
    }

    public Cities get_city_by_id(int id){
        Iterable<Cities> orase = this.getCities();
        for(Cities o: orase){
            if(o.getId()==id){
                return o;
            }
        }
        return null;
    }

    public Iterable<Ticket> getTicket(){
        return this.repoTicket.findAll();
    }

    public void save(Ticket t){
        this.repoTicket.save(t);
    }


    public int get_number_of_tickets(int idTrain, int idCity){
        return this.repoTicket.get_number_of_tickets(idTrain,idCity);
    }
}
