package com.example.skyclad.mysqltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }
    public void startService(View view){
        Intent i = new Intent(this,MyService.class);
        i.putExtra("message","DANK MEMES BRUH");
        startService(i);
    }

    public void stopService(View view){
        Intent i = new Intent(this,MyService.class);
        stopService(i);
    }
}
