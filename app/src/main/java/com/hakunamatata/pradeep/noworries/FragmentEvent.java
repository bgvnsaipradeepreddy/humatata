package com.hakunamatata.pradeep.noworries;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by srinu on 1/1/2016.
 */
public class FragmentEvent extends Fragment {


    android.os.Handler handle = new Handler();
    TextView tv1,tv2,tv3,tv4;

    String location;
    View view;
    int userId,placeId;
    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<String> days = new ArrayList<String>();
    ArrayList<String> dateString = new ArrayList<String>();
    public static FragmentEvent newInstance(int userId,int placeId,String location){

        FragmentEvent fragment = new FragmentEvent();
        Bundle args = new Bundle();
        args.putString("place",location);
        args.putInt("place_id", placeId);
        args.putInt("user_id",userId);
        Log.e("didan","didan"+userId+placeId);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEvent() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        location = getArguments().getString("place");
        userId = getArguments().getInt("user_id");
        placeId = getArguments().getInt("place_id");

        view=inflater.inflate(R.layout.event_fragment, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.vpFragmentEvent);
//        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tlFragmentEvent);
//        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();





        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        //days.add(dateFormat(date));

        for (int j=15;j>0;j--) {
            cal.add(Calendar.DAY_OF_MONTH, 1-j);
            Date x = cal.getTime();
            days.add(dateFormat(x));
            dateString.add(dateStringFormat(x));
            cal = Calendar.getInstance();
        }

        for (int j=1;j<15;j++){
            cal.add(Calendar.DAY_OF_MONTH,j);
            Date x = cal.getTime();
            days.add(dateFormat(x));
            dateString.add(dateStringFormat(x));
            cal=Calendar.getInstance();
        }
        for(int i=0;i<days.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(days.get(i)));
        }



        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter adapter = new PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.date_tab_layout, tabLayout, false);

            TextView dateTextView = (TextView) relativeLayout.findViewById(R.id.tvDate);
            TextView  monthTextView= (TextView) relativeLayout.findViewById(R.id.tvMonth);
            TextView weekTextView = (TextView) relativeLayout.findViewById(R.id.tvWeek);
            String ex = tab.getText().toString();

            String[] parts = ex.split("-");
            String datetext = parts[0];
            String monthtext = parts[1];
            String weektext = parts[2];

            dateTextView.setText(datetext);
            monthTextView.setText(monthtext);
            weekTextView.setText(weektext);

            tab.setCustomView(relativeLayout);
            tab.select();
            if(tabLayout.getTabCount()>15){
                tabLayout.setClickable(false);
            }
        }
        viewPager.setCurrentItem(14,false);
        return view;
    }

    public static  String dateFormat(Date requiredDate){
        String date = new SimpleDateFormat("dd-MM-EE").format(requiredDate);
        return date;

    }


    public static  String dateStringFormat(Date requiredDate){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(requiredDate);
        return date;

    }


    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            Log.e("date", "disdate" + userId+placeId);

            return FragmentEventResult.newInstance(userId,placeId,location,dateString.get(position));
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
