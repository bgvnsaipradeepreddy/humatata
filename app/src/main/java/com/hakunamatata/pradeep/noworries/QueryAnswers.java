package com.hakunamatata.pradeep.noworries;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pradeep on 14/5/16.
 */
public class QueryAnswers extends AppCompatActivity {

    ListView listView;
    String[] answer ={"pradeep","sai"};
    PopulateComments populateComments;
    int queryId,userId;
    PopulateQueryAnswers populateQueryAnswers;
    ProgressBar dialogProgress;
    Toolbar toolbar;
    String queryTitle,queryContent,userImage,userName;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_query);
        Log.e("query1", "query1");
        Bundle bundle = getIntent().getExtras();
        queryId=bundle.getInt("query_id");

        userId = bundle.getInt("user_id");
        Log.e("queryanswer","q"+userId);
        Log.e("queryid","q"+queryId);
        queryTitle = bundle.getString("query_title");
        queryContent = bundle.getString("query_content");
        userImage = bundle.getString("user_image");
        //Log.e("userimage",userImage);
        userName = bundle.getString("user_name");
        Log.e("queryid1","q1"+userImage+queryTitle+queryContent+queryId);

        toolbar = (Toolbar) findViewById(R.id.tbIncludeQueryAnswers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.lvQueryAnswers);

        View header = getLayoutInflater().inflate(R.layout.answer_query_header, null);

        TextView query = (TextView) header.findViewById(R.id.tvQueryHeaderQueryAnswer);
        //TextView date = (TextView) convertView.findViewById(R.id.date_query);
        ExpandableTextView expandableTextView = (ExpandableTextView) header.findViewById(R.id.etvDescriptionHeaderQueryAnswer);
        EditText editText = (EditText) header.findViewById(R.id.etHeaderQueryAnswer);



        ImageView img1 = (ImageView) header.findViewById(R.id.ivHeaderQueryAnswer);
        Bitmap decodedByte;
        if(userImage.equals("no value")){
            decodedByte = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.myprofile_black);
        }else {
            byte[] decodedString = Base64.decode(userImage, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        }

        Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 100, 100, true);
        Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
        img1.setImageBitmap(conv_bm);
        TextView name = (TextView) header.findViewById(R.id.tvNameHeaderQueryAnswer);
        TextView date = (TextView) header.findViewById(R.id.tvNameHeaderQueryAnswer);


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("user_id",userId);
                bundle.putInt("query_id",queryId);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.ADDQUERYANSWER");
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


        query.setText(queryTitle);
        expandableTextView.setText(queryContent);
        Date datePosted = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //date.setText("Posted on "+sdf.format(datePosted));
        name.setText(userName);
        listView.addHeaderView(header);


        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/queries/getqueryanswers");
        String queryIdFinal = Integer.toString(queryId);
        requestPackage.setParam("query_id",queryIdFinal);
        new ProgressQueryAnswerTask().execute(requestPackage);




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


    class ProgressTask extends AsyncTask<RequestPackage, String, String> {

        JsonParser jsonParser = new JsonParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("pradeepdebug", "querypre");
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
            //

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<ModelDataQuery> modelDataQueryList = new ArrayList<>();
            try {
                modelDataQueryList = jsonParser.parseQueries(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            View convertView =  getLayoutInflater().inflate(R.layout.comments_query, null);
            ListView listPopulate = (ListView) convertView.findViewById(R.id.lvQueryComments);
            dialogProgress = (ProgressBar) convertView.findViewById(R.id.pbQueryComments);
            dialogProgress.setVisibility(View.INVISIBLE);
            builder.setTitle(Html.fromHtml("<font color='#c24946'>Comments</font>"));

            builder.setView(convertView);
            populateComments = new PopulateComments(QueryAnswers.this,modelDataQueryList);

            listPopulate.setAdapter(populateComments);

            // Set the action buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //  Your code when user clicked on Cancel
                        }
                    });
            builder.show();
            Log.e("pradeepdebug", "querypost");
        }
    }




    class PopulateComments extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<String> quest = new ArrayList<String>();
        ArrayList<String> quest_description = new ArrayList<String>();
        ArrayList<ModelDataQuery> queries = new ArrayList<>();
        int queryId;
        String queryTitle,queryContent,userImage,userName;
        ImageButton bookmark;

        public PopulateComments(Activity activity, ArrayList<ModelDataQuery> queries) {
            super(activity, R.layout.comments_populate, queries);
            this.queries = queries;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ModelDataQuery modelDataQuery = new ModelDataQuery();
            modelDataQuery = queries.get(position);
            convertView = li.inflate(R.layout.comments_populate, parent, false);
            queryId = modelDataQuery.getQueryId();
            queryTitle = modelDataQuery.getQueryTitle();
            queryContent = modelDataQuery.getQueryContent();
            userImage = modelDataQuery.getImageLocation();
            userName = modelDataQuery.getUserName();
            Log.e("queryid1","q1"+userImage+queryTitle+queryContent+queryId);

            TextView tvComments = (TextView) convertView.findViewById(R.id.tvCommentsPopulateComments);
            //TextView date = (TextView) convertView.findViewById(R.id.date_query);
            TextView name = (TextView) convertView.findViewById(R.id.tvNamePopulateComments);

            ExpandableTextView expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.etvDescriptionPopulateComments);



            List<String> your_array_list = new ArrayList<String>();
            your_array_list.add("pradeep");
            your_array_list.add("reddy");
            ImageView img1 = (ImageView) convertView.findViewById(R.id.ivPopulateComments);
            name.setText(modelDataQuery.getUserName());
            Bitmap decodedByte;
            if(modelDataQuery.getImageLocation().equals("no value")){
                decodedByte = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.myprofile_black);
            }else {
                byte[] decodedString = Base64.decode(modelDataQuery.getImageLocation(), Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            }

            Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 100, 100, true);
            Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
            img1.setImageBitmap(conv_bm);
            TextView date = (TextView) convertView.findViewById(R.id.tvDatePopulateComments);


            expandableTextView.setText(modelDataQuery.getQueryContent());
            Date datePosted = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //date.setText("Posted on "+sdf.format(datePosted));
            return convertView;
        }
        public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int borderWidth=2;
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            //Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            radius = radius-10;
            Log.e("radius","radius"+radius);
            canvas.drawCircle(width / 2, height / 2, radius, paint);
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(borderWidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
            return output;
        }


    }
    private class ProgressQueryAnswerTask extends AsyncTask<RequestPackage,String,String> {

        JsonParser jsonParser = new JsonParser();
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

            ArrayList<ModelDataQueryAnswer> modelDataQueryAnswers = new ArrayList<>();
            try {
                modelDataQueryAnswers = jsonParser.parseQueryAnswers(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            populateQueryAnswers = new PopulateQueryAnswers(QueryAnswers.this,modelDataQueryAnswers);
            listView.setAdapter(populateQueryAnswers);
            //progressBar.setVisibility(View.GONE);
            Log.e("pradeepdebug", "querypost");
        }
    }
    public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int borderWidth=2;
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        //Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        radius = radius-10;
        Log.e("radius", "radius" + radius);
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return output;
    }




    class PopulateQueryAnswers extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<ModelDataQueryAnswer> queryAnswer = new ArrayList<>();
        int userId;

        public PopulateQueryAnswers(Activity activity, ArrayList<ModelDataQueryAnswer> queryAnswer) {
            super(activity, R.layout.answers_query_populate, queryAnswer);
            this.queryAnswer = queryAnswer;
            li = activity.getWindow().getLayoutInflater();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ModelDataQueryAnswer modelDataQueryAnswer = new ModelDataQueryAnswer();
            modelDataQueryAnswer = queryAnswer.get(position);
            convertView = li.inflate(R.layout.answers_query_populate, parent, false);
            TextView answer = (TextView) convertView.findViewById(R.id.etvDescriptionPopulateQueryAnswers);
            //TextView date = (TextView) convertView.findViewById(R.id.date_query);
            TextView name = (TextView) convertView.findViewById(R.id.tvNamePopulateQueryAnswers);

            TextView tvComments = (TextView) convertView.findViewById(R.id.tvCommentsPopulateQueryAnswers);


            tvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    builder = new AlertDialog.Builder(QueryAnswers.this);



                    RequestPackage requestPackage = new RequestPackage();
                    requestPackage.setMethod("GET");
                    requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/queries/getplacequery");
                    String place = Integer.toString(1);
                    Log.e("please", place);
                    requestPackage.setParam("place_id", place);

                    new ProgressTask().execute(requestPackage);

                }
            });
            ImageView img1 = (ImageView) convertView.findViewById(R.id.ivPopulateQueryAnswers);

            Bitmap decodedByte;
            if(modelDataQueryAnswer.getImageLocation().equals("no value")){
                decodedByte = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.myprofile_black);
            }else {
                byte[] decodedString = Base64.decode(modelDataQueryAnswer.getImageLocation(), Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            }

            Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 100, 100, true);
            Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
            img1.setImageBitmap(conv_bm);
            answer.setText(modelDataQueryAnswer.getDescription());
            String day = new SimpleDateFormat("dd-MMM-EE").format(new Date());
            //date.setText(day);
            name.setText(modelDataQueryAnswer.getUserName());
            return convertView;
        }


        public Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,int borderWidth){

            if (bitmap == null || bitmap.isRecycled()) {
                return null;
            }

            final int width = bitmap.getWidth() + borderWidth;
            final int height = bitmap.getHeight() + borderWidth;

            Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            canvas.drawCircle(width / 2, height / 2, radius, paint);
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(borderWidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
            return canvasBitmap;
        }

        public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int borderWidth=2;
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            //Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            radius = radius-10;
            Log.e("radius","radius"+radius);
            canvas.drawCircle(width / 2, height / 2, radius, paint);
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(borderWidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
            return output;
        }


    }


}

