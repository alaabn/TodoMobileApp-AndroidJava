package com.example.todo.database;

public class User {
    private String id;
    private String nom;
    private String email;
    private String motdpass;

    public User() {

    }
    public User(String nom, String email, String motdpass) {
        this.nom = nom;
        this.email = email;
        this.motdpass = motdpass;
    }
    public User(String id, String nom, String email, String motdpass) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motdpass = motdpass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotdpass() {
        return motdpass;
    }

    public void setMotdpass(String motdpass) {
        this.motdpass = motdpass;
    }
}
