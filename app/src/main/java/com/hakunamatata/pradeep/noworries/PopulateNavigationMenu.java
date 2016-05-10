package com.hakunamatata.pradeep.noworries;

/**
 * Created by pradeep on 24/4/16.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class PopulateNavigationMenu extends ArrayAdapter {
    LayoutInflater li;
    String[] title;
    int[] image;
    TextView titletext;
    ImageView titleimage;

    public PopulateNavigationMenu(Activity activity, String[] mNavTitle, int[] mNavImage) {

        super(activity, R.layout.menu_navigation_populate,mNavTitle);
        title=mNavTitle;
        image=mNavImage;
        li = activity.getWindow().getLayoutInflater();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = li.inflate(R.layout.menu_navigation_populate,parent,false);
        titletext=(TextView)convertView.findViewById(R.id.tvPopulateNavigationMenu);
        titleimage=(ImageView)convertView.findViewById(R.id.ivPopulateNavigationMenu);
        titleimage.setImageResource(image[position]);
        titletext.setText(title[position]);
        return convertView;
    }
}


