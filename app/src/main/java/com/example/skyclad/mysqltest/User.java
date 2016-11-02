package com.example.skyclad.mysqltest;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by acer on 10/29/2016.
 */

public class User implements Parcelable {
    private String name,uname,pass;

    public User(String name, String uname, String pass){
        this.setName(name);
        this.setUName(uname);
        this.setPass(pass);
    }

    protected User(Parcel in) {
        name = in.readString();
        uname = in.readString();
        pass = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            Log.d("ServiceThread","createFromParcel");
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getUName());
        dest.writeString(getPass());
    }
}
