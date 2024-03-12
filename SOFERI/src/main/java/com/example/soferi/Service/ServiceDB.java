package com.example.soferi.Service;

import com.example.soferi.Controller.ControllerClienti;
import com.example.soferi.Controller.ControllerSoferi;
import com.example.soferi.Domain.Comanda;
import com.example.soferi.Domain.Persoana;
import com.example.soferi.Domain.Sofer;
import com.example.soferi.Repository.RepoComanda;
import com.example.soferi.Repository.RepoPersoane;
import com.example.soferi.Repository.RepoSoferi;

import java.util.ArrayList;
import java.util.List;

public class ServiceDB {
    private RepoSoferi repoSoferi;
    private RepoPersoane repoPersoane;
    private RepoComanda repoComanda;

    private List<ControllerClienti> lista_clienti=new ArrayList<>();

    private List<ControllerSoferi> lista_soferi = new ArrayList<>();

    public ServiceDB(RepoPersoane repoPersoane, RepoSoferi repoSoferi, RepoComanda repoComanda) {
        this.repoPersoane=repoPersoane;
        this.repoSoferi=repoSoferi;
        this.repoComanda=repoComanda;
        this.repoComanda.setRepoPersoane(this.repoPersoane);
        this.repoComanda.setRepoSoferi(this.repoSoferi);
    }

    public Iterable<Sofer> getSoferi(){
        return this.repoSoferi.getAllSofer();
    }
    public Iterable<Persoana> getClienti(){
        return this.repoPersoane.getAllPersoane();
    }

    public Iterable<Comanda> getComenzi(){
        return this.repoComanda.findAll();
    }

    public void adaugaComanda(Comanda c){
        this.repoComanda.save(c);
    }

    public void adaugaComandaBD(Long id){
        this.repoComanda.save_DB(id);
    }

    public void modifica(Long id,Long id_sofer,String timp){
        Sofer sofer = this.repoSoferi.findOne(id_sofer);
        this.repoComanda.comanda_preluata(id,sofer,timp);
    }

    public void cancel(Long id){
        this.repoComanda.comanda_canceled(id);
    }


    public Iterable<Persoana> getClientiPaginat(){
        return this.repoPersoane.findAll_Limit_Offset();
    }


    public void setSofer_RepoPersoane(Long id){
        this.repoPersoane.setId_taximetrist(id);
    }

    public void setLimit(int limit){
         this.repoPersoane.setLimit(limit);
    }

    public void setOffset(int offset){
        this.repoPersoane.setOffset(offset);
    }

    public void add_client(ControllerClienti c){
        lista_clienti.add(c);
    }

    public void add_sofer(ControllerSoferi s){
        lista_soferi.add(s);
    }

    public void remove_client(ControllerClienti c){
        lista_clienti.remove(c);
    }

    public void remove_sofer(ControllerSoferi s){
        lista_soferi.remove(s);
    }

    public void init_model_all_controllers(){
        for(ControllerSoferi s : this.lista_soferi){
            s.initModel();
        }
        for(ControllerClienti c : this.lista_clienti){
            c.initModel();
        }
    }


    public int get_offset(){
        return this.repoPersoane.getOffset();
    }

    public int get_limit(){
        return this.repoPersoane.getLimit();
    }

    public int get_count(){
        return this.repoPersoane.getCOUNT();
    }

}
