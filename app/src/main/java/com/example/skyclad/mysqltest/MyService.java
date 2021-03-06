/**
 * Module name: MyService
 * Description:  Service that handles the periodic access to the server. This is for the RecyclerView in RecyclerViewFragment
 *      that displays items. This periodic access is also crucial for the notification system. A service is used so that even
 *      if the user is not using the app, it still works since it is separate from the Application Life Cycle.
 * Programmer: Brent Carl Anonas
 * Date Coded: November 17, 2016
 * Module Parameters: none.
 * Variable names:
 *      private static final String TAG = "ServiceThread";  - for error testing.
 *      private Timer timer; - timer for threading
 *      SharedPreferences sharedPreferences; - sharedPreferences to access String data that contains all items so that even though
 *          the user exits the app, the RecyclerView is still populated.
 *      SharedPreferences.Editor editor; - part of sharedPreferences
 *      String previousTimestamp = "2016-11-07 09:12:08"; - used for timestamp functionality. Accesses from server.
 *      int prevID = 0, userIDSharedPrefs = 0; - prevID is used to determine what the latest ID is so that the server doesn't have
 *              to retrieve all the information from table item over and over again.
 *      String jsonData; - jsonData from server. Instance Variable because it used in multiple portions of the code.
 *      User user; - user holder that is returned through the AIDL.
 *      Item item; - item holder that is returned through the AIDL.
 *      List<Item> userItems =  Collections.emptyList(); - items of the user himself.
 *      List<Item> items =  Collections.emptyList(); - all the items from the server.
 *      List<User> users =  Collections.emptyList(); - list of users.
 * Files accessed: activity_main.xml, BackgroundTask.java, ViewPagerActivity.java, Register.java
 * Files updated: none
 * Module Input: User's log in information.
 * Module Output: Log in information to be sent to BackgroundTask.java for verification.
 * Error handling capabilities: none.
 */
package com.example.skyclad.mysqltest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final String TAG = "ServiceThread";
    private Timer timer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String previousTimestamp = "2016-11-07 09:12:08";
    int prevID = 0;
    int userIDSharedPrefs = 0;
    String jsonData;
    User user;
    Item item;
    List<Item> userItems =  Collections.emptyList();
    List<Item> items =  Collections.emptyList();
    List<User> users =  Collections.emptyList();
    /**
     * requests from server every 5 seconds.
     */
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            requestFromServer();
        }
    };

    /**
     * Refactored. Used to be very inefficient because it retrieved all the data from server. Now it only retrieves the new queries.
     * What needs to be refactored however is the notification, since it is O(n^3).
     */
    public void requestFromServer(){
        Log.i(TAG, "Timer task doing work");
        try {
            Log.d("timestamp","prevID "+Integer.toString(prevID));
            String json_url = "http://skycladobserver.net23.net/json_get_item_data.php";
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String data = URLEncoder.encode("prevID", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(prevID), "UTF-8");
            bw.write(data);
            bw.flush();
            bw.close();
            os.close();
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String jsonString;
            while((jsonString = bufferedReader.readLine())!=null){
                sb.append(jsonString+"\n");
            }
            bufferedReader.close();
            is.close();
            httpURLConnection.disconnect();
            synchronized (resultLock){
                jsonData=sb.toString().trim();
                editor.putString("jsonData",jsonData);
                editor.commit();
            }
            Log.d(TAG,jsonData);
            parseJSON(jsonData,items,true);
            synchronized (listeners) {
                for (ItemListener listener : listeners) {
                    try {
                        listener.handleListUpdated();
                    } catch (RemoteException e) {
                        Log.w(TAG, "Failed to notify listener " + listener, e);
                    }
                }
            }
            synchronized (listeners) {
                Calendar calendar = Calendar.getInstance();
                java.util.Date now = calendar.getTime();
                Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
                previousTimestamp = currentTimestamp.toString();
                //Log.d("timestamp","currentTimestamp: "+previousTimestamp);
            }

        }catch (Throwable t) { /* you should always ultimately catch
									   all exceptions in timer tasks, or
									   they will be sunk */
            t.printStackTrace();
            Log.e(TAG, "Failed", t);
        }
    }
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
    private void parseJSON(String parse,List<Item> items,boolean replacePrev){
        try {
            JSONObject jsonObject = new JSONObject(parse);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int itemID, type, claimed,userID;
            String name, description, location, time, uploader, email;
            int count = 0;

            while (count<jsonArray.length()){
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
                userID = JO.getInt("userID");
                //timestamp = JO.get()
                synchronized (resultLock) {
                    items.add(0,item = new Item(itemID,name,description,location,time,uploader,email,type,claimed,userID));
                    Log.d("ServiceThread",item.getItemID()+" "+item.getName()+" "+item.getDescription());
                    if(item.getUserID()==userIDSharedPrefs){
                        userItems.add(item);
                        Log.d("userItems","added");
                        Log.d("userItems",item.getItemID()+" "+item.getName()+" "+item.getDescription()+" "+item.getUserID());
                    }else{
                        String tags[] = item.getName().split(" ");
                        outerloop:
                        for(Item userItem : userItems){
                            if(userItem.getType()!=item.getType()) {
                                for (String s : tags) {
                                    if (s.contains(userItem.getName())||userItem.getName().contains(s)) {
                                        Intent intent = new Intent(this, ViewPagerActivity.class);
                                        String notifType = type == 0 ? "Lost" : "Found";
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                                        builder.setSmallIcon(R.drawable.ic_sheep_silhouette);
                                        builder.setContentTitle("Similar " + notifType + " Item!");
                                        builder.setContentText("View item here.");
                                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        builder.setSound(alarmSound);
                                        builder.setVibrate(new long[]{1000, 1000});

                                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                                        stackBuilder.addParentStack(ViewPagerActivity.class);
                                        stackBuilder.addNextIntent(intent);
                                        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent);
                                        NotificationManager NM = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                                        NM.notify(0, builder.build());
                                        break outerloop;
                                    }
                                }
                            }
                        }
                    }
                }
                count++;
                if(count==jsonArray.length()&&replacePrev){
                    prevID = itemID;
                    Log.d("timestamp","ITEMID "+Integer.toString(prevID));
                    Log.d("timestamp","currentID "+Integer.toString(prevID));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("ItemData",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String parse = sharedPreferences.getString("jsonData","{server_response:[]}");

        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userIDSharedPrefs = sharedPreferences.getInt("userID",0);
        items = new ArrayList<Item>();
        parseJSON(parse,items,false);
        userItems = new ArrayList<Item>();
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
