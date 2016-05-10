package com.hakunamatata.pradeep.noworries;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List   ;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by pradeep on 20/4/16.
 */
public class UserSelectPlaces extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();


    ArrayAdapter<String> populatePlace;
    LayoutInflater li;
    List<String> tempStore = new ArrayList<String>();
    boolean[] checkedStatus;
    ArrayList<String> place = new ArrayList<String>();
    ArrayList<String> seletedItems = new ArrayList<String>();
    Map<String,Integer> mapStateIds = new HashMap<String,Integer>();
    Map<String,Integer> mapDistrictIds = new HashMap<String,Integer>();
    Map<String,Integer> mapLocationIds = new HashMap<String,Integer>();
    int userId, districtId;
    int stateId;
    String stateData,districtData,locationData;
    Bundle bundle = new Bundle();
    Toolbar toolbar;
    ListView lv;
    ProgressBar progressBar;
    String selecteddistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_select_user);

        bundle = getIntent().getExtras();
        userId = bundle.getInt("user_id");


        // get the listview
        progressBar = (ProgressBar) findViewById(R.id.pbUserSelectPlaces);
        progressBar.setVisibility(View.INVISIBLE);
        expListView = (ExpandableListView) findViewById(R.id.elvUserSelectPlaces);
        final Button bdone = (Button) findViewById(R.id.bUserSelectPlaces);
        bdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    JsonParser jsonParser = new JsonParser();
                    List<ModelData> locations = new ArrayList<>();
                    mapLocationIds = jsonParser.parseLocationIdMapFeed(locationData);

                    for(int i=0;i<seletedItems.size();i++){
                        int locationId = mapLocationIds.get(seletedItems.get(i));
                        ModelData modelData = new ModelData();
                        modelData.setUserId(userId);
                        modelData.setLocationId(locationId);
                        modelData.setLocationName(seletedItems.get(i));
                        locations.add(modelData);
                    }

                    String locationJson= "";
                    JsonUtility jsonUtility = new JsonUtility();
                    locationJson = jsonUtility.constructJSONUserSelectPlaces(locations);
                    Log.e("pradeepjson", locationJson);


                    RequestPackage requestPackage = new RequestPackage();
                    requestPackage.setMethod("GET");
                    String stateUri = "http://192.168.42.49:8080/noworries/webapi/placestate/getlocation";


                    requestPackage.setUri(stateUri);
                    requestPackage.setParam("locations",locationJson);

                    LocationsThread locationsThread = new LocationsThread();
                    //
                    try {
                        locationsThread.execute(requestPackage).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.tbIncludeUserSelectPlaces);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_button);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // preparing list data
        prepareListData();



        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, final int childPosition, long id) {
                JsonParser jsonParser = new JsonParser();
                String selectedDistrict = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                try {
                    mapDistrictIds = jsonParser.parseDistrictIdMapFeed(districtData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int districtId = mapDistrictIds.get(selectedDistrict);

                try {
                    place = jsonParser.parseLocationFeed(locationData, districtId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(UserSelectPlaces.this);

                final String[] placesel = new String[place.size()];
                for (int i = 0; i < place.size(); i++) {
                    placesel[i] = place.get(i);
                }
                checkedStatus = new boolean[place.size()];

                //LayoutInflater inflater= getLayoutInflater();
                //View view_header = inflater.inflate(R.layout.expandable_heading, null);
                //builder.setCustomTitle(view_header);
                builder.setTitle(Html.fromHtml("<font color='#c24946'>Select Places</font>"));


                for (int i = 0; i < placesel.length; i++) {
                    checkedStatus[i] = false;
                    for (int j = 0; j < seletedItems.size(); j++) {
                        if (seletedItems.get(j).equals(place.get(i))) {
                            checkedStatus[i] = true;
                            break;
                        }
                    }
                }

                builder.setMultiChoiceItems(placesel, checkedStatus,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            // indexSelected contains the index of item (of which checkbox checked)

                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected,
                                                boolean isChecked) {

                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    // write your code when user checked the checkbox
                                    tempStore.add(placesel[indexSelected]);
                                } else if (seletedItems.contains(placesel[indexSelected])) {
                                    // Else, if the item is already in the array, remove it
                                    // write your code when user Uchecked the checkbox
                                    seletedItems.remove(placesel[indexSelected]);
                                    tempStore.remove(placesel[indexSelected]);
                                }
                            }
                        })
                        // Set the action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                for (int i = 0; i < tempStore.size(); i++) {
                                    seletedItems.add(tempStore.get(i));
                                }
                                tempStore.clear();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel

                            }
                        });
                builder.show();

                return false;
            }
        });
    }


    private void prepareListData() {



            RequestPackage requestPackageState = new RequestPackage();
            requestPackageState.setMethod("GET");
            String stateUri = "http://192.168.42.49:8080/noworries/webapi/placestate/getplacestate";
            String districtUri = "http://192.168.42.49:8080/noworries/webapi/placestate/getplacedistrict";
            String locationUri = "http://192.168.42.49:8080/noworries/webapi/placestate/getplaces";

        requestPackageState.setUri(stateUri);

        RequestPackage requestPackageDistrict = new RequestPackage();
        requestPackageDistrict.setMethod("GET");
        requestPackageDistrict.setUri(districtUri);

        RequestPackage requestPackageLocation = new RequestPackage();
        requestPackageLocation.setMethod("GET");
        requestPackageLocation.setUri(locationUri);

            PlacesThread placesThread = new PlacesThread();
            //
        try {
            placesThread.execute(requestPackageState,requestPackageDistrict,requestPackageLocation,requestPackageLocation).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class PlacesThread extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            stateData = httpManager.getData(params[0]);
            districtData = httpManager.getData(params[1]);
            locationData = httpManager.getData(params[2]);
            return null;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            JsonParser jsonParser = new JsonParser();
            try {
                Log.e("pradeeppostexec","pradeeppostexec");
                listDataHeader = jsonParser.parseStateFeed(stateData);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e("pradeeptest","pradeeptest");

            try {
                mapStateIds = jsonParser.parseStateIdMapFeed(stateData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < listDataHeader.size(); i++) {
                List<String> city = new ArrayList<String>();
                //register.storeDistrictData(subcity,stateId);
                int id = mapStateIds.get(listDataHeader.get(i));
                try {
                    city = jsonParser.parseDistrictFeed(districtData,id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listDataChild.put(listDataHeader.get(i), city);

            }
            progressBar.setVisibility(View.INVISIBLE);

            listAdapter = new ExpandableStateListAdapter(UserSelectPlaces.this, listDataHeader, listDataChild);


            // setting list adapter
            expListView.setAdapter(listAdapter);

        }
    }
    private class LocationsThread extends AsyncTask<RequestPackage, String, String>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            HttpManager httpManager = new HttpManager();
            String output = httpManager.getData(params[0]);
            Log.e("pradeepoutput",output);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.INVISIBLE);
            bundle.putInt("user_id", userId);
            Intent intent = new Intent("com.hakunamatata.pradeep.noworries.USERSELECTIONOPTIONS");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}









