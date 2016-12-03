package com.example.skyclad.mysqltest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends Fragment {

    private static final String ARG_EXAMPLE = "accountFragment";
    private String example_data;
    public AccountFragment() {

    }

    public static AccountFragment newInstance(String argument) {
        AccountFragment accountFragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE, argument);
        accountFragment.setArguments(args);
        return accountFragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        example_data = getArguments().getString(ARG_EXAMPLE);
        Log.i("Fragment created with ", example_data);
    }


    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //changed here
        return inflater.inflate(R.layout.fragment_account,container,false);

    }
}