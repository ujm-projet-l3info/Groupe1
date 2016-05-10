package com.example.slim.parkme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by slim on 09/05/2016.
 */
public class RecupBD {


         ListPlaces listPlaces;
    BackGround b;

        RecupBD(ListPlaces p) {
            listPlaces = p;
            b = new BackGround();
            b.execute();
        }


        class BackGround extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {
//                String pseudo = params[0];
  //              String password = params[1];
                String data="";
                int tmp;

                try {
                    URL url = new URL("http://assandi-baco.top/Configuration/place.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.flush();
                    os.close();

                    InputStream is = httpURLConnection.getInputStream();
                    while((tmp=is.read())!=-1){
                        data+= (char)tmp;
                    }

                    is.close();
                    httpURLConnection.disconnect();

                    return data;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: "+e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception: "+e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    //System.out.println("\n\n\n\n "+s+"\n\n\n");
                    JSONArray jsonArray = new JSONArray(s);


                    for (int i = 0 ; i<jsonArray.length();i++){
                        listPlaces.add(new Place(new LatLng(jsonArray.getJSONObject(i).getDouble("lat"),jsonArray.getJSONObject(i).getDouble("lon")),jsonArray.getJSONObject(i).getString("pseudo"),jsonArray.getJSONObject(i).getInt("fiabilite")));
                    }
                   // System.out.println(listPlaces);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


}


