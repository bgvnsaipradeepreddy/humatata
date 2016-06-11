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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by srinu on 1/1/2016.
 */
public class FragmentDeals extends Fragment {

    View view;
    ListView listDeal;
    FloatingActionButton fabevent;
    PopulateDeals populateDeals;
    ProgressBar progressBar;
    String date,location;
    int userId,placeId;
    String[] events = {"31st hyderabad film fare, Gachibowligfukfkkufkufkuutdfkydykfukfkutfutfkuyfgufulftufutfutfultftulfigoufyyti", "31st hyderabad film fare, Gachibowli", "31st hyderabad film fare, Gachibowli", "31st hyderabad film fare, Gachibowli"};

    public static FragmentDeals newInstance(int userId, int placeId,String location) {

        FragmentDeals fragment = new FragmentDeals();
        Bundle args = new Bundle();
        args.putString("place", location);
        args.putInt("place_id", placeId);
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDeals() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        location = getArguments().getString("place");
        userId = getArguments().getInt("user_id");
        placeId = getArguments().getInt("place_id");
        view = inflater.inflate(R.layout.deals_fragment, container, false);
        listDeal =(ListView) view.findViewById(R.id.lvFragmentDeals);
        progressBar = (ProgressBar) view.findViewById(R.id.pbFragmentDeals);
        progressBar.setVisibility(View.INVISIBLE);
        fabevent = (FloatingActionButton)view.findViewById(R.id.fabFragmentDeals);
        fabevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("placeid", placeId);
                bundle.putInt("userid", userId);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.POSTDEAL");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/deals/getplacedeals");
        String place = Integer.toString(placeId);
        String dateString = date;
        Log.e("please", place);
        requestPackage.setParam("place_id", place);

        new DealsTask().execute(requestPackage);

        return view;
    }



    class DealsTask extends AsyncTask<RequestPackage, String, String> {

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
            ArrayList<ModelDataDeal> modelDataDealsList = new ArrayList<>();
            try {
                modelDataDealsList = jsonParser.parseDeals(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            populateDeals = new PopulateDeals(getActivity(),modelDataDealsList);
            listDeal.setAdapter(populateDeals);
            Log.e("pradeepdebug", "querypost");
        }
    }



    class PopulateDeals extends ArrayAdapter {
        LayoutInflater li;
        //ArrayList<String> events = new ArrayList<>();
        ArrayList<ModelDataDeal> deals;
        int userId;
        ImageButton bookmark;

        public PopulateDeals(Activity activity, ArrayList<ModelDataDeal> deals) {
            super(activity, R.layout.deals_populate,deals);
            this.deals = deals;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ModelDataDeal modelDataDeal = new ModelDataDeal();
            modelDataDeal = deals.get(position);
            convertView = li.inflate(R.layout.deals_populate, parent, false);

            TextView dealTitle = (TextView)convertView.findViewById(R.id.tvTitlePopulateDeals);
            TextView userName = (TextView)convertView.findViewById(R.id.tvUserPopulateDeals);

            ImageView img1 = (ImageView) convertView.findViewById(R.id.ivImagePopulateDeals);
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.myprofile_black);

            Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
            Bitmap conv_bm = getRoundedCornerBitmap(resized, 75);
            img1.setImageBitmap(conv_bm);
            userName.setText(modelDataDeal.getUserName());
            dealTitle.setText(modelDataDeal.getDealTitle());
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