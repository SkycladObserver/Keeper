/**
 * Module name: ViewPagerActivity
 * Description: ViewPagerActivity is the module that enables the application to use ViewPager as its method of displaying information.
 *              Basically, ViewPager is the view that shifts every time a tab is selected. ViewPager connects all tabs together in
 *              this class.
 * Programmer: Brent Carl Anonas
 * Date Coded: December 3, 2016
 * Module Parameters: None.
 * Variable names:
 *  viewPager - in order to manipulate ViewPager
 *  tabLayout - in order to manipulate tabLayout, which displays the tabs at the top of the app.
 * Files accessed: activity_view_pager.xml, RecyclerViewFragment.java, AccountFragment.java
 * Files updated: RecyclerViewActivity.java edited to RecyclerViewFragment.java because ViewPager can only use Fragments, not
 *               Activities.
 * Module Input: AccountFragment and two different instances of RecyclerViewFragment.
 * Module Output: A View Pager with all the views presented in a user-friendly way.
 * Error handling capabilities: none
 */
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
        int[] tabIcons = {R.drawable.ic_lost,R.drawable.ic_found,R.drawable.ic_account};
        setContentView(R.layout.activity_view_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
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
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    /**
     * this instantiates the fragments to be added to the view pagers. It is done this way in order to add arguments to the bundles.
     * @param viewPager The View Pager upon which the fragments will be added to.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(RecyclerViewFragment.newInstance("Lost"), "Lost");
        adapter.addFragment(RecyclerViewFragment.newInstance("Found"), "Found");
        adapter.addFragment(AccountFragment.newInstance("Account"), "Three");
        viewPager.setAdapter(adapter);
    }
}
