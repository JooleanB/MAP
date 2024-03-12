package com.example.lab7gui.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Prietenie extends Entity<UUID> {

    private Utilizator user1,user2;
    private String user1_email,user2_email;
    private LocalDateTime date;

    private FriendRequest request;
    public LocalDateTime getDate() {
        return date;
    }
    public String getUser1_email() {
        return user1_email;
    }

    public String getUser2_email() {
        return user2_email;
    }

    public String getRequest() {
        return request.toString();
    }





    public Prietenie(Utilizator u1, Utilizator u2){
        this.user1=u1;
        this.user2=u2;
        this.user1_email=u1.getEmail();
        this.user2_email=u2.getEmail();
        this.request=FriendRequest.PENDING;
        this.setId(UUID.randomUUID());
        this.date =LocalDateTime.now();
    }

    public Prietenie(Utilizator u1, Utilizator u2,LocalDateTime from){
        this.user1=u1;
        this.user1_email=u1.getEmail();
        this.user2_email=u2.getEmail();
        this.user2=u2;
        this.request = FriendRequest.PENDING;
        this.setId(UUID.randomUUID());

        this.date = from;
    }




    public void set_request(FriendRequest r){
        this.request=r;
        if(r==FriendRequest.ACCEPTED){
            addFriend();
        }
    }
    public void addFriend() {
        //Map<UUID,Utilizator> lista_prieteni = user1.getFriends();
        //if(lista_prieteni!=null && lista_prieteni.containsKey(user2.getId())){
        //  return;
        //}
        user1.addFriendUtilizator(user2);
        user2.addFriendUtilizator(user1);

    }

    public Utilizator getUser1() {
        return user1;
    }

    public Utilizator getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", date=" + date +
                ", request=" + request +
                '}';
    }

    /**
     *
     * @return the date when the friendship was created
     */

}
