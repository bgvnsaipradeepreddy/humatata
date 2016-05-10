package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 24/4/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPlaces extends Fragment {
    ArrayList<String> places = new ArrayList<String>();

    View view;
    ListView lv;
    int userId;
    ProgressBar pb;
    Map<String,Integer> locationId;
    ArrayList<String> list;
    Button placesButton;
    PopulateInfo populateInfo;
    String data = "";
    int output = 0;

    public static FragmentPlaces newInstance(int userId) {
        Log.e("inplaces1","inplaces1");
        FragmentPlaces fragment = new FragmentPlaces();
        Bundle args = new Bundle();
        args.putInt("userid", userId);
        fragment.setArguments(args);
        Log.e("inplaces2", "inplaces2");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //updateWeatherData(pulivendula);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        Log.e("inplaces3","inplaces3");
        userId = getArguments().getInt("userid");
        String userIdString;
        userIdString = Integer.toString(userId);
        view = inflater.inflate(R.layout.places_fragment, container, false);
        lv = (ListView) view.findViewById(R.id.lvFragmentPlaces);
        pb = (ProgressBar) view.findViewById(R.id.pbFragmentPlaces);
        placesButton = (Button) view.findViewById(R.id.bFragmentPlaces);
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUri("http://192.168.42.49:8080/noworries/webapi/userselection/getuserselection");
        requestPackage.setParam("user_id", userIdString);
        Log.e("inplaces4", "inplaces4");
        PlacesThread placesThread = new PlacesThread();
        placesThread.execute(requestPackage);

        placesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("user_id",userId);
                //bundle.putString("data","pradeep");
                Intent place_intent = new Intent("com.hakunamatata.pradeep.noworries.USERSELECTPLACES");
                place_intent.putExtras(bundle);
                startActivity(place_intent);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("place", list.get(position));
                bundle.putInt("placeId",locationId.get(list.get(position)));
                bundle.putInt("userid", userId);
                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.USERENTITIES");
                Log.e("pradeepdebug", "pradeep1");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        return view;
    }


    class PopulateInfo extends ArrayAdapter {
        LayoutInflater li;
        ArrayList<String> placeInfo;

        public PopulateInfo(Activity activity, ArrayList<String> placeInfo,Map<String,Integer> locationIdMap) {
            super(activity, R.layout.state_place_populate, placeInfo);
            this.placeInfo = placeInfo;
            locationId = locationIdMap;
            li = activity.getWindow().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.state_place_populate, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.tvPopulateInfo);
            tv.setText(placeInfo.get(position));
            return convertView;
        }
    }
    private class PlacesThread extends AsyncTask<RequestPackage,String,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            Log.e("inplaces6","inplaces6");
            HttpManager httpManager = new HttpManager();
            data = httpManager.getData(params[0]);
            Log.e("inplaces7","inplaces7");
            Log.e("data",data);
            return data;
        }

        @Override
        protected void onPostExecute(String locationData) {
            super.onPostExecute(locationData);
            List<String> locationCount = new ArrayList<>();
            try {
                locationCount = new JsonParser().parseSelectedLocationCount(locationData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("locationdata",locationData);
            if(locationCount.size()>0) {


                pb.setVisibility(View.INVISIBLE);
                Map<String,Integer> locationMap = new HashMap<>();
                try {
                    locationMap = JsonParser.parseLocationIdMapFeed(locationData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list = new ArrayList<String>(locationMap.keySet());
                for(int i =0;i<list.size();i++){
                    Log.e("pradeeplist",list.get(i));
                }
                Log.e("exampe","xxx");
                populateInfo = new PopulateInfo(getActivity(), list,locationMap);
                lv.setAdapter(populateInfo);

            }
            else {


                pb.setVisibility(View.INVISIBLE);
                placesButton.setVisibility(View.VISIBLE);
                pb.setVisibility(View.INVISIBLE);
                //populateInfo = new PopulateInfo(getActivity(), places);
                //lv.setAdapter(populateInfo);
                Log.e("populatesuccess","populatesuccess");
            }
        }
    }

}