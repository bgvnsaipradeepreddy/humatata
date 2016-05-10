package com.hakunamatata.pradeep.noworries;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by pradeep on 17/4/16.
 */
public class UserHomePage extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home_user);
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");

        int userId = bundle.getInt("user_id");
        Log.e("pradeepuserid","pradeep"+userId);
        TextView textView = (TextView) findViewById(R.id.outputtext);
        textView.setText(data);
    }
}
