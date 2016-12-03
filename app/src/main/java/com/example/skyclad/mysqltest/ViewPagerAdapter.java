package com.example.skyclad.mysqltest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by acer on 12/2/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mFragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        Log.d("viewPager","retrieving");
        return mFragmentList.get(position);
    }

    @Override public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentListTitles.add(title);
        Log.d("viewPager","added");
        Log.d("viewPager",Integer.toString(mFragmentList.size()));
    }

    @Override public CharSequence getPageTitle(int position) {
         //return mFragmentListTitles.get(position);
        return null;
    }
}

