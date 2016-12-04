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

/**
 * Created by acer on 11/1/2016.
 */

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
    public void delete(int position){
        items.remove(position);
        notifyItemRemoved(position);
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
                   // if(users.size()>getItemCount()){
                    //    notifyItemInserted(0);
                   // }
                    //else if(users.size()<getItemCount()){
                    //    notifyItemRemoved(0);
                    //}
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
