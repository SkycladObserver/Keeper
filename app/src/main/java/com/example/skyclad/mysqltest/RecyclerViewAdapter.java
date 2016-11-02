package com.example.skyclad.mysqltest;

import android.content.Context;
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
    public RecyclerViewAdapter(Context context,List<User> users){
        inflater = LayoutInflater.from(context);
        this.users = users;
        this.context = context;
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
