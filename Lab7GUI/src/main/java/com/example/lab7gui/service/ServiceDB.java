package com.example.lab7gui.service;



//import com.example.lab7gui.Utils.Events.Event;
//import com.example.lab7gui.Utils.Events.UserChangeEvent;
//import com.example.lab7gui.Utils.Observer.Observable;
//import com.example.lab7gui.Utils.Observer.Observer;
import com.example.lab7gui.domain.Message;
import com.example.lab7gui.domain.Prietenie;
import com.example.lab7gui.domain.Utilizator;
import com.example.lab7gui.repository.FriendshipDatabase;
import com.example.lab7gui.repository.MessageDatabase;
import com.example.lab7gui.repository.UserDatabase;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


import static com.example.lab7gui.domain.FriendRequest.ACCEPTED;
import static com.example.lab7gui.domain.FriendRequest.REJECTED;


public class ServiceDB implements ServiceInterface {
    private UserDatabase repo_utilizator;
    private FriendshipDatabase repo_prieteni;

    private MessageDatabase repo_message;


    public ServiceDB(UserDatabase r1, FriendshipDatabase r2,MessageDatabase r3){
        this.repo_utilizator=r1;
        this.repo_prieteni=r2;
        this.repo_message=r3;
    }

    public void set_limit_offset(int l,int o){
        this.repo_utilizator.setLimit(l);
        this.repo_utilizator.setOffset(o);
        this.repo_prieteni.setLimit(l);
        this.repo_prieteni.setOffset(o);
        this.repo_message.setLimit(l);
        System.out.println("LIMIT MESAJE ESTE : " + repo_message.getLimit());
        this.repo_message.setOffset(o);
    }

    public void set_offset_users(int o){
        this.repo_utilizator.setOffset(o);
    }

    public void set_offset_friendships(int o){
        this.repo_prieteni.setOffset(o);
    }

    public void set_offset_messages(int o){
        this.repo_message.setOffset(o);
    }

    public int get_number_of_users(){
        return this.repo_utilizator.get_count();
    }

    public int get_number_of_friendships(){
        return this.repo_prieteni.get_count();
    }
    public int get_number_of_messages(){
        return this.repo_message.get_count();
    }

    public int get_limit(){
        return this.repo_message.getLimit();
    }
    public int get_offset_users(){
        return this.repo_utilizator.getOffset();
    }

    public int get_offset_friendships(){
        return this.repo_prieteni.getOffset();
    }

    public int get_offset_messages(){
        return this.repo_message.getOffset();
    }

