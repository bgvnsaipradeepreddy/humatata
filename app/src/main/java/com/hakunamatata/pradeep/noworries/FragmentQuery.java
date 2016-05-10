package com.hakunamatata.pradeep.noworries;


/**
 * Created by pradeep on 26/4/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class FragmentQuery extends Fragment {
    String location;
    int userId;
    int placeId;
    EditText search;
    ProgressBar pbQuery;
    ListView listView;
    PopulateQueries populateQueries;
    View view;
    SearchView searchView;
    FloatingActionButton fabquery;
    View header;
    ArrayList<String> questions = new ArrayList<String>();
    ArrayList<String> description = new ArrayList<String>();

    public static FragmentQuery newInstance(int userId, int placeId, String location) {

        FragmentQuery fragment = new FragmentQuery();
        Bundle args = new Bundle();
        args.putString("place", location);
        args.putInt("placeId", placeId);
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentQuery() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        location = getArguments().getString("place");
        userId = getArguments().getInt("userId");
        placeId = getArguments().getInt("placeId");
        view = inflater.inflate(R.layout.query_fragment, container, false);
        pbQuery = (ProgressBar) view.findViewById(R.id.pbFragmentQuery);
        pbQuery.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tbIncludeUserEntities);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(location);


        listView = (ListView) view.findViewById(R.id.lvFragmentQuery);

        fabquery = (FloatingActionButton) view.findViewById(R.id.fabFragmetQuery);
        fabquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("place", location);
                bundle.putInt("place_id", placeId);
                bundle.putInt("user_id", userId);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.ASKQUERY");
                //Intent intent = new Intent("com.hakunamatata.hakunamatata.EXPANDEXAMPLE");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/queries/getplacequery");
        String place = Integer.toString(placeId);
        Log.e("please",place);
        requestPackage.setParam("place_id",place);

        new ProgressTask().execute(requestPackage);
        return view;
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
            populateQueries = new PopulateQueries(getActivity(),modelDataQueryList);
            pbQuery.setVisibility(View.GONE);
            listView.setAdapter(populateQueries);
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

    class PopulateQueries extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<String> quest = new ArrayList<String>();
        ArrayList<String> quest_description = new ArrayList<String>();
        ArrayList<ModelDataQuery> queries = new ArrayList<>();
        int userId;
        ImageButton bookmark;

        public PopulateQueries(Activity activity, ArrayList<ModelDataQuery> queries) {
            super(activity, R.layout.queries_populate,queries);
            this.queries = queries;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ModelDataQuery modelDataQuery = new ModelDataQuery();
            modelDataQuery = queries.get(position);
            convertView = li.inflate(R.layout.queries_populate, parent, false);
            TextView query = (TextView) convertView.findViewById(R.id.tvQueryPopulateQueries);
            //TextView date = (TextView) convertView.findViewById(R.id.date_query);
            ExpandableTextView expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.etvDescriptionPopulateQueries);
            query.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("com.hakunamatata.pradeep.noworries.QUERYANSWERS");
                    getContext().startActivity(intent);

                }
            });
            ImageView img1 = (ImageView) convertView.findViewById(R.id.ivPopulateQueries);
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
            TextView name = (TextView) convertView.findViewById(R.id.tvNamePopulateQueries);
            TextView date = (TextView) convertView.findViewById(R.id.tvNamePopulateQueries);


            query.setText(modelDataQuery.getQueryTitle());
            expandableTextView.setText(modelDataQuery.getQueryContent());
            Date datePosted = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //date.setText("Posted on "+sdf.format(datePosted));
            name.setText(modelDataQuery.getUserName());
            return convertView;
        }
        public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth() - 20, bitmap.getHeight() - 20);
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }


    }

}