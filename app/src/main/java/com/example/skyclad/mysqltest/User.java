package com.example.skyclad.mysqltest;

/**
 * Created by acer on 10/29/2016.
 */

public class User {
    private String name,uname,pass;
    public User(String name, String uname, String pass){
        this.setName(name);
        this.setUName(uname);
        this.setPass(pass);
    }
    public String getName(){
        return name;
    }
    public String getUName(){
        return uname;
    }
    public String getPass(){
        return pass;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUName(String uname){
        this.uname = uname;
    }
    public void setPass(String pass){
        this.pass = pass;
    }
}
