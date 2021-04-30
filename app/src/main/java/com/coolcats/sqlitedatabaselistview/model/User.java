package com.coolcats.sqlitedatabaselistview.model;

import android.icu.text.Transliterator;

import com.coolcats.sqlitedatabaselistview.util.Position;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private int id;
    private Position position;

    //Constructor to use when reading from the Database to display information
    public User(String name, int id, Position position) {
        this.name = name;
        this.id = id;
        this.position = position;
    }

    //Constructor to use when writing to Database
    public User(String name, Position position){
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", position=" + position +
                '}';
    }
}

