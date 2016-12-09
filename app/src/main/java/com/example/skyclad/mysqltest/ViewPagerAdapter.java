/**
 * Module name: ViewPagerAdapter
 * Description: ViewPagerAdapter holds the information of the fragments that will be placed in the ViewPagerActivity. ViewPagerActivity
 *              is just the view. ViewPagerAdapter is the adapter, as implied by the name.
 * Programmer: Brent Carl Anonas
 * Date Coded: December 3, 2016
 * Module Parameters: FragmentManager
 * Variable names:
 * private ArrayList<Fragment> mFragmentList = new ArrayList<>(); - List of Fragments
 * private ArrayList<String> mFragmentListTitles = new ArrayList<>(); - List of Titles
 * Files accessed: activity_view_pager.xml, RecyclerViewFragment.java, AccountFragment.java
 * Files updated: RecyclerViewActivity.java edited to RecyclerViewFragment.java because ViewPager can only use Fragments, not
 *               Activities.
 * Module Input: Fragments to be added to the ViewPager
 * Module Output: The information to be displayed in ViewPagerActivity.
 * Error handling capabilities: none
 */

package com.example.skyclad.mysqltest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mFragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentListTitles.add(title);
        Log.d("viewPager",Integer.toString(mFragmentList.size()));
    }

    @Override public CharSequence getPageTitle(int position) {
        return null;
    }
}

