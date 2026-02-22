package com.myorg.lab5.model;

public class Studio {
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
