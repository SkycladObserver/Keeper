package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayListView extends AppCompatActivity {
    String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    UserAdapter userAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_view);
        listView = (ListView) findViewById(R.id.listview);
        userAdapter = new UserAdapter(this,R.layout.row_layout);
        listView.setAdapter(userAdapter);
        jsonString = getIntent().getExtras().getString("jsonData");
        Log.d("ListView","inside onCreate");
        try{
            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String name,uname,pass;
            Log.d("ListView","inside try");
            while (count <jsonArray.length()){
                Log.d("ListView","inside while");
                JSONObject JO = jsonArray.getJSONObject(count);
                name = JO.getString("name");
                uname = JO.getString("uname");
                pass = JO.getString("pass");
                Log.d("ListView",name+" "+uname+" "+pass);
                Users user = new Users(name,uname,pass);
                userAdapter.add(user);
                count++;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }
}
