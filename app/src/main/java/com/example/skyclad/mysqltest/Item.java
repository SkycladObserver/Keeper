package com.example.skyclad.mysqltest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 11/1/2016.
 */

public class Item implements Parcelable{
    //photo not yet included
    int itemID, type, claimed,userID;
    String name, description, location, time, uploader, email;
    public Item(int itemID, String name, String description, String location, String time, String uploader, String email, int type, int claimed,int userID){
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.uploader = uploader;
        this.email = email;
        this.type = type;
        this.claimed = claimed;
        this.userID = userID;
    }
    protected Item(Parcel in) {
        itemID = in.readInt();
        name = in.readString();
        description = in.readString();
        location = in.readString();
        time = in.readString();
        uploader = in.readString();
        email = in.readString();
        type = in.readInt();
        claimed = in.readInt();
        userID = in.readInt();
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
    public int getItemID(){
        return itemID;
    }
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
    public int getUserID(){
        return userID;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getItemID());
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeString(getLocation());
        dest.writeString(getTime());
        dest.writeString(getUploader());
        dest.writeString(getEmail());
        dest.writeInt(getType());
        dest.writeInt(getClaimed());
        dest.writeInt(getUserID());
    }

}
