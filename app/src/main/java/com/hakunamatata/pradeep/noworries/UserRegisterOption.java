package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 19/4/16.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by srinu on 11/30/2015.
 */
public class UserRegisterOption extends AppCompatActivity implements View.OnClickListener {
    Button office,place;
    Toolbar toolbar;
    int userId;
    Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_register_user);
        bundle = getIntent().getExtras();
        userId = bundle.getInt("user_id");
        toolbar = (Toolbar) findViewById(R.id.tbIncludeUserRegisterOption);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        selectOption();
        office.setOnClickListener(this);
        place.setOnClickListener(this);
    }

    private void selectOption() {
        office =(Button)findViewById(R.id.bOfficesUserRegisterOption);
        place=(Button)findViewById(R.id.bPlacesUserRegisterOption);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bOfficesUserRegisterOption:
                bundle.putInt("user_id",userId);
                //bundle.putString("data","pradeep");
                Intent place_intent = new Intent("com.hakunamatata.pradeep.noworries.USERSELECTPLACES");
                place_intent.putExtras(bundle);
                startActivity(place_intent);

                break;
            case R.id.bPlacesUserRegisterOption:

                bundle.putInt("user_id",userId);
                //bundle.putString("data", "pradeep");
                Intent office_intent = new Intent("com.hakunamatata.pradeep.noworries.USERSELECTPLACES");
                office_intent.putExtras(bundle);
                startActivity(office_intent);
                break;

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
