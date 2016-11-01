package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RecyclerView","before setContentView");
        setContentView(R.layout.activity_recycler_view);
        Log.d("RecyclerView","before recyclerViewInstantiation");
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        Log.d("RecyclerView","before recyclerViewAdapter Instantiation");
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(),getData());
        Log.d("RecyclerView","before setAdapter");
        recyclerView.setAdapter(recyclerViewAdapter);
        Log.d("RecyclerView","before setLayoutManger");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("RecyclerView","nothing Left");
    }
    private List<User> getData(){
        Log.d("RecyclerView","before list instantiation");
        List<User> users = new ArrayList<User>();
        Log.d("RecyclerView","before dummyData array");
        String dummyData[] = {"brent","dank","memes","daryl","lol","dfq","HAHAHAHA"};
        for(int i = 0; i <= 100; i++) {
            Log.d("RecyclerView", "inside for loop");
            users.add(new User(dummyData[i%4],dummyData[i%4],dummyData[i%4]));
            Log.d("RecyclerView",users.get(i).getName()+" "+users.get(i).getUName()+users.get(i).getPass());
        }
        return users;
    }
}
