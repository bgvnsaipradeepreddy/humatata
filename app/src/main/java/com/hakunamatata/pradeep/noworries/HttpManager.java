package com.hakunamatata.pradeep.noworries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pradeep on 17/4/16.
 */
public class HttpManager {

    public static String getData(RequestPackage requestPackage){
        BufferedReader reader  = null;
        String uri = requestPackage.getUri();
        if(requestPackage.getMethod().equals("GET")){
            uri += "?" + requestPackage.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
            System.out.println(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(requestPackage.getMethod());
            if(requestPackage.getMethod().equals("POST")){
                httpURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                outputStreamWriter.write(requestPackage.getEncodedParams());
                outputStreamWriter.flush();
            }
            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String data;
            while ((data=reader.readLine()) != null){
                stringBuilder.append(data + "\n");
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
