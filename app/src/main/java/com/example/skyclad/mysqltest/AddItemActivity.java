package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {
    EditText eName,eDescription,eLocation,eTime;
    String name,description,location,time;
    String type;
    TextView tvTime;
    Button butAddItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        eName = (EditText) findViewById(R.id.name);
        eDescription = (EditText) findViewById(R.id.description);
        eLocation = (EditText) findViewById(R.id.location);
        eTime = (EditText) findViewById(R.id.time);
        tvTime = (TextView) findViewById(R.id.tvTime);
        butAddItem = (Button) findViewById(R.id.butAddItem);
        type = getIntent().getExtras().getString("type");

        tvTime.setText("Time "+type);
        butAddItem.setText("Add "+type+" Item");
    }
    public void butAddItem(View view){
        name = eName.getText().toString();
        description = eDescription.getText().toString();
        location = eLocation.getText().toString();
        time = eTime.getText().toString();
        String method = "addItem";
        BackgroundTask bgTask = new BackgroundTask(this);
        bgTask.execute(method,name,description,location,time,type);
        finish();
    }
}
