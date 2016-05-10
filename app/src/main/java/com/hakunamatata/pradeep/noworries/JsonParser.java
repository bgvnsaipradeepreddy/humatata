package com.hakunamatata.pradeep.noworries;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pradeep on 18/4/16.
 */
public class JsonParser {

    public static ModelData parseLoginFeed(String content) throws JSONException {
        JSONObject object = null;
        ModelData modelData = new ModelData();

            object = new JSONObject(content);
            Boolean status = object.getBoolean("status");
            int userId = object.getInt("user_id");

            if(status.equals(true)){
                modelData.setLoginStatus("successful");
                modelData.setLoginUserId(userId);
                return modelData;
            }else {
                String errorMsg = object.getString("error_msg");
                modelData.setLoginStatus("unsuccessful");
                modelData.setLoginUserId(0);
                modelData.setLoginErrorMsg(errorMsg);
                return modelData;
            }
    }
    public static ModelData parseRegisterFeed(String content) throws JSONException {
        JSONObject object = null;
        ModelData modelData = new ModelData();
        Log.e("pradeepregister",content);

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");

        if(status.equals(true)){
            modelData.setRegisterStatus("successful");
            int userId = object.getInt("user_id");
            modelData.setRegisterUserId(userId);
            return modelData;
        }else {
            String errorMsg = object.getString("error_msg");
            Log.e("pradeepregister",errorMsg);
            modelData.setRegisterStatus("unsuccessful");
            modelData.setRegisterUserId(0);
            modelData.setRegisterErrorMsg(errorMsg);
            return modelData;
        }
    }

