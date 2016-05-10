package com.hakunamatata.pradeep.noworries;

import android.app.Activity;
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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pradeep on 9/5/16.
 */
public class FragmentClimate extends Fragment {


    android.os.Handler handle = new Handler();
    TextView temp,info;
    ProgressBar progressBar;
    PopulateData populateData;
    String location;
    ListView lv;
    View view;
    Map<String,String> data = new HashMap<>();

    EditText addSome;
    int userId,placeId;


    public static FragmentClimate newInstance(int userId, int placeId,String location) {

        FragmentClimate fragment = new FragmentClimate();

        Bundle args = new Bundle();
        args.putString("place", location);
        args.putInt("place_id",placeId);
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentClimate() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //updateWeatherData(pulivendula);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ArrayList<String> values = new ArrayList<>();
        values.add("Basavaraju Venkata Padmanabha Rao, better known as Padmanabham to generations of countless fans in Telugu cinema was born to Santham and Basavaraju Venkata Seshaiah in Simhadripuram, Pulivendula");
        values.add("TVSR Technologies was founded in 2014 by Raghunath Reddy. The company was incorporated as TVSR Technologies India Pvt Ltd. the company corporate headquarters located Bangalore. registered office is Pulivendula, Development offices Bangalore, Hyderabad. TVSR Technologies have Top Software Development, Website Designing in Bangalore, Hyderabad, Pulivendula.");
        values.add("Huge deposits of natural uranium, which promises to be one of the top 20 of the world's reserves, have been found in the Tummalapalle belt in the southern part of the Kadapa basin in Andhra Pradesh in March 2011");
        view = inflater.inflate(R.layout.climate_fragment, container, false);
        userId = getArguments().getInt("user_id");
        Log.e("giri","giri"+userId);
        placeId=getArguments().getInt("place_id");
        location = getArguments().getString("place");
        lv = (ListView) view.findViewById(R.id.lvFragmentClimate);
        //PopulateData populateData = new PopulateData(getActivity(), userId, values);
        View header = inflater.inflate(R.layout.climate_fragment_header, null);
        temp = (TextView) header.findViewById(R.id.tvTemFragmentClimate);
        info = (TextView) header.findViewById(R.id.tvInfoFragmentClimate);
        addSome = (EditText) header.findViewById(R.id.etadd);
        progressBar = (ProgressBar) view.findViewById(R.id.pbFragmentClimate);

        //lv.setAdapter(populateData);

        addSome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("place", location);
                bundle.putInt("place_id", placeId);
                bundle.putInt("user_id", userId);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.ADDPLACEINFORMATION");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://api.openweathermap.org/data/2.5/weather");
        String place = Integer.toString(placeId);
        Log.e("please", place);
        requestPackage.setParam("units", "metric");
        requestPackage.setParam("APPID","527a0500b6c8830f54621415a5dd1f45");
        requestPackage.setParam("q",location);
        lv.addHeaderView(header);
        new ProgressClimateTask().execute(requestPackage);

        return view;
    }



    class PopulateData extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<ModelDataTrivia> trivia = new ArrayList<>();
        int userId;

        public PopulateData(Activity activity, int userId, ArrayList<ModelDataTrivia> trivia) {
            super(activity, R.layout.populate_climate_fragment, trivia);
            this.userId = userId;
            this.trivia = trivia;
            li = activity.getWindow().getLayoutInflater();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ModelDataTrivia modelDataTrivia = new ModelDataTrivia();
            modelDataTrivia = trivia.get(position);
            convertView = li.inflate(R.layout.populate_climate_fragment, parent, false);
            TextView query = (TextView) convertView.findViewById(R.id.tvDataFragmentClimatePopulate);
            //TextView date = (TextView) convertView.findViewById(R.id.date_query);
            ImageView img1 = (ImageView) convertView.findViewById(R.id.ivFragmentClimatePopulate);
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.myprofile_black);
            //Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
            //Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
            Bitmap conv_bm = getCircularBitmapWithWhiteBorder(bm, 2);
            img1.setImageBitmap(conv_bm);
            TextView name = (TextView) convertView.findViewById(R.id.tvNameFragmentClimatePopulate);
            query.setText(modelDataTrivia.getDescription());
            String day = new SimpleDateFormat("dd-MMM-EE").format(new Date());
            //date.setText(day);
            name.setText(modelDataTrivia.getUserName());
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

    private class ProgressClimateTask extends AsyncTask<RequestPackage,String,String>{

        JsonParser jsonParser = new JsonParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            String jsonOutput = httpManager.getData(params[0]);


            return jsonOutput;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> details = new ArrayList<>();
            try {
                details = jsonParser.parseTempData(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            temp.setText(details.get(0) + " ℃");
            info.setText(details.get(1).toUpperCase(Locale.US));

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod("GET");
            requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/trivia/getplacetrivia");
            String placeString = Integer.toString(placeId);
            Log.e("please", placeString);
            requestPackage.setParam("place_id", placeString);

            new ProgressTrivia().execute(requestPackage);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private class ProgressTrivia extends AsyncTask<RequestPackage,String,String>{

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

            ArrayList<ModelDataTrivia> modelDataTriviaList = new ArrayList<>();
            try {
                modelDataTriviaList = jsonParser.parseTrivia(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            populateData = new PopulateData(getActivity(),userId,modelDataTriviaList);
            lv.setAdapter(populateData);
            progressBar.setVisibility(View.GONE);
            Log.e("pradeepdebug", "querypost");
        }
    }
}