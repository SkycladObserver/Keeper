package com.example.skyclad.mysqltest;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int tabIcon = R.drawable.ic_sheep_silhouette;
        //int tabIcon = 1;
        setContentView(R.layout.activity_view_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        for(int i = 0; i<3;i++){
            tabLayout.getTabAt(i).setIcon(tabIcon);
        }
        Log.d("viewPager","before getTabAt");
        //tabLayout.getTabAt(0).setIcon(tabIcon);
        Log.d("viewPager","after getTabAt");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AccountFragment.newInstance("this data is for fragment 1"), "One");
        adapter.addFragment(AccountFragment.newInstance("this data is for fragment 2"), "Two");
        adapter.addFragment(AccountFragment.newInstance("this data is for fragment 3"), "Three");
        //adapter.addFragment(TabFragmentTwo.newInstance("this data is for fragment 2"), "Two");
        //adapter.addFragment(TabFragmentThree.newInstance("this data is for fragment 3"), "Three");
        viewPager.setAdapter(adapter);
    }
}
