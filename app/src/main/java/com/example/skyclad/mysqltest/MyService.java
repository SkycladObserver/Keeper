package com.example.skyclad.mysqltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final String TAG = "ServiceThread";
    private Timer timer;
    String jsonString;
    String jsonData;
    User user;
    List<User> users =  Collections.emptyList();
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "Timer task doing work");
            try {
                users = new ArrayList<User>();
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
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                String name,uname,pass;
                Log.d("ListView","inside try");
                int count = 0;
                while (count<jsonArray.length()){
                    Log.d("ListView","inside while");
                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("name");
                    uname = JO.getString("uname");
                    pass = JO.getString("pass");

                    synchronized (resultLock) {
                        users.add(user = new User(name,uname,pass));
                        Log.d("ServiceThread",user.getName()+" "+user.getUName()+" "+user.getPass());
                    }
                    count++;
                }
                synchronized (listeners) {
                    for (ItemListener listener : listeners) {
                        try {
                            listener.handleListUpdated();
                        } catch (RemoteException e) {
                            Log.w(TAG, "Failed to notify listener " + listener, e);
                        }
                    }
                }
            }catch (Throwable t) { /* you should always ultimately catch
									   all exceptions in timer tasks, or
									   they will be sunk */
                t.printStackTrace();
                Log.e(TAG, "Failed", t);
            }
        }
    };
    private final Object resultLock = new Object();

    private List<ItemListener> listeners = new ArrayList<ItemListener>();

    private AidlApi.Stub apiEndpoint = new AidlApi.Stub(){
        @Override
        public User getUser() throws RemoteException {
            synchronized (resultLock) {
                return user;
            }
        }
        @Override
        public List<User> getUsers() throws RemoteException{
            synchronized (resultLock) {
                return users;
            }
        }
        @Override
        public void addListener(ItemListener listener) throws RemoteException {
            synchronized (listeners) {
                listeners.add(listener);
            }
        }

        @Override
        public void removeListener(ItemListener listener) throws RemoteException {
            synchronized (listeners) {
                listeners.remove(listener);
            }
        }
    };
    public IBinder onBind(Intent intent) {
        if (MyService.class.getName().equals(intent.getAction())) {
            Log.d(TAG, "Bound by intent " + intent);
            return apiEndpoint;
        } else {
            return null;
        }
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
}
