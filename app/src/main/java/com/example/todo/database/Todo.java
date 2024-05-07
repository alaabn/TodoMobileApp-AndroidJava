package com.example.todo.database;

public class Todo {
    public static  final String DONE = "Finis";
    public static  final String OPEN = "Creer";
    public static  final String PROGRESS = "En Cours";
    public static  final String CANCELED = "Annuler";
    private String id;
    private String nom;
    private String descp;
    private String state;
    private String user_id;

    public Todo(){
    }

    public Todo(String nom, String descp, String state, String user_id) {
        this.nom = nom;
        this.descp = descp;
        this.state = state;
        this.user_id = user_id;
    }

    public Todo(String id, String nom, String descp, String state, String user_id) {
        this.id = id;
        this.nom = nom;
        this.descp = descp;
        this.state = state;
        this.user_id = user_id;
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

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
