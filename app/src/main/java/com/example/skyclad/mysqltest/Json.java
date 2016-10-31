package com.example.skyclad.mysqltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Json extends AppCompatActivity{
    TextView jsonData;
    BackgroundTask bgTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        jsonData = (TextView) findViewById(R.id.jsonData);

    }
    public void getJSON(View view){
        Log.d("JSON","pressed");
        bgTask = new BackgroundTask(this,jsonData);
        bgTask.execute("getJson");
    }
    public void parseJSON(View view){
        if (jsonData.getText().toString().equals("no data"))
            Toast.makeText(getApplicationContext(),"first get JSON",Toast.LENGTH_LONG).show();
        else{
            Log.d("JSON","inside parseJSON");
            Log.d("JSON",jsonData.getText().toString());
            Intent intent = new Intent(this,DisplayListView.class);
            intent.putExtra("jsonData",jsonData.getText().toString());
            startActivity(intent);
        }
    }
}
