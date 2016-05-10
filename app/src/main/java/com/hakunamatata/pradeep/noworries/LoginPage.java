package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 16/4/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.MenuInflater;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srinu on 11/11/2015.
 */
public class LoginPage extends AppCompatActivity {

    String email = "pradeepreddy.chinna@gmail.com";
    String message= "no password  lol";
    Bundle bundle = new Bundle();
    Button loginButton;
    TextInputLayout emaildisplay,passworddisplay;
    TextView tv;
    ProgressBar progressBar;
    Toolbar toolbar;
    String data;
    ArrayList<String> places = new ArrayList<String>();
    EditText etmail;
    EditText etpassword;
    String mailtostring;
    String passwordtostring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_login);
        loginButton = (Button)findViewById(R.id.bLoginloginpage);
        tv = (TextView) findViewById(R.id.bForgotpasswordloginpage);
        toolbar = (Toolbar) findViewById(R.id.tbIncludeloginpage);
        etmail = (EditText) findViewById(R.id.etEmailloginpage);
        emaildisplay=(TextInputLayout)findViewById(R.id.tvEmailloginpage);
        passworddisplay=(TextInputLayout)findViewById(R.id.tvPasswordloginpage);
        etpassword = (EditText) findViewById(R.id.etPasswordloginpage);
        progressBar = (ProgressBar) findViewById(R.id.pbLoginpage);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_button);
        progressBar.setVisibility(View.INVISIBLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, MainPage.class);
                startActivity(intent);

            }
        });

        etmail.addTextChangedListener(new GenericTextWatcher(etmail));
        etpassword.addTextChangedListener(new GenericTextWatcher(etpassword));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validate_Email;
                validate_Email = validateEmail(etmail.getText().toString());
                mailtostring = etmail.getText().toString();
                passwordtostring = etpassword.getText().toString();
                String retrivedData = "";
                if ((mailtostring.equals("")) || (passwordtostring.equals(""))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                    builder.setMessage("Enter both Email and password");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    if (validate_Email) {

                        try {
                            RequestPackage requestPackage = new RequestPackage();
                            requestPackage.setMethod("GET");
                            requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/login/dologin");
                            requestPackage.setParam("useremail",mailtostring);
                            requestPackage.setParam("password",passwordtostring);
                            LoginThread loginThread = new LoginThread();
                            loginThread.execute(requestPackage);

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                        builder.setMessage("Invalid Email Address");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });



        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mail_intent = new Intent(android.content.Intent.ACTION_SEND);
                mail_intent.setType("application/octet-stream");
                mail_intent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
                mail_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "YOUR PASSWORD");
                mail_intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                if (mail_intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mail_intent);
                    tv.setText("Password sent to mail");
                } else {
                    tv.setText("No Email App for sending email");
                }
            }
        });
    }

    private boolean validateEmail(final String emailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailAddress);
        return matcher.matches();

    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence s, int start, int before, int count) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable editable) {

        }
    }
    private boolean validateEmail() {
        String email = etmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            emaildisplay.setError("Enter Valid email address");
            requestFocus(etmail);
            return false;
        } else {
            emaildisplay.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class LoginThread extends AsyncTask<RequestPackage,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            data = httpManager.getData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            String output = null;
            ModelData modelData = new ModelData();
            JsonParser jsonParser = new JsonParser();
            try {
                modelData = jsonParser.parseLoginFeed(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (modelData.getLoginStatus().equals("successful")){


                progressBar.setVisibility(View.INVISIBLE);
                int userId = modelData.getLoginUserId();
                Log.e("pradeepuserid", "pradeep" + userId);

                SessionManagement sessionManagement = new SessionManagement(LoginPage.this);
                sessionManagement.createLoginSession(mailtostring, passwordtostring,userId);
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                bundle.putInt("user_id",userId);
                Log.e("pradeepdata", data);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.USERSELECTIONOPTIONS");
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginPage.this,modelData.getRegisterErrorMsg(),Toast.LENGTH_LONG).show();
            }

        }
    }

}
