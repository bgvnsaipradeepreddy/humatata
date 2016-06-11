package com.hakunamatata.pradeep.noworries;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by pradeep on 27/5/16.
 */
public class CarPooling extends AppCompatActivity {

    Toolbar toolbar;
    GoogleMap googleMap;
    EditText editText;
    Button button,bDone;
    Location location;
    String type;
    String selectedLocation;
    String selectedSubLocality;
    private LocationListener locListener = new MyLocationListener();
    private static final float DEFAULTZOOM = 17;
    boolean gps_enabled= false;
    boolean network_enabled = false;
    private LocationManager locationManager;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("start_destination");
        if (servicesOK()) {
            setContentView(R.layout.pooling_car);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            bDone = (Button) findViewById(R.id.bDone);

            if(initMap()){
                //Toast.makeText(this, "ready to map", Toast.LENGTH_SHORT).show();

                //googleMap.setMyLocationEnabled(true);
                if(type.equals("start")){
                    getLocation();
                }else
                {
                    getLocation();
                }

            }else {
                Toast.makeText(this, "not ready to map", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Cant connect to google play services", Toast.LENGTH_SHORT).show();
        }

        editText = (EditText) findViewById(R.id.etStartCarPooling);
        button = (Button) findViewById(R.id.bStartCarPooling);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editText.getText().toString();
                Geocoder geocoder = new Geocoder(CarPooling.this);
                List<Address> list = new ArrayList<>();
                try {
                    list = geocoder.getFromLocationName(location,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = list.get(0);

                String locality = address.getLocality();

                double lat = address.getLatitude();
                double lng = address.getLongitude();

                //Geocoder geocoder = new Geocoder(CarPooling.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                String subLocality = addresses.get(0).getSubLocality();
                String locationName = cityName + ","+stateName+","+countryName;

                gotoLocation(lat,lng,DEFAULTZOOM,locationName,subLocality);
                selectedLocation = locationName;
                selectedSubLocality = subLocality;

            }
        });

        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", selectedLocation);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

                toolbar = (Toolbar) findViewById(R.id.tbIncludeCarPooling);
        setSupportActionBar(toolbar);
        if(type.equals("start")) {
            getSupportActionBar().setTitle("Starting From");
        }else
        {
            getSupportActionBar().setTitle("Going To");
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            toolbar.setNavigationIcon(R.drawable.back_button);

    }



    private boolean initMap() {
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fCarPooling);
            googleMap = mapFragment.getMap();
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(CarPooling.this);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Address address = list.get(0);
                CarPooling.this.setMarker(latLng.latitude, latLng.longitude, address.getSubLocality() + "," + address.getLocality());
                editText.setText(address.getAddressLine(0) + "," + address.getAddressLine(2) + "," + address.getAddressLine(3));
                selectedLocation = address.getAddressLine(0) + "," + address.getAddressLine(2) + "," + address.getAddressLine(3);
                selectedSubLocality = address.getSubLocality();
            }
        });


        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                Geocoder geocoder = new Geocoder(CarPooling.this);
                List<Address> list = null;
                LatLng ll = marker.getPosition();
                try {
                    list = geocoder.getFromLocation(ll.latitude, ll.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Address address = list.get(0);
                marker.setTitle(address.getSubLocality() + "," + address.getLocality());
                marker.setSnippet(address.getCountryName());

                editText.setText(address.getAddressLine(0) + "," + address.getAddressLine(2) + "," + address.getAddressLine(3));
                selectedLocation = address.getAddressLine(0) + "," + address.getAddressLine(2) + "," + address.getAddressLine(3);
                selectedSubLocality = address.getSubLocality();
                marker.showInfoWindow();
            }
        });
        return (googleMap != null);

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


    public boolean servicesOK() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        1972).show();
            }

        } else {
            Toast.makeText(this, "Cant connect to google play services", Toast.LENGTH_SHORT).show();
        }
        return false;

    }


    private void gotoLocation(double lat,double lng,float zoom){
        LatLng latLng = new LatLng(lat,lng);

        MarkerOptions markerOptions =  new MarkerOptions()
                .position(new LatLng(lat, lng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.animateCamera(cameraUpdate);


          //  marker.remove();



        //marker = googleMap.addMarker(markerOptions);



    }


    private void gotoLocation(double lat,double lng,float zoom,String locality,String subLocality){
        LatLng latLng = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.animateCamera(cameraUpdate);
        setMarker(lat, lng, locality);

        editText.setText(locality);
        selectedLocation = locality;
        selectedSubLocality = subLocality;

    }




    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            gps_enabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            network_enabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enabled && !network_enabled) {
                // No network provider is enabled
                showSettingsAlert();
            } else {

                try {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }
                try {
                    network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }
            }

            if (gps_enabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            }
            if (network_enabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public  void setMarker(double lat,double lng,String locality){

        MarkerOptions markerOptions =  new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                .draggable(true);
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


        if(marker != null){
            marker.remove();
        }


        marker = googleMap.addMarker(markerOptions);
        //marker = googleMap.addMarker(markerOptions);
    }


    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // This needs to stop getting the location data and save the battery power.

                Log.e("pooling1","pooling1");
                locationManager.removeUpdates(locListener);

                float londitude =(float) location.getLongitude();
                float latitude = (float) location.getLatitude();
                String altitiude = "Altitiude: " + location.getAltitude();
                String accuracy = "Accuracy: " + location.getAccuracy();
                Log.e("pooling2","pooling2");
                String time = "Time: " + location.getTime();

                Geocoder geocoder = new Geocoder(CarPooling.this, Locale.getDefault());
                Log.e("pooling3","pooling3");
                List<Address> addresses = null;
                try {
                    Log.e("pooling4","pooling4"+latitude+londitude);
                    addresses = geocoder.getFromLocation(latitude, londitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("pooling5","pooling5");
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                String locality = addresses.get(0).getSubLocality();
                Log.e("pooling6","pooling6");
                String locationName = locality+","+cityName + ","+stateName+","+countryName;
                Log.e("location",latitude+londitude+altitiude+locationName);
                if(type.equals("start")){
                    gotoLocation(latitude,londitude,DEFAULTZOOM,locationName,locality);
                }
                else {
                    gotoLocation(latitude,londitude,11);
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }




}


