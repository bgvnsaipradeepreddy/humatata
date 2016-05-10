package com.hakunamatata.pradeep.noworries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.sql.SQLException;


public class MainActivity extends Activity {
    protected void onCreate(Bundle bundledata){
        super.onCreate(bundledata);
        setContentView(R.layout.activity_main);
        //timer for number of seconds to display the title when app starts
        Thread timer = new Thread(){
            public void run() {
                try {
                    sleep(1000);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }finally {
                    //Check if the user already logged in. If he logged in then directly open the page else open registration page
                    SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                    try {
                        sessionManagement.checkLogin();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //Intent i = new Intent("com.hakunamatata.hakunamatata.USEROPTIONWITHVIDEO");
                    //startActivity(i);
                }

            }
        };
        timer.start();
    }

    protected void onPause(){
        super.onPause();
        finish();
    }
}

