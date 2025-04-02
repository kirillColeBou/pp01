package com.example.pp01;

import java.io.Serializable;

public class Storehouse implements Serializable {
    private int id;
    private String name;

    public Storehouse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}