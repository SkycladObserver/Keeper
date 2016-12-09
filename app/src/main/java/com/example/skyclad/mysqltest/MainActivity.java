/**
 * Module name: MainActivity
 * Description:  First View seen. This is the log in screen.
 * Programmer: Brent Carl Anonas
 * Date Coded: October 24, 2016
 * Module Parameters: none.
 * Variable names:
 *      EditText etName, etPass; - editTexts
 *      public static final String DEFAULT = "N/A"; - for SharedPreferences. If N/A, meaning no logged user.
 *      String name, pass; data from editTexts
 * Files accessed: activity_main.xml, BackgroundTask.java, ViewPagerActivity.java, Register.java
 * Files updated: none
 * Module Input: User's log in information.
 * Module Output: Log in information to be sent to BackgroundTask.java for verification.
 * Error handling capabilities: none.
 */

package com.example.skyclad.mysqltest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etName, etPass;
    public static final String DEFAULT = "N/A";
    String name, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        String uname = sharedPreferences.getString("uname",DEFAULT);
        Log.d("login",uname);
        if(!uname.equals(DEFAULT)){ //if there is no data in sharedPreferences, meaning if user is logged in, directly go to ViewPagerActivity
            startActivity(new Intent(this,ViewPagerActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.uname);
        etPass = (EditText) findViewById(R.id.pass);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("Error","connectivity manager");
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.d("Error","network Info");
        if(networkInfo!=null) {
            Log.d("Error","inside not null");
            if (networkInfo.isConnectedOrConnecting()) {
                Log.d("Error", "inside connected or connecting");
               // status.setText("Network Found!");
            }
        }else
            //status.setText("Network not found.");
        Log.d("Error","after if statement");
    }
    public void butReg(View view){
        startActivity(new Intent(this,Register.class));
    }
    public void butLogin(View view){
        name = etName.getText().toString();
        pass = etPass.getText().toString();
        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,name,pass);
    }
}
