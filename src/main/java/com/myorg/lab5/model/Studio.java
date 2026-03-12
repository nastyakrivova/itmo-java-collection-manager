package com.myorg.lab5.model;

import java.util.Objects;

public class Studio {
    private String name; 

    public Studio(String name){
        setName(name);
    }

    public void setName(String name){
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }

    public String getName(){return this.name;}

    @Override
    public String toString(){
        return "Name: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Studio studio = (Studio) o;
        return Objects.equals(name, studio.name);
    }
}
