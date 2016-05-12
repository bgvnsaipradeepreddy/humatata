package com.hakunamatata.pradeep.noworries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by pradeep on 23/4/16.
 */
public class JsonUtility {


    public static String constructJSONUserSelectPlaces(List<ModelData> locations) {

        JSONArray jsonArray = new JSONArray();

        try {
            for(int i =0;i<locations.size();i++){
                ModelData modelData = new ModelData();
                JSONObject obj = new JSONObject();
                modelData = locations.get(i);
                obj.put("location_name", modelData.getLocationName());
                obj.put("location_id", modelData.getLocationId());
                obj.put("user_id", modelData.getUserId());
                jsonArray.put(obj);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(obj);
        JSONObject mainJsonObject = new JSONObject();
        try {
            mainJsonObject.put("locations", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mainJsonObject.toString();
    }

    public static  String constructJSONQuery(Map<String,String> data) throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("user_id",data.get("user_id"));
        obj.put("place_id",data.get("place_id"));
        obj.put("date",data.get("date"));
        obj.put("title",data.get("title"));
        obj.put("query",data.get("query"));


        return obj.toString();

    }


    public static  String constructJSONEmergency(Map<String,String> data) throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("user_id",data.get("user_id"));
        obj.put("place_id",data.get("place_id"));
        obj.put("title",data.get("title"));
        obj.put("query",data.get("query"));


        return obj.toString();

    }

    public static  String constructJSONTrivia(Map<String,String> data) throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("user_id",data.get("user_id"));
        obj.put("place_id",data.get("place_id"));
        obj.put("trivia",data.get("trivia"));


        return obj.toString();

    }


    public static  String constructJSONEvent(Map<String,String> data) throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("user_id",data.get("user_id"));
        obj.put("place_id",data.get("place_id"));
        obj.put("date",data.get("date"));
        obj.put("event",data.get("event"));
        obj.put("event_title",data.get("event_title"));


        return obj.toString();

    }

}
