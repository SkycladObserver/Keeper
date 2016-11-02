package com.example.skyclad.mysqltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by acer on 11/2/2016.
 */

public class RecyclerViewDataHelper {
    Handler handler;
    AidlApi api;
    Context context;
    public RecyclerViewDataHelper(Context context){

        handler = new Handler();
        this.context = context;
    }
    ItemListener.Stub itemListener = new ItemListener.Stub(){

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
    public void startService(){
        Intent i = new Intent(MyService.class.getName());
        context.startService(i);
        context.bindService(i,serviceConnection,0);
    }
    public void endService(){
        try {
            api.removeListener(itemListener);
            context.unbindService(serviceConnection);
        } catch (Throwable t) {
            // catch any issues, typical for destroy routines
            // even if we failed to destroy something, we need to continue destroying
            Log.d("ServiceThread", "Failed to unbind from the service", t);
        }
        Log.i("ServiceThread", "Activity destroyed");
    }
    private void updateView() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    List<User> users = api.getUsers();

                } catch (Throwable t) {
                    Log.e("ServiceThread", "Error while updating the UI", t);
                }
                /*try {
                    User user = api.getUser();
                    StringBuilder builder = new StringBuilder();
                    builder.append(tv.getText());
                    builder.append("\n" + user.getName() + " " + user.getUName() + " " + user.getPass());
                    tv.setText(builder.toString());
                } catch (Throwable t) {
                    Log.e("ServiceThread", "Error while updating the UI", t);
                }*/
            }
        });
    }
}
