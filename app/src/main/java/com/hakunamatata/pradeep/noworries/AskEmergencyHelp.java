package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 5/5/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srinu on 2/3/2016.
 */
public class AskEmergencyHelp extends AppCompatActivity {
    Toolbar toolbar;
    EditText etaskquery,etTitleQuery;
    ImageButton ibhyperlink,ibattachimage;
    int RESULT_LOAD_IMG=1;
    String imgDecodableString;
    Map<String,String> data = new HashMap<>();
    LinearLayout focusText,buttonVisible;
    ImageButton boldButton,italicButton,underlineButton,exampleButton;
    ProgressBar pb;
    Button bDone;
    int userId,placeId;
    String location;
    int mStart=-1;
    int italicStart=-1;
    int underLineStart=-1;
    boolean bold=false;
    boolean italic = false;
    boolean underline = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_ask);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("user_id");
        Log.e("debug11","debug11"+userId);
        placeId = bundle.getInt("place_id");
        location = bundle.getString("place");
        toolbar = (Toolbar) findViewById(R.id.tbIncludeAskQuery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        etaskquery = (EditText)findViewById(R.id.etQueryAskQuery);
        etTitleQuery = (EditText) findViewById(R.id.etTitleAskQuery);
        ibattachimage=(ImageButton)findViewById(R.id.ibImageAskQuery);
        ibhyperlink = (ImageButton) findViewById(R.id.ibHyperlinkAskQuery);
        boldButton = (ImageButton) findViewById(R.id.ibBoldAskQuery);
        italicButton= (ImageButton) findViewById(R.id.ibItalicAskQuery);
        underlineButton = (ImageButton) findViewById(R.id.ibUnderlineAskQuery);
        focusText = (LinearLayout)findViewById(R.id.llAskQuery);
        buttonVisible = (LinearLayout)findViewById(R.id.trAskQuery);
        bDone = (Button) findViewById(R.id.bDone);
        pb = (ProgressBar) findViewById(R.id.pbAskQuery);



        focusText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etaskquery.requestFocus();
                etaskquery.setFocusableInTouchMode(true);
                buttonVisible.setVisibility(View.VISIBLE);
                return false;
            }
        });


        etaskquery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    buttonVisible.setVisibility(View.VISIBLE);
                } else {
                    buttonVisible.setVisibility(View.GONE);
                }
            }
        });



        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryTitle = etTitleQuery.getText().toString();
                String mainQuery = etaskquery.getText().toString();
                data.put("user_id",Integer.toString(userId));
                data.put("place_id",Integer.toString(placeId));
                data.put("title",queryTitle);
                data.put("query",mainQuery);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                //get current date time with Date()MM
                Date date = new Date();
                data.put("date",dateFormat.format(date));
                JsonUtility jsonUtility = new JsonUtility();
                String queryData=null;
                try {
                    queryData = jsonUtility.constructJSONEmergency(data);
                    Log.e("querydata",queryData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod("POST");
                requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/emergency/insertemergencyhelp");
                requestPackage.setParam("query_data", queryData);

                AskQueryThread askQueryThread = new AskQueryThread();
                askQueryThread.execute(requestPackage);

            }
        });

