/**
 * Module name: AddItemActivity
 * Description: This module handles the activity that is shown when a user wants to post an item.
 * Programmer: Brent Carl Anonas
 * Date Coded: November 2, 2016
 * Module Parameters: None.
 * Variable names:
 * EditText eName,eDescription,eLocation,eTime - EditTexts
 * String name,description,location,time,type - the details that will be extracted from the EditTexts
 * TextView tvTime - changes depending if the person is posting a lost or found item.
 * Button butAddItem - button that runs the method to add the item into the server.
 * Files accessed: fragment_account.xml, BackgroundTask.java
 * Files updated:
 * Module Input: Opened through the Floating Action Button in the RecyclerView Fragments.
 * Module Output: Data to be passed to BackgroundTask.java, which in turn sends it to the server.
 * Error handling capabilities: none
 */
package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        eName = (EditText) findViewById(R.id.fname);
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
        String intType = type.equalsIgnoreCase("Lost") ? "0" : "1";
        Log.d("login",type);
        Log.d("login",intType);
        bgTask.execute(method,name,description,location,time,intType);
        finish();
    }
}
