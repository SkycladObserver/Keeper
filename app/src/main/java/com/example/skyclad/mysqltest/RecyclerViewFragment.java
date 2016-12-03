package com.example.skyclad.mysqltest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class RecyclerViewFragment extends Fragment {

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
        Log.d("viewPager", data);
    }


    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView =(RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(rootView.getContext(),data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewFragment.RecyclerTouchListener(container.getContext(), recyclerView, new RecyclerViewFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(container.getContext(),data+" onClick "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(container.getContext(),"onLongClick "+position, Toast.LENGTH_SHORT).show();
            }
        }));
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
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}