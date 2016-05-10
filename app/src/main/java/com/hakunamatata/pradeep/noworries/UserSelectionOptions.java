package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 24/4/16.
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * Created by srinu on 12/19/2015.
 */
public class UserSelectionOptions extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    FrameLayout frameLayout;

    private ListView mDrawerList;
    private ViewPager viewPager;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawer;
    NavigationView nvDrawer;
    private String[] mNavTitle;
    private int[] mNavImage={R.drawable.myprofile,R.drawable.mybookmark,R.drawable.mychats,R.drawable.myquestions,R.drawable.logout};
    ActionBarDrawerToggle drawerToggle;
    int userId;
    ArrayList<String> selectedPslaces = new ArrayList<String>();
    ArrayList<String> selectedOffices = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_selection_user);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("user_id");
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentMain fragmentMain = FragmentMain.newInstance(userId);
        fragmentTransaction.add(R.id.fldlUserSelectionOptions, fragmentMain).addToBackStack("firstFragment");
        fragmentTransaction.commit();


        mTitle = mDrawerTitle = getTitle();
        mDrawer = (DrawerLayout) findViewById(R.id.dlUserSelectionOptions);
        frameLayout=(FrameLayout)findViewById(R.id.fldlUserSelectionOptions);

        toolbar = (Toolbar) findViewById(R.id.tbIncludeUserSelectionOptions);
        setSupportActionBar(toolbar);

        mNavTitle = getResources().getStringArray(R.array.nav_drawer_labels);


        //nvDrawer = (NavigationView) findViewById(R.id.nvView);
        mDrawerList = (ListView) findViewById(R.id.lvdlUserSelectionOptions);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);

        PopulateNavigationMenu populateNavigationMenu =new PopulateNavigationMenu(UserSelectionOptions.this,mNavTitle,mNavImage);
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, mNavTitle));
        mDrawerList.setAdapter(populateNavigationMenu);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            onBackPressed();
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            selectItem(position);
        }
    }




    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Intent intent;
        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt("userid", userId);

                intent = new Intent("com.hakunamatata.pradeep.noworries.USERPROFILE");
                intent.putExtras(bundle);
                mDrawerList.setItemChecked(position, true);
                mDrawer.closeDrawer(mDrawerList);
                //setTitle(mNavTitle[position]);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent("com.hakunamatata.hakunamatata.BOOKMARKS");
                mDrawerList.setItemChecked(position, true);
                mDrawer.closeDrawer(mDrawerList);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent("com.hakunamatata.hakunamatata.USERQUESTIONS");
                mDrawerList.setItemChecked(position, true);
                mDrawer.closeDrawer(mDrawerList);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent("com.hakunamatata.hakunamatata.USERCHATS");
                mDrawerList.setItemChecked(position, true);
                mDrawer.closeDrawer(mDrawerList);
                startActivity(intent);
                break;
            case 4:
                SessionManagement sessionManagement = new SessionManagement(UserSelectionOptions.this);
                sessionManagement.logoutUser();
                break;
            default:
                break;
        }




    }
}

