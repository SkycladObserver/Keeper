/**
 * Module name: user
 * Description: This is the User class. Note these are parcelable (a more efficient serializable), thus the less familiar implementation of item.
 * Programmer: Brent Carl Anonas
 * Date Coded: October 29, 2016
 * Module Parameters: String name, String uname, String pass
 *              - these data is the identifies a user.
 * Variable names: basically the same as the parameters.
 * Files accessed: none
 * Files updated: none
 * Module Input: Data from the server.
 * Module Output: Class item that holds data about an item.
 * Error handling capabilities: none
 */
package com.example.skyclad.mysqltest;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
