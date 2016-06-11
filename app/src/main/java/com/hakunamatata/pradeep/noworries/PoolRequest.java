package com.hakunamatata.pradeep.noworries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by pradeep on 29/5/16.
 */
public class PoolRequest extends AppCompatActivity {

    EditText etStart,etEnd, etPersons,etCost,etComment;
    TextInputLayout tilStart,tilEnd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_pool);
        etStart = (EditText) findViewById(R.id.etStartPoolRequest);
        etEnd = (EditText) findViewById(R.id.etEndPoolRequest);


        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoolRequest.this,CarPooling.class);
                Bundle bundle = new Bundle();
                bundle.putString("start_destination","start");
                intent.putExtras(bundle);
                startActivityForResult(intent,1,bundle);
            }
        });

        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoolRequest.this, CarPooling.class);
                Bundle bundle = new Bundle();
                bundle.putString("start_destination", "destination");
                intent.putExtras(bundle);
                startActivityForResult(intent, 2, bundle);
            }
        });
        toolbar = (Toolbar) findViewById(R.id.tbIncludePoolRequest);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lets Ride");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_button);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                etStart.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                etEnd.setText(result);
            }
        }
    }//onActivityResult



}
