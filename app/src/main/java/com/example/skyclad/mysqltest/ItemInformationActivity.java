
/**
 * Module name: ItemInformationActivity
 * Description: This module handles the interface that displays the information about an item chose from recyclerView.
 * Programmer: Brent Carl Anonas
 * Date Coded: December 4, 2016
 * Module Parameters: type, title, description, location, time, email, uploader - details about the item. Not necessarily parameters,
 *              but they are essential that they must be parameters.
 * Variable names: TextView[] textViews; - text Views in ItemInfoActivity
 * Files accessed: activity_item_information.xml
 * Files updated: none
 * Module Input: same as parameters
 * Module Output: displays to the user the information about the item he chose from the recyclerView.
 * Error handling capabilities: null extras.
 */
package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ItemInformationActivity extends AppCompatActivity {
    TextView[] textViews; //type, title, description, location, time, email, uploader

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);
        textViews = new TextView[7];
        int id[] = {R.id.type, R.id.title, R.id.description, R.id.location, R.id.time, R.id.femail, R.id.uname};
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String getExtras[] = {extras.getString("type"),
                    extras.getString("title"),
                    extras.getString("description"),
                    extras.getString("location"),
                    extras.getString("time"),
                    extras.getString("email"),
                    extras.getString("uploader")};

            for (int i = 0; i < 7; i++) {
                Log.d("getExtra", getExtras[i]);
                textViews[i] = (TextView) findViewById(id[i]);
                textViews[i].setText(getExtras[i]);
            }
        }else{
            Log.d("getExtra", "extras null value");
        }
    }
}
