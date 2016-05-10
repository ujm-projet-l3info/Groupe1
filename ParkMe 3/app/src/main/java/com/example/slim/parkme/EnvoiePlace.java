package com.example.slim.parkme;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by slim on 09/05/2016.
 */
public class EnvoiePlace {

    Place place;
    BackGround b;

    EnvoiePlace(Place p) {
        b = new BackGround();
        place = p;
        b.execute();
    }


    class BackGround extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://assandi-baco.top/Configuration/signplace.php");
                String param = "lat=" + place.getLatlong().latitude + "&lon=" + place.getLatlong().longitude + "&pseudo=" + place.getIdUser() + "&date=" + place.getDate();
                System.out.println(param);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(param.getBytes());
                os.flush();
                os.close();



                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }
    }
}
