package com.example.skyclad.mysqltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
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
    Item item;
    List<Item> items =  Collections.emptyList();
    List<User> users =  Collections.emptyList();
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "Timer task doing work");
            try {
                String timestamp = "2016-11-07 09:12:08";
                //Timestamp timestamp = Timestamp.valueOf("2016-11-07 09:12:08");
                items = new ArrayList<Item>();
                String json_url = "http://skycladobserver.net23.net/json_get_item_data.php";
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                /*httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8");
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();*/
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
                int itemID, type, claimed;
                String name, description, location, time, uploader, email;
                Log.d("ListView","inside try");
                int count = 0;
                while (count<jsonArray.length()){
                    Log.d("ListView","inside while");
                    JSONObject JO = jsonArray.getJSONObject(count);
                    itemID = JO.getInt("itemID");
                    name = JO.getString("name");
                    description = JO.getString("description");
                    location = JO.getString("location");
                    time = JO.getString("time");
                    uploader = JO.getString("uname");
                    email = JO.getString("email");
                    type = JO.getInt("type");
                    claimed = JO.getInt("claimed");
                    //timestamp = JO.get()

                    synchronized (resultLock) {
                        items.add(item = new Item(itemID,name,description,location,time,uploader,email,type,claimed));
                        Log.d("ServiceThread",item.getItemID()+" "+item.getName()+" "+item.getDescription());
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
        public Item getItem() throws RemoteException {
            synchronized (resultLock) {
                return item;
            }
        }
        @Override
        public List<Item> getItems() throws RemoteException{
            synchronized (resultLock) {
                return items;
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
