package com.hakunamatata.pradeep.noworries;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pradeep on 26/4/16.
 */
public class UserEntities extends AppCompatActivity {

    int userId;
    String selectedPlace;
    int placeId;
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entities_user);


        final Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userid");
        placeId = bundle.getInt("placeId");
        selectedPlace = bundle.getString("place");

        toolbar = (Toolbar) findViewById(R.id.tbIncludeUserEntities);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tlUserEntities);
        viewPager = (ViewPager) findViewById(R.id.vpUserEntities);


        tabLayout.addTab(tabLayout.newTab().setText("Ask"));
        tabLayout.addTab(tabLayout.newTab().setText("Emergency Help"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Deals"));
        tabLayout.addTab(tabLayout.newTab().setText("ChatRoom"));
        tabLayout.addTab(tabLayout.newTab().setText("Trivia"));
        tabLayout.addTab(tabLayout.newTab().setText("Lets Go"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
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
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tvTabLayout);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }


        viewPager.setCurrentItem(0,false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
//                    return FragmentQueries.newInstance(userId,selectedPlace);
                    return FragmentQuery.newInstance(userId,placeId,selectedPlace);
                case 1:
//                    return FragmentQueries.newInstance(userId,selectedPlace);
                    return FragmentEmergencyHelp.newInstance(userId,placeId,selectedPlace);
                case 2:
                    //return FragmentClimate.newInstance(userId,selectedPlace);
                    return FragmentEvent.newInstance(userId,placeId,selectedPlace);
                case 3:
                    //return FragmentEvent.newInstance(userId,selectedPlace);
                    return FragmentDeals.newInstance(userId,placeId,selectedPlace);
                case 4:
                    //return FragmentDeals.newInstance(userId,selectedPlace);
                    return FragmentGroupChat.newInstance(userId,placeId,selectedPlace);
                case 5:
                    //return FragmentChatRoom.newInstance(userId,selectedPlace);
                    return FragmentClimate.newInstance(userId,placeId,selectedPlace);
                case 6:
                    //return FragmentChatRoom.newInstance(userId,selectedPlace);
                    return FragmentLetsGo.newInstance(userId,placeId,selectedPlace);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
