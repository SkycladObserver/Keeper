package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {
    String type;
    TextView tvTime;
    Button butAddItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        tvTime = (TextView) findViewById(R.id.tvTime);
        butAddItem = (Button) findViewById(R.id.butAddItem);
        type = getIntent().getExtras().getString("type");
        tvTime.setText("Time "+type);
        butAddItem.setText("Add "+type+" Item");
    }
}
