package com.example.skyclad.mysqltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final String TAG = "ServiceThread";
    private Timer timer;
    String jsonString;
    String jsonData;
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "Timer task doing work");
            try {
                String json_url = "http://skycladobserver.net23.net/json_get_data.php";
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                while((jsonString = bufferedReader.readLine())!=null){
                    sb.append(jsonString+"\n");
                }
                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                synchronized (resultLock){
                     jsonData=sb.toString().trim();
                }
                Log.d(TAG,jsonData);
            }catch (Throwable t) { /* you should always ultimately catch
									   all exceptions in timer tasks, or
									   they will be sunk */
                t.printStackTrace();
                Log.e(TAG, "Failed", t);
            }
        }
    };
    private final Object resultLock = new Object();

    private List<ItemListener> itemListeners = new ArrayList<ItemListener>();

    private AidlApi.Stub apiEndpoint = new AidlApi.Stub(){
        @Override
        public User getParcelableItem() throws RemoteException {
            return null;
        }

        @Override
        public void addListener(ItemListener listener) throws RemoteException {

        }

        @Override
        public void removeListener(ItemListener listener) throws RemoteException {

        }
    };
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service creating");
        timer = new Timer("ServiceTimer");
        timer.schedule(updateTask, 1000L, 5000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroying");

        timer.cancel();
        timer = null;
    }

    /*@Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service is created",Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service is started",Toast.LENGTH_LONG).show();
        String message = intent.getStringExtra("message");
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this,"Service is stopped",Toast.LENGTH_LONG).show();
    }*/
}
