package com.example.skyclad.mysqltest;

import android.content.Context;
import android.content.Intent;
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
    //i made some memes;
    TextView status;
    String name, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.uname);
        etPass = (EditText) findViewById(R.id.pass);
        status = (TextView) findViewById(R.id.status);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("Error","connectivity manager");
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.d("Error","network Info");
        if(networkInfo!=null) {
            Log.d("Error","inside not null");
            if (networkInfo.isConnectedOrConnecting()) {
                Log.d("Error", "inside connected or connecting");
                status.setText("Network Found!");
            }
        }else
            status.setText("Network not found.");
        Log.d("Error","after if statement");
    }
    public void butReg(View view){
        startActivity(new Intent(this,Register.class));
    }
    public void butData(View view) {startActivity(new Intent(this,Json.class));}
    public void butLogin(View view){
        name = etName.getText().toString();
        pass = etPass.getText().toString();
        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,name,pass);
    }
}