    public boolean adaugare_utilizator(Utilizator u) {
        try{
            if(repo_utilizator.save(u).isPresent())
                return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }


    public boolean adaugare_mesaj(Message u) {
        try{
            if(repo_message.save(u).isPresent())
                return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public void modifica_mesaj(Message entity,Message m){
        repo_message.modifica(entity,m);
    }

    public void modifica_utilizator(Utilizator u,String n,String l, String e){
        repo_utilizator.modifica(u,n,l,e);
    }


    public void sterge_utilizator(String email) {
        if(email==null){
            throw new IllegalArgumentException("EMAIL-UL NU POATE SA FIE NULL");
        }
        Utilizator u = getUtilizatorEmail(email);
        if(u==null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else repo_utilizator.delete(u.getId());
    }


    public void creazaPrietenie(String email1, String email2) {
        Utilizator u1,u2;
        u1=getUtilizatorEmail(email1);
        System.out.println(u1);

        u2=getUtilizatorEmail(email2);

        System.out.println(u2);
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else {
            Prietenie prietenie = new Prietenie(u1,u2);
            repo_prieteni.save(prietenie);
        }
    }

    public void modificaPrietenie(Prietenie p){
            repo_prieteni.modify(p);
    }


    public void stergePrietenie(String email1, String email2) {
        Utilizator u1,u2;
        u1=getUtilizatorEmail(email1);
        u2=getUtilizatorEmail(email2);
        if(email1==null || email2==null){
            throw new IllegalArgumentException("EMAIL-UL NU POATE SA FIE NULL");
        }
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else{
            Iterable<Prietenie> lista = repo_prieteni.findAll();
            for(Prietenie p : lista){
                if (Objects.equals(p.getUser1().getEmail(), u1.getEmail()) && Objects.equals(p.getUser2().getEmail(), u2.getEmail())) {
                    System.out.println(p.getUser1().getEmail());
                    System.out.println(p.getUser2().getEmail());
                    repo_prieteni.delete(p.getId());
                    break;
                }
                if (Objects.equals(p.getUser2().getEmail(), u1.getEmail()) && Objects.equals(p.getUser1().getEmail(), u2.getEmail())) {
                    System.out.println(p.getUser1().getEmail());
                    System.out.println(p.getUser2().getEmail());
                    repo_prieteni.delete(p.getId());
                    break;
                }
            }
        }
    }


    public Iterable<Utilizator> getAllUtilizatori() {
        return repo_utilizator.findAll();
    }

    public Iterable<Utilizator> getUtilizatoriLimitOffset(){ return repo_utilizator.findAll_Limit_Offset();}

    public Iterable<Prietenie> getAllPrietenii() {
        return repo_prieteni.findAll_DB();
    }

    public Iterable<Message> getAllMessages() {
        return repo_message.findAll_DB();
    }



    public int numarComunitati() {
        AtomicInteger nr = new AtomicInteger();
        Set<Utilizator> set = new HashSet<>();
        Iterable<Utilizator> u = getAllUtilizatori();

        u.forEach(utilizator->{
            if(!set.contains(utilizator)){
                nr.getAndIncrement();
                DFS(utilizator,set);
            }
        });
        return nr.get();
    }


    public void acceptFriendRequest(String email1, String email2) {
        Utilizator u1,u2;
        if(email1==null || email2==null){
            throw new IllegalArgumentException("EMAIL-UL NU POATE SA FIE NULL");
        }
        u1=getUtilizatorEmail(email1);
        u2=getUtilizatorEmail(email2);

        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else{
            Iterable<Prietenie> lista = repo_prieteni.findAll_DB();
            lista.forEach(p->{
                if(Objects.equals(p.getUser1().getEmail(), u1.getEmail()) && Objects.equals(p.getUser2().getEmail(), u2.getEmail())){
                    p.set_request(ACCEPTED);
                }
            });
        }
    }


    public void declineFriendRequest(String email1, String email2) {
        Utilizator u1,u2;
        u1=getUtilizatorEmail(email1);
        u2=getUtilizatorEmail(email2);
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("NU EXISTA UTILIZATOR CU ACEST EMAIL");
        }
        else{
            Iterable<Prietenie> lista = repo_prieteni.findAll();
            lista.forEach(p->{
                if(p.getUser1()==u1 && p.getUser2()==u2){
                    p.set_request(REJECTED);
                }
            });
        }
    }




    public Utilizator getUtilizatorEmail(String email) {
        Iterable<Utilizator> lista = repo_utilizator.findAll();
        AtomicReference<Utilizator> found = new AtomicReference<>();
        lista.forEach(u->{
            if(u.getEmail().equals(email)){
                found.set(u);
            }
        });
        return found.get();
    }

    public void prieteni_in_luna(String email, Month luna){
        Iterable<Prietenie> prietenii = getAllPrietenii();
        for(Prietenie prieten : prietenii){
            if(Objects.equals(prieten.getUser1().getEmail(), email)){
                LocalDateTime from = prieten.getDate();
                if(from.getMonth()==luna){
                    System.out.println(
                            prieten.getUser2().getFirstName() +
                                    " | " +
                                    prieten.getUser2().getLastName() +
                                    " | " +
                                    from
                    );
                }
            }
            if(Objects.equals(prieten.getUser2().getEmail(), email)){
                LocalDateTime from = prieten.getDate();
                if(from.getMonth()==luna){
                    System.out.println(
                            prieten.getUser1().getFirstName() +
                                    " | " +
                                    prieten.getUser1().getLastName() +
                                    " | " +
                                    from
                    );
                }
            }
        }
    }

//    public void add_date(){
//        Utilizator u1=new Utilizator("u1FirstName", "u1LastName","u1Email");
//        u1.setId(UUID.randomUUID());
//        Utilizator u2=new Utilizator("u2FirstName", "u2LastName","u2Email"); u2.setId(UUID.randomUUID());
//        Utilizator u3=new Utilizator("u3FirstName", "u3LastName","u3Email"); u3.setId(UUID.randomUUID());
//        Utilizator u4=new Utilizator("u4FirstName", "u4LastName","u4Email"); u4.setId(UUID.randomUUID());
//        Utilizator u5=new Utilizator("u5FirstName", "u5LastName","u5Email"); u5.setId(UUID.randomUUID());
//        Utilizator u6=new Utilizator("u6FirstName", "u6LastName","u6Email"); u6.setId(UUID.randomUUID());
//        Utilizator u7=new Utilizator("u7FirstName", "u7LastName","u7Email"); u7.setId(UUID.randomUUID());
//        repo_utilizator.save(u1);
//        repo_utilizator.save(u2);
//        repo_utilizator.save(u3);
//        repo_utilizator.save(u4);
//        repo_utilizator.save(u5);
//        repo_utilizator.save(u6);
//        repo_utilizator.save(u7);
//        creazaPrietenie(u1.getEmail(),u2.getEmail());
////        creazaPrietenie(u2.getEmail(),u3.getEmail());
////        creazaPrietenie(u4.getEmail(),u5.getEmail());
////        creazaPrietenie(u6.getEmail(),u5.getEmail());
////        creazaPrietenie(u4.getEmail(),u6.getEmail());
////        creazaPrietenie(u1.getEmail(),u3.getEmail());
////        creazaPrietenie(u1.getEmail(),u7.getEmail());
//        acceptFriendRequest(u1.getEmail(),u2.getEmail());
////        acceptFriendRequest(u2.getEmail(),u3.getEmail());
////        acceptFriendRequest(u4.getEmail(),u5.getEmail());
////        acceptFriendRequest(u6.getEmail(),u5.getEmail());
////        acceptFriendRequest(u4.getEmail(),u6.getEmail());
////        acceptFriendRequest(u1.getEmail(),u3.getEmail());
////        acceptFriendRequest(u1.getEmail(),u7.getEmail());
//    }

    private List<Utilizator> DFS(Utilizator u, Set<Utilizator> set){
        List <Utilizator> list = new ArrayList<>();
        list.add(u);
        set.add(u);
        Collection<Utilizator> lista_prieteni = u.getFriends().values();
        lista_prieteni.forEach(f->{
            if(!set.contains(f)){
                List<Utilizator> l = DFS(f, set);
                l.forEach(list::add);
            }
        });
//        for(Utilizator f : u.getFriends().values()){
//            if(!set.contains(f)){
//                List<Utilizator> l = DFS(f, set);
//                for(Utilizator x : l){
//                    list.add(x);
//                }
//            }
//        }
        return list;
    }

    @Override
    public Iterable<Utilizator> mostSociableCommunity() {
        final List<Utilizator>[] list = new List[]{new ArrayList<>()};
        Iterable<Utilizator> it = repo_utilizator.findAll();
        Set<Utilizator> set = new HashSet<>();

        final int[] max = {-1};
        it.forEach(u -> {
            if (!set.contains(u)) {
                List<Utilizator> aux = DFS(u, set);
                int l = longestPath(aux);
                if (l > max[0]) {
                    list[0] = aux;
                    max[0] = l;
                }
            }
        });

        return list[0];
//        List<Utilizator> list = new ArrayList<>();
//        Iterable<Utilizator> it = repo_utilizator.findAll();
//        Set<Utilizator> set = new HashSet<>();
//
//        int max = -1;
//        for(Utilizator u : it)
//            if(!set.contains(u))
//            {
//                List<Utilizator> aux = DFS(u, set);
//                int l = longestPath(aux);
//                if(l > max) {
//                    list=aux;
//                    max = l;
//                }
//            }
//
//        return list;
    }

    private int longestPath(List<Utilizator> nodes){
        final int[] max = {0};
        nodes.forEach(u->{
            int l = longestPathFromSource(u);
            if(max[0] < l)
                max[0] = l;
        });
        return max[0];
    }

    private int longestPathFromSource(Utilizator source){
        Set<Utilizator> set = new HashSet<>();
        return BFS(source, set);
    }

    private int BFS(Utilizator source, Set<Utilizator> set){
        final int[] max = {-1};
        Collection<Utilizator> c =source.getFriends().values();
        c.forEach(f-> {
            if (!set.contains(f)) {
                set.add(f);
                int l = BFS(f, set);
                if (l > max[0])
                    max[0] = l;
                set.remove(f);
            }
        });

        return max[0] + 1;

    }
}
