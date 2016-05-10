package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 16/4/16.
 */import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by srinu on 11/14/2015.
 */
public class MainPage extends AppCompatActivity implements SurfaceHolder.Callback,View.OnClickListener {

    MediaPlayer mp;
    Button blogin;
    Button bregister;
    SurfaceView sv = null;
    SurfaceHolder holder;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);

        sv = (SurfaceView)findViewById(R.id.svVideomainpage);
        holder = sv.getHolder();
        holder.addCallback(this);
        mp = new MediaPlayer();
        buttonListener();
        //toolbar = (Toolbar) findViewById(R.id.include_toolbar_video);
        //setSupportActionBar(toolbar);
        blogin.setOnClickListener(this);
        bregister.setOnClickListener(this);

    }

    private void buttonListener() {
        blogin = (Button)findViewById(R.id.bLoginmainpage);
        bregister = (Button)findViewById(R.id.bRegistermainpage);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bgvideo);
        try {
            mp.setDisplay(holder);
            mp.setDataSource(this, video);
            mp.prepare();
        }catch (IOException ie){}

        //Start video
        mp.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mp.release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLoginmainpage:
                Intent login_intent = new Intent("com.hakunamatata.pradeep.noworries.LOGINPAGE");
                startActivity(login_intent);
                break;
            case R.id.bRegistermainpage:
                Intent register_intent = new Intent("com.hakunamatata.pradeep.noworries.REGISTERPAGE");
                startActivity(register_intent);
                break;
        }
    }
}


