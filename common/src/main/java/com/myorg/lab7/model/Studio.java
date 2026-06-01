package com.myorg.lab7.model;

import java.io.Serializable;

public class Studio implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name; 

    public Studio(String name) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("-> throwing exception!");
            throw new IllegalArgumentException("Studio name cannot be null or empty");
        }
        this.name = name;
    }

    public void setName(String name){
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null");
        }
    }

    public String getName(){return this.name;}

    @Override
    public String toString(){
        return "Name: " + name;
    }
}
