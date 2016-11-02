package com.example.skyclad.mysqltest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 10/29/2016.
 */

public class UserAdapter extends ArrayAdapter{
    List list = new ArrayList();
    public UserAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(User object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        UserHolder userHolder;
        if (row==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            userHolder = new UserHolder();
            userHolder.name = (TextView) row.findViewById(R.id.name);
            userHolder.uname = (TextView) row.findViewById(R.id.uname);
            userHolder.pass = (TextView) row.findViewById(R.id.pass);
            row.setTag(userHolder);
        }
        else{
            userHolder = (UserHolder)row.getTag();
        }
        User user = (User) this.getItem(position);
        userHolder.name.setText(user.getName());
        userHolder.uname.setText(user.getUName());
        userHolder.pass.setText(user.getPass());
        return row;
    }

    static class UserHolder{
        TextView name,uname,pass;

    }
}

