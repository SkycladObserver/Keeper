/**
 * Module name: AccountFragment
 * Description: This module handles the view for account fragment.
 * Programmer: Brent Carl Anonas
 * Date Coded: December 4, 2016
 * Module Parameters: None.
 * Variable names:
 *  btn - this is for logout.
 *  data - argument for Account. This is for later use.
 * Files accessed: fragment_account.xml
 * Files updated:
 * Module Input: instantiated in ViewPagerActivity
 * Module Output: a set up account fragment ready for use for the View Pager.
 * Error handling capabilities: none
 */

package com.example.skyclad.mysqltest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountFragment extends Fragment {
    Button btn;
    private static final String ARG = "accountFragment";
    private String data;
    public AccountFragment() {

    }

    public static AccountFragment newInstance(String argument) {
        AccountFragment accountFragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG, argument);
        accountFragment.setArguments(args);
        return accountFragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getString(ARG);
        Log.d("viewPager", data);
    }
    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account,container,false);
        btn = (Button) rootView.findViewById(R.id.butLogout);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("UserData",view.getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(view.getContext(),MainActivity.class));
                ((Activity)view.getContext()).finish();


            }
        });
        return rootView;
    }
}