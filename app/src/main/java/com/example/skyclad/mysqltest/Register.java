package com.example.skyclad.mysqltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText ET_FNAME,ET_LNAME,ET_UNAME,ET_PASS,ET_EMAIL;
    String fname, lname, uname, pass,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ET_FNAME = (EditText) findViewById(R.id.fname);
        ET_LNAME = (EditText) findViewById(R.id.lname);
        ET_UNAME = (EditText) findViewById(R.id.uname);
        ET_PASS = (EditText) findViewById(R.id.pass);
        ET_EMAIL = (EditText) findViewById(R.id.email);
    }
    public void butReg(View view){
        fname = ET_FNAME.getText().toString();
        lname = ET_LNAME.getText().toString();
        uname = ET_UNAME.getText().toString();
        pass = ET_PASS.getText().toString();
        email = ET_EMAIL.getText().toString();
        String method = "register";
        BackgroundTask bgTask = new BackgroundTask(this);
        bgTask.execute(method,fname,lname,uname,pass,email);
        finish();
    }

}
