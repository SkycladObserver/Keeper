package com.example.skyclad.mysqltest;

/**
 * Created by acer on 11/1/2016.
 */

public class Item {
    //photo not yet included
    int type;
    String name,description, location, uploader;
    boolean claimed;
    public Item(String name, String description, String location){
        this.name = name;
        this.description = description;
        this.location = location;
    }

}
