
/**
 * Module name: RecyclerViewFragment
 * Description: This module handles the data to be displayed in the recyclerView for the recyclerView fragment.
 *          A recyclerView is an efficient list in android.
 * Programmer: Brent Carl Anonas
 * Date Coded: November 15, 2016
 * Module Parameters:
 *      Context - context of which activity/view this is happening.
 *      Data - Distinguishes whether the recyclerView is for lost or found.
 * Variable names:
 *      private boolean changeDataSet; - checks if it will change the dataset or not. This is used for search where while searching,
 *                              the data set should not refresh. It should only refresh after the search is done.
 *      String data; - Distinguishes whether the recyclerView is for lost or found.
 *      ArrayList<Item> items = new ArrayList<Item>(); - list of items that will be displayed.
 *      ArrayList<Item> itemHolder = new ArrayList<Item>(); - list of all the items. It gets constrained depending on search and type (lost/found)
 *      private LayoutInflater inflater - layoutInflater, as the name implies.
 *      Context context; - context of the class.
 *      Handler handler; - handles multithreading in updating the data coming from the server.
 *      AidlApi api; Access to the Android Interface Definition Language API for Interprocess Communication.
 * Files accessed: AidlApi.aidl, MyService.java, ItemListener.Aidl, Item.Aidl
 * Files updated: none
 * Module Input: Data from the server. Type (lost/found).
 * Module Output: Information to be presented in RecyclerViewFragment.
 * Error handling capabilities: none
 */

package com.example.skyclad.mysqltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    //List<User> users = Collections.emptyList();
    private boolean changeDataSet;
    String data;
    ArrayList<Item> items = new ArrayList<Item>();
    ArrayList<Item> itemHolder = new ArrayList<Item>();
    private LayoutInflater inflater;
    Context context;
    Handler handler;
    AidlApi api;

    public RecyclerViewAdapter(Context context, String data){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.handler = new Handler();
        this.data = data;
        changeDataSet = true;
        startService();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerView", "onCreateViewHolder called");
        View view = inflater.inflate(R.layout.row_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void canChangeDataSet(boolean bool){
        changeDataSet = bool;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d("RecyclerView", "onBindViewHolder "+position);
        Item current = items.get(position);
        holder.name.setText(current.getName());
        holder.description.setText(current.getDescription());
        holder.time.setText(current.getTime());
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

    public void resetData(){
        items = new ArrayList<Item>();
        items.addAll(itemHolder);
        notifyDataSetChanged();
    }
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
                    items = new ArrayList<Item>();
                    itemHolder = new ArrayList<Item>();
                    List<Item> tempItems = api.getItems();
                    for(Item i : tempItems){
                        if ((i.getType()==0 && data.equals("Lost"))||
                                (i.getType()==1 && data.equals("Found"))) {
                            items.add(i);
                            itemHolder.add(i);
                        }
                    }
                    Log.d("viewPager",data);
                    Log.d("viewPager","Users Arraylist size: "+items.size());
                    Log.d("viewPager","getItemCount: "+getItemCount());
                    if(changeDataSet)
                        notifyDataSetChanged();
                    Log.d("search",Boolean.toString(changeDataSet));
                    Log.d("ServiceThread","after notify getItemCount: "+getItemCount());
                } catch (Throwable t) {
                    Log.e("ServiceThread", "Error while updating the UI", t);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,time,description;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.fname);
            time = (TextView) itemView.findViewById(R.id.time);
            description = (TextView) itemView.findViewById(R.id.description);
            img = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public void setFilter(String query){
        ArrayList<Item> newList = new ArrayList<>();
        for(Item i : itemHolder){
            if(i.getName().toLowerCase().contains(query)){
                newList.add(i);
            }
        }
        items = new ArrayList<Item>();
        items.addAll(newList);
        notifyDataSetChanged();
    }
    public Item getItem(int position){
        return items.get(position);
    }
}
