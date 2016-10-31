package com.example.skyclad.mysqltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }
    public void butAddLost(View view){
        Intent intent = new Intent(this,AddItemActivity.class);
        intent.putExtra("type","Lost");
        startActivity(intent);
    }

    public void butAddFound(View view){
        Intent intent = new Intent(this,AddItemActivity.class);
        intent.putExtra("type","Found");
        startActivity(intent);
    }
}
