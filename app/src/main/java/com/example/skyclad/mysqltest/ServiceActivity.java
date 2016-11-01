package com.example.skyclad.mysqltest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ServiceActivity extends AppCompatActivity {
    private TextView tv;
    private AidlApi api;
    Handler handler;
    private ItemListener.Stub itemListener = new ItemListener.Stub() {
        @Override
        public void handleListUpdated() throws RemoteException {
            updateView();
        }
    };
    private ServiceConnection serviceConnection =new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceThread", "Service connection established");
            api = AidlApi.Stub.asInterface(service);
            try {
                api.addListener(itemListener);
            } catch (RemoteException e) {
                Log.e("ServiceThread", "Failed to add listener", e);
            }
            //TODO updateTweetView
            updateView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Service Thread", "Service connection closed");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        handler = new Handler();
        tv = (TextView) findViewById(R.id.data);
    }
    public void startService(View view){
        Intent i = new Intent(MyService.class.getName());
        //Intent i = new Intent(this,MyService.class);
        //i.putExtra("message","DANK MEMES BRUH");
        startService(i);
        bindService(i,serviceConnection,0);
    }

    public void stopService(View view){
        Intent i = new Intent(MyService.class.getName());//new Intent(this,MyService.class);
        stopService(i);
    }
    private void updateView(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    User user = api.getUser();
                    StringBuilder builder = new StringBuilder();
                    builder.append(tv.getText());
                    builder.append("\n"+user.getName()+" "+user.getUName()+" "+user.getPass());
                    tv.setText(builder.toString());
                } catch (Throwable t) {
                    Log.e("ServiceThread", "Error while updating the UI", t);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            api.removeListener(itemListener);
            unbindService(serviceConnection);
        } catch (Throwable t) {
            // catch any issues, typical for destroy routines
            // even if we failed to destroy something, we need to continue destroying
            Log.d("ServiceThread", "Failed to unbind from the service", t);
        }

        Log.i("ServiceThread", "Activity destroyed");
    }
}
