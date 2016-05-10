package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 24/4/16.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
public class FragmentMain extends Fragment {

    ArrayList<String> selectedPlaces = new ArrayList<String>();
    int userId;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public static FragmentMain newInstance(int userId){

        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt("userid",userId);
        fragment.setArguments(args);
        return fragment;
    }


    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);


        selectedPlaces=getArguments().getStringArrayList("place");
        userId=getArguments().getInt("userid");


        viewPager = (ViewPager) rootView.findViewById(R.id.vpFragmentMain);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tlFragmentMain);



        //setupViewPagerInfo(viewPager);
        //tabLayout.setupWithViewPager(viewPager);


        tabLayout.addTab(tabLayout.newTab().setText("MY PLACES"));
        tabLayout.addTab(tabLayout.newTab().setText("MY OFFICES"));

        //viewPager.setCurrentItem(0);


        PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
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
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab_layout, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tvTabLayout);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }


        viewPager.setCurrentItem(0, false);



        // Inflate the layout for this fragment
        return rootView;
    }


    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {

            super(fm);
            Log.e("pradeeptab1", "pra");
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Log.e("pradeeptab2", "pra");
                    return  FragmentPlaces.newInstance(userId);
                case 1:
                    Log.e("pradeeptab3", "pra");
                    return FragmentPlaces.newInstance(userId);
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
