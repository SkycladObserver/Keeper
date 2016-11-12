package com.example.skyclad.mysqltest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 11/1/2016.
 */

public class Item implements Parcelable{
    //photo not yet included
    int type, claimed;
    String name, description, location, time, uploader, email;
    public Item(String name, String description, String location, String time, String uploader, String email, int type, int claimed){
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.uploader = uploader;
        this.email = email;
        this.type = type;
        this.claimed = claimed;
    }
    protected Item(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        time = in.readString();
        uploader = in.readString();
        email = in.readString();
        type = in.readInt();
        claimed = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getLocation(){
        return location;
    }
    public String getTime(){
        return time;
    }
    public String getUploader(){
        return uploader;
    }
    public String getEmail(){
        return email;
    }
    public int getType(){
        return type;
    }
    public int getClaimed(){
        return claimed;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeString(getLocation());
        dest.writeString(getTime());
        dest.writeString(getUploader());
        dest.writeString(getEmail());
        dest.writeInt(getType());
        dest.writeInt(getClaimed());
    }

}
