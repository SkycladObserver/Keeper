package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText ET_NAME, ET_UNAME,ET_PASS;
    String name, uname, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ET_NAME = (EditText) findViewById(R.id.name);
        ET_UNAME = (EditText) findViewById(R.id.uname);
        ET_PASS = (EditText) findViewById(R.id.pass);
    }
    public void butReg(View view){
        name = ET_NAME.getText().toString();
        uname = ET_UNAME.getText().toString();
        pass = ET_PASS.getText().toString();
        String method = "register";
        BackgroundTask bgTask = new BackgroundTask(this);
        bgTask.execute(method,name,uname,pass);
        finish();
    }

}
