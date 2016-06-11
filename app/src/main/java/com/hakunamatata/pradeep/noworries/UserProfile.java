package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 29/4/16.
 */


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srinu on 11/29/2015.
 */
public class UserProfile extends AppCompatActivity {

    Toolbar toolbar;
    EditText name,biography,profession,phone,age,description;
    int RESULT_LOAD_IMG_CAMERA=1;
    int RESULT_LOAD_IMG_GALLERY=2;
    String imageResource;
    ImageButton userProfile ;
    TextInputLayout phonedisplay,namedisplay,agedisplay,professiondisplay,biodisplay,descriptiondisplay;
    TextView wordcount;
    RadioGroup radioGroup;
    RadioButton male,female;
    Button bDone;
    int userId;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userid");

        userProfile = (ImageButton) findViewById(R.id.ibUserProfile);
        name = (EditText)findViewById(R.id.etNameUserProfile);
        biography=(EditText)findViewById(R.id.etBiographyUserProfile);
        profession = (EditText)findViewById(R.id.etProfessionUserProfile);
        description=(EditText)findViewById(R.id.etDescriptionUserProfile);
        phone = (EditText)findViewById(R.id.etPhoneNoUserProfile);
        age=(EditText)findViewById(R.id.etAgeUserProfile);
        wordcount = (TextView)findViewById(R.id.tvCountUserProfile);
        phonedisplay=(TextInputLayout)findViewById(R.id.tvPhoneNoUserProfile);
        namedisplay=(TextInputLayout)findViewById(R.id.tvNameUserProfile);
        agedisplay=(TextInputLayout)findViewById(R.id.tvAgeUserProfile);
        biodisplay=(TextInputLayout)findViewById(R.id.tvBiographyUserProfile);
        descriptiondisplay=(TextInputLayout)findViewById(R.id.tvDescriptionUserProfile);
        professiondisplay=(TextInputLayout)findViewById(R.id.tvProfessionUserProfile);
        bDone = (Button) findViewById(R.id.bDone);
        radioGroup = (RadioGroup) findViewById(R.id.rgUserProfile);

        name.setText(username);

        toolbar = (Toolbar) findViewById(R.id.tbIncludeUserProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_button);
        biography.addTextChangedListener(new GenericTextWatcher(biography));
        phone.addTextChangedListener(new GenericTextWatcher(phone));
        age.addTextChangedListener(new GenericTextWatcher(age));
        profession.addTextChangedListener(new GenericTextWatcher(profession));
        description.addTextChangedListener(new GenericTextWatcher(description));
        male = (RadioButton) findViewById(R.id.rbMaleUserProfile);
        female = (RadioButton) findViewById(R.id.rbFemaleUserProfile);


        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/profile/getuserprofile");
        requestPackage.setParam("user_id", String.valueOf(userId));
        new GetProfileThread().execute(requestPackage);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                builder.setTitle(Html.fromHtml("<font color='#c24946'>Select Option</font>"));
                View view = getLayoutInflater().inflate(R.layout.upload_options, null);
                Button pickImage = (Button) view.findViewById(R.id.bGallery);
                Button pickCamera = (Button) view.findViewById(R.id.bCamera);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = builder.create();
                pickCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, RESULT_LOAD_IMG_CAMERA);
                    }
                });
                pickImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                        startActivityForResult(i, RESULT_LOAD_IMG_GALLERY);


                    }
                });

                dialog.setView(view);
                dialog.show();

            }


        });


        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userBio=biography.getText().toString();
                String userProffession = profession.getText().toString();
                String userDescription = description.getText().toString();
                String userAge = age.getText().toString();


                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod("POST");
                requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/profile/updateuserprofile");
                requestPackage.setParam("user_bio", userBio);
                requestPackage.setParam("user_proffession",userProffession);
                requestPackage.setParam("user_description",userDescription);
                requestPackage.setParam("user_age",userAge);
                requestPackage.setParam("user_id",String.valueOf(userId));
                if(male.isChecked()){
                    requestPackage.setParam("user_gender","m");
                }
                else if(female.isChecked()){
                    requestPackage.setParam("user_gender","f");
                }else {
                    requestPackage.setParam("user_gender",null);
                }
                requestPackage.setParam("user_profile",imageResource);

                ProfileThread profileThread = new ProfileThread();
                profileThread.execute(requestPackage);

            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == RESULT_LOAD_IMG_GALLERY && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            //Drawable drawable=imageGetter.getDrawable(imgDecodableString);
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(imagePath);
            Bitmap bt = Bitmap.createScaledBitmap(yourSelectedImage, 150, 150, false);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bt.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            imageResource = Base64.encodeToString(byte_arr, Base64.DEFAULT);

            Drawable drawable =new BitmapDrawable(getResources(),bt);
            userProfile.setImageBitmap(bt);


        }
        else if(requestCode == RESULT_LOAD_IMG_CAMERA && resultCode== RESULT_OK && data != null){

            //Bitmap bt = Bitmap.createScaledBitmap(yourSelectedImage,600,400,false);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap bt = Bitmap.createScaledBitmap(photo,150,150,false);
            Drawable drawable =new BitmapDrawable(getResources(),bt);
            Log.e("pradeepimage2", "pradeep");
            userProfile.setImageBitmap(bt);
        }

    }



    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int before, int count) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch(view.getId()) {
                case R.id.etBiographyUserProfile:
                    wordcount.setText(String.valueOf(120 - s.length()));

                    break;
            }
        }

        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }


    private class ProfileThread extends AsyncTask<RequestPackage,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            String data = httpManager.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result ="";
            JsonParser jsonParser = new JsonParser();
            try {
               result  = jsonParser.parseUserProfileFeed(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result.equals("successful")){
                Toast.makeText(UserProfile.this,"Profile has been successfully updated",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(UserProfile.this,"Please try after sometime",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetProfileThread extends AsyncTask<RequestPackage,String,String>{


        @Override
        protected String doInBackground(RequestPackage... params)
        {
            HttpManager httpManager = new HttpManager();
            String data = httpManager.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParser jsonParser = new JsonParser();
            List<String> result = new ArrayList<>();
            try {
                result  = jsonParser.parseUserProfileData(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result.get(0) == null){

            }else {
                name.setText(result.get(0));
            }
            if(result.get(1) == null){

            }else {
                phone.setText(result.get(1));
            }
            if(result.get(2) == "m"){
                male.setSelected(true);
                female.setSelected(false);
            }else if(result.get(2) == "f"){
                male.setSelected(false);
                female.setSelected(true);
            }else {
                male.setSelected(false);
                female.setSelected(false);
            }
            if(result.get(3) == null){

            }else {
                age.setText(result.get(3));
            }

            if(result.get(4) == "") {
            }else {
                biography.setText(result.get(4));
            }

            if(result.get(5) == null){

            }else {
                profession.setText(result.get(5));
            }
            if(result.get(6) == null){

            }else {
                description.setText(result.get(6));
            }
            Bitmap decodedByte;

            if(result.get(7) == null){
                decodedByte = BitmapFactory.decodeResource(UserProfile.this.getResources(),
                        R.drawable.myprofile_black);
            }else {
                byte[] decodedString = Base64.decode(result.get(7), Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            }
            userProfile.setImageBitmap(decodedByte);
        }
    }
}