    public static ArrayList<String> parseStateFeed(String content) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("states");
        ArrayList<String> states = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            states.add(jsonObject.getString("state_name"));
            int id = jsonObject.getInt("state_id");

        }
            return states;

    }
    public static Map<String,Integer> parseStateIdMapFeed(String content) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("states");
        ArrayList<String> states = new ArrayList<>();
        Map<String,Integer> mapStateIds = new HashMap<String,Integer>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            states.add(jsonObject.getString("state_name"));
            int id = jsonObject.getInt("state_id");
            mapStateIds.put(jsonObject.getString("state_name"),id);
        }
        return mapStateIds;

    }



    public static ArrayList<String> parseDistrictFeed(String content,int stateId) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("districts");
        ArrayList<String> districts = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("state_id");
            if(id == stateId) {
                districts.add(jsonObject.getString("district_name"));
            }

        }
        return districts;
    }

    public static Map<String,Integer> parseDistrictIdMapFeed(String content) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("districts");
        ArrayList<String> districts = new ArrayList<>();
        Map<String,Integer> mapDistrictIds = new HashMap<String,Integer>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            districts.add(jsonObject.getString("district_name"));
            int id = jsonObject.getInt("district_id");
            mapDistrictIds.put(jsonObject.getString("district_name"),id);
        }
        return mapDistrictIds;

    }

    public static ArrayList<String> parseLocationFeed(String content,int districtId) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("locations");
        ArrayList<String> districts = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("district_id");
            if(id == districtId) {
                districts.add(jsonObject.getString("location_name"));
            }

        }
        return districts;
    }

    public static Map<String,Integer> parseLocationIdMapFeed(String content) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("locations");
        ArrayList<String> districts = new ArrayList<>();
        Map<String,Integer> mapDistrictIds = new HashMap<String,Integer>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            districts.add(jsonObject.getString("location_name"));
            int id = jsonObject.getInt("location_id");
            mapDistrictIds.put(jsonObject.getString("location_name"),id);
        }
        return mapDistrictIds;

    }

    public static ArrayList<String> parseSelectedLocationCount(String content) throws JSONException {

        JSONObject mainJsonObject = new JSONObject(content);
        JSONArray jsonArray = mainJsonObject.getJSONArray("locations");
        ArrayList<String> locations = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

                locations.add(jsonObject.getString("location_name"));


        }
        return locations;
    }


    public static String parseUserProfileFeed(String content) throws JSONException {

        JSONObject object = null;
        String result = "";

        object = new JSONObject(content);
        Boolean status = object.getBoolean("status");
        if(status==true){
            result="successful";
        }else {
            result ="unsuccessful";
        }


        return result;
    }

    public static ArrayList<ModelDataQuery> parseQueries(String queries) throws  JSONException {

        ArrayList<ModelDataQuery> output = new ArrayList<>();
        JSONObject mainJsonObject = new JSONObject(queries);
        //JSONObject jsonObject =mainJsonObject.getJSONObject("status");
        Boolean status = mainJsonObject.getBoolean("status");
        JSONArray jsonArray = mainJsonObject.getJSONArray("queries");

        if(status == true){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
                ModelDataQuery modelDataQuery = new ModelDataQuery();
                modelDataQuery.setUserName(jsonObjectChild.getString("user_name"));
                modelDataQuery.setDatePosted(jsonObjectChild.getString("date_created"));
                modelDataQuery.setImageLocation(jsonObjectChild.getString("user_image"));
                modelDataQuery.setQueryTitle(jsonObjectChild.getString("query_title"));
                modelDataQuery.setQueryContent(jsonObjectChild.getString("query_content"));
                output.add(modelDataQuery);
            }
        }
        return output;
    }


    public static ArrayList<ModelDataQuery> parseEmergencyHelp(String queries) throws  JSONException {

        ArrayList<ModelDataQuery> output = new ArrayList<>();
        JSONObject mainJsonObject = new JSONObject(queries);
        //JSONObject jsonObject =mainJsonObject.getJSONObject("status");
        Boolean status = mainJsonObject.getBoolean("status");
        JSONArray jsonArray = mainJsonObject.getJSONArray("queries");

        if(status == true){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
                ModelDataQuery modelDataQuery = new ModelDataQuery();
                modelDataQuery.setUserName(jsonObjectChild.getString("user_name"));
                modelDataQuery.setImageLocation(jsonObjectChild.getString("user_image"));
                modelDataQuery.setQueryTitle(jsonObjectChild.getString("query_title"));
                modelDataQuery.setQueryContent(jsonObjectChild.getString("query_content"));
                output.add(modelDataQuery);
            }
        }
        return output;
    }

    public static ArrayList<String> parseTempData(String data) throws JSONException{
        ArrayList<String> output = new ArrayList<>();
        JSONObject mainJsonObject = new JSONObject(data);
        JSONObject tempJson = mainJsonObject.getJSONObject("main");
        String temperature = Double.toString(tempJson.getDouble("temp"));
        output.add(temperature);
        Log.e("temperature",temperature);
        JSONArray jsonArray = mainJsonObject.getJSONArray("weather");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        output.add(jsonObject.getString("description"));

        return output;

    }


    public static ArrayList<ModelDataTrivia> parseTrivia(String data) throws  JSONException {

        ArrayList<ModelDataTrivia> output = new ArrayList<>();
        JSONObject mainJsonObject = new JSONObject(data);
        //JSONObject jsonObject =mainJsonObject.getJSONObject("status");
        Boolean status = mainJsonObject.getBoolean("status");
        JSONArray jsonArray = mainJsonObject.getJSONArray("trivia");

        if(status == true){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
                ModelDataTrivia modelDataTrivia = new ModelDataTrivia();
                modelDataTrivia.setUserName(jsonObjectChild.getString("user_name"));
                //modelDataTrivia.setDatePosted(jsonObjectChild.getString("date_created"));
                modelDataTrivia.setImageLocation(jsonObjectChild.getString("user_image"));
                modelDataTrivia.setDescription(jsonObjectChild.getString("description"));
                output.add(modelDataTrivia);
            }
        }
        return output;
    }

}
