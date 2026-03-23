package com.myorg.lab5.model;

import java.io.Serializable;

public class Studio implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name; 

    public Studio(String name){
        setName(name);
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
