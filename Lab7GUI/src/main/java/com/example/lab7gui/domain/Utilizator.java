package com.example.lab7gui.domain;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Utilizator extends Entity<UUID> {
    private String firstName;
    private String lastName;

    private String email;

    private String password;


    private Map<UUID,Utilizator> friends = new HashMap<>();


    public Utilizator(String lastName, String firstName,String email) {
        this.id=UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email=email;
    }

    public Utilizator(String lastName, String firstName,String email,String password) {
        this.id=UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email=email;
        try {
            byte[] secretKeyBytes = "CRIPTARE_PAROLE".getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] dataToEncrypt = password.getBytes();
            byte[] encryptedData = cipher.doFinal(dataToEncrypt);
            this.password = Arrays.toString(encryptedData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<UUID, Utilizator> getFriends() {
        return friends;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFriendUtilizator(Utilizator u){
        friends.put(u.getId(),u);
    }

    public boolean removeFriendUtilizator(Utilizator u){
        return friends.remove(u.getId(),u);
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
