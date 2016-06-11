package com.hakunamatata.pradeep.noworries;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by pradeep on 27/5/16.
 */
public class FragmentLetsGo extends Fragment{


    View view;
    int userId,placeId;
    String location;
    ListView listView;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    public static FragmentLetsGo newInstance(int userId, int placeId, String location) {
        Log.e("letsgo1","lets1");
        FragmentLetsGo fragment = new FragmentLetsGo();
        Bundle args = new Bundle();
        args.putString("place", location);
        args.putInt("placeId", placeId);
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentLetsGo() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("letsgo2","lets2");
        location = getArguments().getString("place");
        userId = getArguments().getInt("userId");
        Log.e("fragmemtq", "frq" + userId);
        placeId = getArguments().getInt("placeId");
        view = inflater.inflate(R.layout.letsgo_fragment, container, false);


        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabFragmentLetsGo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("place", location);
                bundle.putInt("place_id", placeId);
                bundle.putInt("user_id", userId);

                Intent intent = new Intent("com.hakunamatata.pradeep.noworries.POOLREQUEST");
                //Intent intent = new Intent("com.hakunamatata.hakunamatata.EXPANDEXAMPLE");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }
}
