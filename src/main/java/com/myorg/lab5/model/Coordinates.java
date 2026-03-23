package com.myorg.lab5.model;

import java.io.Serializable;

public class Coordinates implements Serializable{
    private static final long serialVersionUID = 1L;
    private int x;
    private Integer y;

    public Coordinates(int x, Integer y){
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        if (x > 290) {
            throw new IllegalArgumentException("X cannot be greater than 290");
        }
        this.x = x;
    }

    public void setY(Integer y) {
        if (y == null) {
            throw new IllegalArgumentException("Y cannot be null");
        }
        this.y = y;
    }

    public int getX() { return x; }
    public Integer getY() { return y; }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
