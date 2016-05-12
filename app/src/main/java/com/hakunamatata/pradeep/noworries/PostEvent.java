package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 11/5/16.
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
import android.widget.Toast;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostEvent extends AppCompatActivity {
    Toolbar toolbar;
    EditText etpostdetails,etTitle;
    ImageButton ibhyperlink,ibattachimage;
    int RESULT_LOAD_IMG=1;
    String imgDecodableString;
    LinearLayout focusText,buttonVisible;
    ImageButton boldButton,italicButton,underlineButton;
    Button bDone;
    int userId,placeId;
    String date;
    Map<String,String> data = new HashMap<>();
    int mStart=-1;
    int italicStart=-1;
    int underLineStart=-1;
    boolean bold=false;
    boolean italic = false;
    boolean underline = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_post);
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userid");
        placeId = bundle.getInt("placeid");
        date = bundle.getString("datecreated");
        toolbar = (Toolbar) findViewById(R.id.tbIncludePostEvent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        etpostdetails = (EditText)findViewById(R.id.etEventPostEvent);
        etTitle = (EditText) findViewById(R.id.etTitlePostEvent);
        ibattachimage=(ImageButton)findViewById(R.id.ibImagePostEvent);
        ibhyperlink = (ImageButton) findViewById(R.id.ibHyperlinkPostEvent);
        boldButton = (ImageButton) findViewById(R.id.ibBoldPostEvent);
        italicButton= (ImageButton) findViewById(R.id.ibItalicPostEvent);
        underlineButton = (ImageButton) findViewById(R.id.ibUnderlinePostEvent);
        focusText = (LinearLayout)findViewById(R.id.llPostEvent);
        buttonVisible = (LinearLayout)findViewById(R.id.trPostEvent);
        bDone = (Button) findViewById(R.id.bDone);



        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = etpostdetails.getText().toString();
                String eventTitle = etTitle.getText().toString();
                Log.e("postbefore1","postbefore1");
                data.put("user_id",Integer.toString(userId));
                data.put("place_id",Integer.toString(placeId));
                data.put("event",event);
                data.put("event_title",eventTitle);
                data.put("date",date);
                JsonUtility jsonUtility = new JsonUtility();
                String queryData=null;
                try {
                    queryData = jsonUtility.constructJSONEvent(data);
                    Log.e("querydata",queryData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod("POST");
                requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/events/insertnewevent");
                requestPackage.setParam("event_data", queryData);

                AskEventThread askEventThread = new AskEventThread();
                askEventThread.execute(requestPackage);

            }
        });




        focusText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etpostdetails.requestFocus();
                etpostdetails.setFocusableInTouchMode(true);
                buttonVisible.setVisibility(View.VISIBLE);
                return false;
            }
        });


        etpostdetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    buttonVisible.setVisibility(View.VISIBLE);
                } else {
                    buttonVisible.setVisibility(View.GONE);
                }
            }
        });

        ibattachimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostEvent.this);
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





        etpostdetails.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int currentLoc = etpostdetails.getSelectionStart();
                /**if(mStart>currentLoc || italicStart>currentLoc){
                 mStart=currentLoc;
                 italicStart=currentLoc;
                 }*/


                if (bold == true) {
                    int end = etpostdetails.getSelectionEnd();
                    Log.e("pradeepstarterror", "bold-----mStart" + mStart + "end" + end);
                    Spannable spn = (Spannable) etpostdetails.getText();
                    if (end == 0) end = 1;
                    spn.setSpan(new StyleSpan(Typeface.BOLD), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (bold == false) {
                    if (mStart == -1) mStart = 0;
                    int end = etpostdetails.getSelectionEnd();
                    Log.e("pradeepstarterror", "not bold end" + end);
                    Spannable spn2 = (Spannable) etpostdetails.getText();
                    if (end == 0) end = 1;
                    spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                if (italic == true) {
                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn = (Spannable) etpostdetails.getText();
                    Log.e("pradeepstarterror", "italic end" + end);
                    if (end == 0) end = 1;
                    spn.setSpan(new StyleSpan(Typeface.ITALIC), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (italic == false) {
                    if (italicStart == -1) italicStart = 0;
                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn2 = (Spannable) etpostdetails.getText();
                    Log.e("pradeepstarterror", "not italic end" + end);
                    if (end == 0) end = 1;
                    spn2.setSpan(new StyleSpan(Typeface.NORMAL), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }


                if (underline == true) {
                    int end = etpostdetails.getSelectionEnd();
                    UnderlineSpan[] ss = s.getSpans(end - 1, end, UnderlineSpan.class);
                    Spannable spn = (Spannable) etpostdetails.getText();
                    Log.e("pradeepstarterror", " underline end" + end);
                    if (end == 0) end = 1;
                    spn.setSpan(new UnderlineSpan(), end - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (underline == false) {
                    int end = etpostdetails.getSelectionEnd();
                    Spannable spn2 = (Spannable) etpostdetails.getText();
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
                    underLineStart = Selection.getSelectionStart(etpostdetails.getText());
                    underline = true;
                } else {
                    underline = false;
                    underLineStart = Selection.getSelectionStart(etpostdetails.getText());
                    //mStart = etaskquery.getText().length();
                }
            }
        });

    }


    private void addImageBetweentext(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionCursor, "\n.");

        selectionCursor = etpostdetails.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(etpostdetails.getText());
        Log.e("pradeepstarterror",selectionCursor+"selectioncursor");
        builder.setSpan(new ImageSpan(drawable),selectionCursor-1, selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        etpostdetails.setText(builder);
        etpostdetails.setSelection(selectionCursor);
        int selectionline = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionline, "\n");
    }

    private void addHyperlink(EditText editText){

        String hyperlink ="<ahref='"+editText.getText().toString()+"'>"+editText.getText().toString()+"</a" ;
        int selectionCursor = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionCursor, " ");
        int selectionCursor1 = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionCursor1, Html.fromHtml(hyperlink));
        int selectionline = etpostdetails.getSelectionStart();
        etpostdetails.getText().insert(selectionline, " ");

        selectionCursor = etpostdetails.getSelectionStart();
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




    private class AskEventThread extends AsyncTask<RequestPackage,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            Toast.makeText(PostEvent.this, "Query has been successfully posted", Toast.LENGTH_LONG).show();
            finish();


        }
    }
}
