/**
 * Module name: RecyclerViewFragment
 * Description: This module handles the view for recyclerView fragment. A recyclerView is an efficient list in android.
 * Programmer: Brent Carl Anonas
 * Date Coded: December 4, 2016
 * Module Parameters: None.
 * Variable names:
 * private Toolbar toolbar - This is for the search bar.
 * private RecyclerView recyclerView - the list
 * private RecyclerViewAdapter recyclerViewAdapter - the adapter that handles the information to be placed in each list item.
 * private static final String ARG = "recyclerViewFragment" - the key in order to retrieve the argument. This is used to distinguish
 *                                  if the instantiated class is of lost or found.
 * private String data - data is the value accessed through ARG.
 * Files accessed: RecyclerViewAdapter.java, fragment_recycler_view.xml
 * Files updated:
 * Module Input: instantiated in ViewPagerActivity
 * Module Output: a set up recyclerView fragment ready for use for the View Pager.
 * Error handling capabilities: none
 */

package com.example.skyclad.mysqltest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewFragment extends Fragment implements SearchView.OnQueryTextListener{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private static final String ARG = "recyclerViewFragment";
    private String data;
    public RecyclerViewFragment() {

    }

    public static RecyclerViewFragment newInstance(String argument) {
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG, argument);
        recyclerViewFragment.setArguments(args);
        return recyclerViewFragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getString(ARG);
        setHasOptionsMenu(true);
        Log.d("viewPager", data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        if (menuItem != null) {
            MenuItemCompat.setOnActionExpandListener(menuItem,new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    recyclerViewAdapter.canChangeDataSet(false);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    recyclerViewAdapter.canChangeDataSet(true);
                    recyclerViewAdapter.resetData();
                    return true;
                }
            });
            //MenuItemCompat.setActionView(menuItem, mSearchView);
        }
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Search...");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        recyclerView =(RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(rootView.getContext(),data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewFragment.RecyclerTouchListener(container.getContext(), recyclerView, new RecyclerViewFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(container.getContext(),data+" onClick "+position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(),ItemInformationActivity.class);
                Item i = recyclerViewAdapter.getItem(position);
                String type = i.getType()==0 ? "Lost" : "Found";

                intent.putExtra("type",type);
                intent.putExtra("title",i.getName());
                intent.putExtra("description",i.getDescription());
                intent.putExtra("location",i.getLocation());
                intent.putExtra("time",i.getTime());
                intent.putExtra("email",i.getEmail());
                intent.putExtra("uploader",i.getUploader());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(container.getContext(),"onLongClick "+position, Toast.LENGTH_SHORT).show();
            }
        }));
        FloatingActionButton myFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddItemActivity.class);
                intent.putExtra("type",data);
                startActivity(intent);
            }
        });
        return rootView;

    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private RecyclerViewFragment.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewFragment.ClickListener clickListener){
            Log.d("onTouchEventListener","constructor invoked");
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d("onTouchEventListener","single on tap up");
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null&&clickListener!=null){
                        clickListener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                    Log.d("onTouchEventListener","on long press");
                    super.onLongPress(e);
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null&&clickListener!=null&&gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child,recyclerView.getChildAdapterPosition(child));
            }
            Log.d("onTouchEventListener","on intercept event");
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("onTouchEventListener","on touch event");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        recyclerViewAdapter.setFilter(newText);
        return true;
    }
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}