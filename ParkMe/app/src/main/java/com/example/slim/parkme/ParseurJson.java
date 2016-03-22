package com.example.slim.parkme;


import android.content.res.AssetManager;
import android.content.res.Resources;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by slim on 15/03/2016.
 */
public class ParseurJson {

    JsonReader reader;
    JsonParser jsonParser;
    JsonObject userArray;
    JsonArray objets;

    ParseurJson(String url,AssetManager r) throws IOException{
        InputStream is = r.open(url);
        reader = new JsonReader(new InputStreamReader(
               is ));

        jsonParser = new JsonParser();

        userArray = jsonParser.parse(reader).getAsJsonObject();

        objets = userArray.getAsJsonArray("elements");
    }

    public void parse(GoogleMap googlemap){
        for (JsonElement aUser : objets) {


            // On transforme les liste en objet pour lieux recuperer les valeurs
            JsonObject jsonObject = (JsonObject) aUser;

				/*
				System.out.println(jsonObject);
				*/
            if(jsonObject.has("lat") == true){
                //	System.out.println("lat : " + jsonObject.get("lat") + " lon : " + jsonObject.get("lon"));

                if(jsonObject.has("tags") == true){
                    JsonElement tags = jsonObject.get("tags");
                    JsonObject toto = (JsonObject) tags;

                    if(toto.has("name") == true){
                        //				System.out.println("name : " + toto.get("name"));

                        if(toto.has("parking") == true){
                            //					System.out.println("parking : " + toto.get("parking"));

                            if(toto.has("capacity") == true){
                                //						System.out.println("capacité : " + toto.get("capacity"));
                                // Affiche avec capacite

                                LatLng pos = new LatLng(jsonObject.get("lat").getAsDouble(), jsonObject.get("lon").getAsDouble());
                                String nom = toto.get("name").getAsString();
                                String typePark = toto.get("parking").getAsString();
                                String capacite = toto.get("capacity").getAsString();
                                //MarkerOptions mm = new MarkerOptions();
                                googlemap.addMarker(new MarkerOptions().title(nom).snippet(typePark + "\n" + capacite + " places").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(pos));

                            }
                            else{

                                //	Affiche parking sous-terrain avec couleur vert

                                LatLng pos = new LatLng(jsonObject.get("lat").getAsDouble(), jsonObject.get("lon").getAsDouble());
                                String nom = toto.get("name").getAsString();
                                String typePark = toto.get("parking").getAsString();
                                //MarkerOptions mm = new MarkerOptions();
                                googlemap.addMarker(new MarkerOptions().title(nom).snippet(typePark).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(pos));

                            }
                        }
                        //	On a le nom avec
                        else{

                            LatLng pos = new LatLng(jsonObject.get("lat").getAsDouble(), jsonObject.get("lon").getAsDouble());
                            String nom = toto.get("name").getAsString();
                            //MarkerOptions mm = new MarkerOptions();
                            googlemap.addMarker(new MarkerOptions().title(nom).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(pos));
                        }
                    }

                }
                //	pas d'info supplementaire on met que les coordonnees
                else{

                    LatLng pos = new LatLng(jsonObject.get("lat").getAsDouble(), jsonObject.get("lon").getAsDouble());
                    //MarkerOptions mm = new MarkerOptions();
                    googlemap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(pos));

                }

            }

        }
    }
}
