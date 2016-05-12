package com.hakunamatata.pradeep.noworries;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
public class FragmentEventResult extends Fragment {

    View view;
    ListView listEvent;
    FloatingActionButton fabevent;
    PopulateEvents populateEvents;
    ProgressBar progressBar;
    String date,location;
    int userId,placeId;
    String[] events = {"31st hyderabad film fare, Gachibowligfukfkkufkufkuutdfkydykfukfkutfutfkuyfgufulftufutfutfultftulfigoufyyti", "31st hyderabad film fare, Gachibowli", "31st hyderabad film fare, Gachibowli", "31st hyderabad film fare, Gachibowli"};

    public static FragmentEventResult newInstance(int userId, int placeId,String location,String dateString) {

        FragmentEventResult fragment = new FragmentEventResult();
        Bundle args = new Bundle();
        args.putString("place", location);
        args.putInt("place_id", placeId);
        args.putInt("user_id", userId);
        args.putString("date",dateString);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEventResult() {


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
        date = getArguments().getString("date");
        view = inflater.inflate(R.layout.result_event_fragment, container, false);
        listEvent =(ListView) view.findViewById(R.id.lvFragmentEventResult);
        progressBar = (ProgressBar) view.findViewById(R.id.pbFragmentEventResult);
        progressBar.setVisibility(View.INVISIBLE);
        fabevent = (FloatingActionButton)view.findViewById(R.id.fabFragmentEventResult);
        fabevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("placeid", placeId);
                bundle.putInt("userid", userId);
                bundle.putString("datecreated",date);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.POSTEVENT");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/events/getplaceevents");
        String place = Integer.toString(placeId);
        String dateString = date;
        Log.e("please", place);
        requestPackage.setParam("place_id", place);
        requestPackage.setParam("date",dateString);

        new EventsTask().execute(requestPackage);

        return view;
    }



    class EventsTask extends AsyncTask<RequestPackage, String, String> {

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
            ArrayList<ModelDataEvent> modelDataEventList = new ArrayList<>();
            try {
                modelDataEventList = jsonParser.parseEvents(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            populateEvents = new PopulateEvents(getActivity(),modelDataEventList);
            listEvent.setAdapter(populateEvents);
            Log.e("pradeepdebug", "querypost");
        }
    }



    class PopulateEvents extends ArrayAdapter {
        LayoutInflater li;
        //ArrayList<String> events = new ArrayList<>();
        ArrayList<ModelDataEvent> events;
        int userId;
        ImageButton bookmark;

        public PopulateEvents(Activity activity, ArrayList<ModelDataEvent> events) {
            super(activity, R.layout.event_populate,events);
            this.events = events;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ModelDataEvent modelDataEvent = new ModelDataEvent();
            modelDataEvent = events.get(position);
            convertView = li.inflate(R.layout.event_populate, parent, false);

            TextView textView = (TextView)convertView.findViewById(R.id.tvPopulateEvent);
            textView.setText(modelDataEvent.getEventTitle());
            return convertView;
        }
    }
}