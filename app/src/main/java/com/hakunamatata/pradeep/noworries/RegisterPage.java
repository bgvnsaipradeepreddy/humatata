package com.hakunamatata.pradeep.noworries;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pradeep on 16/4/16.
 */
public class RegisterPage extends AppCompatActivity {

    Button buttonRegister;
    Toolbar toolbar;
    EditText etName,etMail,etPhoneno,etPassword;
    TextInputLayout tvName,tvMail,tvPhoneno,tvPassword;
    ProgressBar progressBar;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_register);
        etName = (EditText) findViewById(R.id.etNameregisterpage);
        etMail = (EditText) findViewById(R.id.etEmailregisterpage);
        etPhoneno = (EditText) findViewById(R.id.etPhonenoregisterpage);
        etPassword = (EditText) findViewById(R.id.etPasswordregisterpage);

        buttonRegister = (Button) findViewById(R.id.bRegisterregisterpage);
        toolbar = (Toolbar) findViewById(R.id.tbIncluderegisterpage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_button);

        etName.addTextChangedListener(new GenericTextWatcher(etName));
        etMail.addTextChangedListener(new GenericTextWatcher(etMail));
        etPhoneno.addTextChangedListener(new GenericTextWatcher(etPhoneno));
        etPassword.addTextChangedListener(new GenericTextWatcher(etPassword));
        tvName = (TextInputLayout)findViewById(R.id.tilNameregisterpage);
        tvMail = (TextInputLayout)findViewById(R.id.tilEmailregisterpage);
        tvPhoneno = (TextInputLayout)findViewById(R.id.tilPhonenoregisterpage);
        tvPassword = (TextInputLayout)findViewById(R.id.tilPasswordregisterpage);
        progressBar = (ProgressBar)findViewById(R.id.pbregisterpage);
        progressBar.setVisibility(View.INVISIBLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this,MainPage.class);
                startActivity(intent);

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate_Email;
                validate_Email = validateEmail(etMail.getText().toString());
                if(validate_Email) {

                    try {
                        String name = etName.getText().toString();
                        String email = etMail.getText().toString();
                        String phoneno = etPhoneno.getText().toString();
                        String password = etPassword.getText().toString();


                        RequestPackage requestPackage = new RequestPackage();
                        requestPackage.setMethod("POST");
                        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/register/doregister");
                        requestPackage.setParam("username", name);
                        requestPackage.setParam("useremail", email);
                        requestPackage.setParam("password",password);
                        requestPackage.setParam("phoneno",phoneno);


                        RegisterThread registerThread = new RegisterThread();
                        registerThread.execute(requestPackage);

                    } catch (Exception e) {
                        Dialog d = new Dialog(RegisterPage.this);
                        d.setTitle("Error");
                        TextView tv = new TextView(RegisterPage.this);
                        d.setContentView(tv);
                        d.show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                    builder.setMessage("Invalid Email Address");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
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


    private class RegisterThread extends AsyncTask<RequestPackage,String,String> {

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParser jsonParser = new JsonParser();
            ModelData modelData = new ModelData();

            try {
                modelData = jsonParser.parseRegisterFeed(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (modelData.getRegisterStatus().equals("successful")){

                progressBar.setVisibility(View.INVISIBLE);
                int userId = modelData.getRegisterUserId();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                bundle.putInt("user_id",userId);
                Log.e("pradeepdata", data+userId);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.USERSELECTIONOPTIONS");
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterPage.this, modelData.getRegisterErrorMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