/**
 ibhyperlink.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
AlertDialog.Builder builder = new AlertDialog.Builder(AskQuery.this);
View view= getLayoutInflater().inflate(R.layout.add_hyperlink,null);
final EditText hyperlink = (EditText) view.findViewById(R.id.ethyperlink);
builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int id) {
//  Your code when user clicked on Cancel
dialog.dismiss();
}
});

builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int id) {
//  Your code when user clicked on Cancel
addHyperlink(hyperlink);
}
});
final AlertDialog dialog= builder.create();


dialog.setView(view);
dialog.show();


}
});
 */

        ibattachimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AskEmergencyHelp.this);
                builder.setTitle(Html.fromHtml("<font color='#c24946'>Select Option</font>"));
                View view= getLayoutInflater().inflate(R.layout.upload_options,null);
                Button pickImage = (Button)view.findViewById(R.id.bGallery);
                Button pickCamera = (Button)view.findViewById(R.id.bCamera);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog= builder.create();
                pickCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 100);
                    }
                });
                pickImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                        startActivityForResult(i, RESULT_LOAD_IMG);


                    }
                });

                dialog.setView(view);
                dialog.show();

            }


        });





        etaskquery.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int currentLoc = etaskquery.getSelectionStart();
                /**if(mStart>currentLoc || italicStart>currentLoc){
                 mStart=currentLoc;
                 italicStart=currentLoc;
                 }*/


                if (bold == true) {
                    int end = etaskquery.getSelectionEnd();
                    Log.e("pradeepstarterror", "bold-----mStart" + mStart + "end" + end);
                    Spannable spn = (Spannable) etaskquery.getText();
                    if (end == 0) end = 1;
                    spn.setSpan(new StyleSpan(Typeface.BOLD), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (bold == false) {
                    if (mStart == -1) mStart = 0;
                    int end = etaskquery.getSelectionEnd();
                    Log.e("pradeepstarterror", "not bold end" + end);
                    Spannable spn2 = (Spannable) etaskquery.getText();
                    if (end == 0) end = 1;
                    spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                if (italic == true) {
                    int end = etaskquery.getSelectionEnd();
                    Spannable spn = (Spannable) etaskquery.getText();
                    Log.e("pradeepstarterror", "italic end" + end);
                    if (end == 0) end = 1;
                    spn.setSpan(new StyleSpan(Typeface.ITALIC), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (italic == false) {
                    if (italicStart == -1) italicStart = 0;
                    int end = etaskquery.getSelectionEnd();
                    Spannable spn2 = (Spannable) etaskquery.getText();
                    Log.e("pradeepstarterror", "not italic end" + end);
                    if (end == 0) end = 1;
                    spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }


                if (underline == true) {
                    int end = etaskquery.getSelectionEnd();
                    UnderlineSpan[] ss = s.getSpans(end - 1, end, UnderlineSpan.class);
                    Spannable spn = (Spannable) etaskquery.getText();
                    Log.e("pradeepstarterror", " underline end" + end);
                    if (end == 0) end = 1;
                    spn.setSpan(new UnderlineSpan(), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (underline == false) {
                    int end = etaskquery.getSelectionEnd();
                    Spannable spn2 = (Spannable) etaskquery.getText();
                    if (end == 0) end = 1;
                    spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //unused
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //unused


            }

        });

        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bold == false) {
                    //mStart = Selection.getSelectionStart(etaskquery.getText());
                    bold = true;
                } else {
                    bold = false;
                    //mStart = Selection.getSelectionStart(etaskquery.getText());
                    //mStart = etaskquery.getText().length();
                }
            }
        });
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (italic==false) {
                    //italicStart = Selection.getSelectionStart(etaskquery.getText());
                    italic=true;
                } else {
                    italic=false;
                    //italicStart = Selection.getSelectionStart(etaskquery.getText());
                    //mStart = etaskquery.getText().length();
                }
            }
        });
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (underline == false) {
                    underLineStart = Selection.getSelectionStart(etaskquery.getText());
                    underline = true;
                } else {
                    underline = false;
                    underLineStart = Selection.getSelectionStart(etaskquery.getText());
                    //mStart = etaskquery.getText().length();
                }
            }
        });

    }


    private void addImageBetweentext(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = etaskquery.getSelectionStart();
        etaskquery.getText().insert(selectionCursor, "\n.");

        selectionCursor = etaskquery.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(etaskquery.getText());
        Log.e("pradeepstarterror",selectionCursor+"selectioncursor");
        builder.setSpan(new ImageSpan(drawable),selectionCursor-1, selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        etaskquery.setText(builder);
        etaskquery.setSelection(selectionCursor);
        int selectionline = etaskquery.getSelectionStart();
        etaskquery.getText().insert(selectionline, "\n");
    }

    private void addHyperlink(EditText editText){

        String hyperlink ="<ahref='"+editText.getText().toString()+"'>"+editText.getText().toString()+"</a" ;
        int selectionCursor = etaskquery.getSelectionStart();
        etaskquery.getText().insert(selectionCursor, " ");
        int selectionCursor1 = etaskquery.getSelectionStart();
        etaskquery.getText().insert(selectionCursor1, Html.fromHtml(hyperlink));
        int selectionline = etaskquery.getSelectionStart();
        etaskquery.getText().insert(selectionline, " ");

        selectionCursor = etaskquery.getSelectionStart();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
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
            Bitmap bt = Bitmap.createScaledBitmap(yourSelectedImage, 600, 400, false);

            Drawable drawable =new BitmapDrawable(getResources(),bt);
            addImageBetweentext(drawable);


        }
        else if(requestCode == 100 && resultCode== RESULT_OK && data != null){

            //Bitmap bt = Bitmap.createScaledBitmap(yourSelectedImage,600,400,false);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap bt = Bitmap.createScaledBitmap(photo,600,400,false);
            Drawable drawable =new BitmapDrawable(getResources(),bt);
            Log.e("pradeepimage2","pradeep");
            addImageBetweentext(drawable);
        }

    }



    Html.ImageGetter imageGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String filePath) {
            Drawable d = Drawable.createFromPath(filePath);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    };


    private class AskQueryThread extends AsyncTask<RequestPackage,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String jsonOutput;
            Log.e("inplaces6","inplaces6");
            HttpManager httpManager = new HttpManager();
            jsonOutput = httpManager.getData(params[0]);
            Log.e("inplaces7","inplaces7");
            Log.e("data",jsonOutput);
            return jsonOutput;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AskEmergencyHelp.this,"Query has been successfully posted",Toast.LENGTH_LONG).show();
            finish();


        }
    }
}
