package com.hakunamatata.pradeep.noworries;


/**
 * Created by pradeep on 26/4/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class QueryComments extends Activity {
    String location;
    int userId;
    int placeId,queryId;
    EditText search;
    ProgressBar pbQuery;
    ListView listView;
    PopulateComments populateComments;
    View view;
    Toolbar toolbar;
    SearchView searchView;
    FloatingActionButton fabquery;
    View header;
    ArrayList<String> questions = new ArrayList<String>();
    ArrayList<String> description = new ArrayList<String>();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_query);

        Bundle bundle = getIntent().getExtras();


        userId = bundle.getInt("user_id");
        Log.e("fragmemtq","frq"+userId);
        queryId = bundle.getInt("query_id");
        pbQuery = (ProgressBar) findViewById(R.id.pbQueryComments);

        pbQuery.setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.lvQueryComments);

        //toolbar = (Toolbar) findViewById(R.id.tbIncludeQueryComments);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.back_button);


        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/queries/getplacequery");
        String place = Integer.toString(1);
        Log.e("please",place);
        requestPackage.setParam("place_id",place);

        new ProgressTask().execute(requestPackage);
    }

    class ProgressTask extends AsyncTask<RequestPackage, String, String> {

        JsonParser jsonParser = new JsonParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("pradeepdebug", "querypre");
            pbQuery.setVisibility(View.VISIBLE);
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
            populateComments = new PopulateComments(QueryComments.this,modelDataQueryList);
            pbQuery.setVisibility(View.GONE);
            listView.setAdapter(populateComments);
            Log.e("pradeepdebug", "querypost");
        }
    }



    private void setViewStatus(View vg2, int status) {

        vg2.setVisibility(status);
    }

    public AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //setViewStatus(toolbar, View.GONE);

                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //scroll was stopped, let's show search bar again
                        //setViewStatus(searchView, View.VISIBLE);
                        setViewStatus(fabquery, View.VISIBLE);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        //user is scrolling, let's hide search bar
                        //setViewStatus(searchView, View.GONE);
                        setViewStatus(fabquery, View.GONE);
                        break;
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                /**
                 if (firstVisibleItem == 0) {
                 // check if we reached the top or bottom of the list
                 View v = listView.getChildAt(0);
                 int offset = (v == null) ? 0 : v.getTop();
                 if (offset == 0) {
                 // reached the top: visible header and footer

                 setViewStatus(toolbar, View.VISIBLE);
                 }

                 } else if (totalItemCount - visibleItemCount == firstVisibleItem) {
                 View v = listView.getChildAt(totalItemCount - 1);
                 int offset = (v == null) ? 0 : v.getTop();
                 if (offset == 0) {
                 // reached the bottom: visible header and footer

                 setViewStatus(toolbar, View.VISIBLE);
                 }
                 } else if (totalItemCount - visibleItemCount > firstVisibleItem){
                 // on scrolling
                 //setViewStatus(toolbar, View.GONE);

                 }
                 */
            }
        };

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
            super(activity, R.layout.comments_populate,queries);
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


            tvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("query_id", queryId);
                    bundle.putInt("user_id", userId);
                    Intent intent = new Intent("com.hakunamatata.pradeep.noworries.QUERYCOMMENTS");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

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

}