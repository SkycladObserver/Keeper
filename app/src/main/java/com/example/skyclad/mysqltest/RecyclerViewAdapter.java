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
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by acer on 11/1/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    List<User> users = Collections.emptyList();
    private LayoutInflater inflater;
    Context context;
    Handler handler;
    AidlApi api;

    public RecyclerViewAdapter(Context context,List<User> users){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.handler = new Handler();
        startService();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerView", "onCreateViewHolder called");
        View view = inflater.inflate(R.layout.row_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void delete(int position){
        users.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d("RecyclerView", "onBindViewHolder "+position);
        User current = users.get(position);
        holder.name.setText(current.getName());
        holder.uname.setText(current.getUName());
        holder.pass.setText(current.getPass());
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
                    users = api.getUsers();
                    Log.d("ServiceThread","getItemCount: "+getItemCount());
                    notifyItemRangeChanged(0,users.size());
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
        return users.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,uname,pass;
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            uname = (TextView) itemView.findViewById(R.id.uname);
            pass = (TextView) itemView.findViewById(R.id.pass);
            img = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
