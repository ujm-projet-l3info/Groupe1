package com.example.slim.parkme;


import android.content.res.AssetManager;
import android.content.res.Resources;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by slim on 15/03/2016.
 */
public class ParseurJson {

    JsonReader reader;
    JsonParser jsonParser;
    JsonObject userArray;
    JsonArray objets;

    JsonArray jsonIdWay;
    JsonArray jsonId;

    // Les Lat Lon des marqueurs seul
    JsonArray jsonLatIdNode;
    JsonArray jsonLonIdNode;

    // Les way pour les polygone final
    JsonArray jsonIdWay2;
    JsonArray jsonLatIdWay;
    JsonArray jsonLonIdWay;

    int i;


    ParseurJson(String url,AssetManager r) throws IOException{
        InputStream is = r.open(url);
        reader = new JsonReader(new InputStreamReader(
                is ));

        jsonParser = new JsonParser();

        userArray = jsonParser.parse(reader).getAsJsonObject();

        objets = userArray.getAsJsonArray("elements");

        jsonIdWay = new JsonArray();
        jsonId = new JsonArray();

        // Les Lat Lon des marqueurs seul
        jsonLatIdNode = new JsonArray();
        jsonLonIdNode = new JsonArray();

        // Les way pour les polygone final
        jsonIdWay2 = new JsonArray();
        jsonLatIdWay = new JsonArray();
        jsonLonIdWay = new JsonArray();
        i = 1;

    }

    public void parse(GoogleMap googlemap){

        //	On recupere chaque liste d'element qui sont {...}
        for (JsonElement aUser : objets) {


            // On transforme les liste en objet pour mieux recuperer les valeurs
            JsonObject jsonObject = (JsonObject) aUser;

			/*
			System.out.println(jsonObject);
			*/
            /****************************************************/

            // Variable pour comparer si c'est un node ou way
            JsonPrimitive nomNode = new JsonPrimitive("node");
            JsonPrimitive nomWay = new JsonPrimitive("way");


            //recupere tout les id, lat et lon de tous les marqueurs
            if(jsonObject.get("type").equals(nomNode)){


                jsonId.add(jsonObject.get("id"));

                jsonLatIdNode.add(jsonObject.get("lat"));
                jsonLonIdNode.add(jsonObject.get("lon"));

                jsonIdWay2.add(jsonObject.get("id"));
                jsonLatIdWay.add(jsonObject.get("lat"));
                jsonLonIdWay.add(jsonObject.get("lon"));

                if(jsonObject.getAsJsonPrimitive("id").equals(new JsonPrimitive(385242926))){
                    System.out.println("il rentre bien ici!!!!!");
                }
                i++;
            }

            //recupere les id des noeuds pour faire les marqueurs en forme de polygones
            if(jsonObject.get("type").equals(nomWay)){


                // On stoque toute la liste des id des way dans un tableau de json
                // Chaque liste est separe par un -1
                jsonIdWay.addAll((JsonArray)jsonObject.get("nodes"));
                jsonIdWay.add(-1);

            }
        }

        // Supprime dans la liste Idnode ceux qui sont semblable Idway et supprime les Lat et Lon
        int taille = jsonId.size();
        for(int j = 0; j < taille; j++){
            for(int k = 0; k < jsonIdWay.size(); k++){

                if(jsonId.get(j).equals(jsonIdWay.get(k))){

                    jsonId.remove(j);
                    jsonLatIdNode.remove(j);
                    jsonLonIdNode.remove(j);
                    taille = jsonId.size();

                    j--;
                    k = jsonIdWay.size();
                }
            }

        }

        // Supprime les Lon, Lat et Id de Way qui sont semblable au Node
        int taille2 = jsonLatIdWay.size();
        for(int j = 0; j < jsonLatIdNode.size(); j++){
            for(int k = 0; k < taille2; k++){

                if(jsonLatIdNode.get(j).equals(jsonLatIdWay.get(k))){

                    jsonIdWay2.remove(k);
                    jsonLatIdWay.remove(k);
                    jsonLonIdWay.remove(k);
                    taille2 = jsonLatIdWay.size();

                    k = jsonLatIdWay.size();
                }
            }

        }


        // 2ieme parcours du fichier
        for (JsonElement aUser : objets) {


            // On transforme les liste en objet pour lieux recuperer les valeurs
            JsonObject jsonObject = (JsonObject) aUser;

				/*
				System.out.println(jsonObject);
				*/

            // Parcour la liste des Idnode
            for(int l = 0; l < jsonId.size(); l++){

                // Test si il est dans la liste des Idnode
                if(jsonObject.getAsJsonPrimitive("id").equals(jsonId.get(l))){

                    if(jsonObject.has("lat") == true){
                        //	System.out.println("lat : " + jsonObject.get("lat") + " lon : " + jsonObject.get("lon"));

                        if(jsonObject.has("tags") == true){
                            JsonElement tags = jsonObject.get("tags");
                            JsonObject toto = (JsonObject) tags;

                            if(toto.has("name") == true){
                                //	System.out.println("name : " + toto.get("name"));

                                if(toto.has("parking") == true){
                                    //	System.out.println("parking : " + toto.get("parking"));

                                    if(toto.has("capacity") == true){
                                        // System.out.println("capacité : " + toto.get("capacity"));
                                        // Affiche avec capacite

                                        LatLng pos = new LatLng(jsonObject.get("lat").getAsDouble(), jsonObject.get("lon").getAsDouble());
                                        String nom = toto.get("name").getAsString();
                                        String typePark = toto.get("parking").getAsString();
                                        String capacite = toto.get("capacity").getAsString();
                                        //MarkerOptions mm = new MarkerOptions();
                                        googlemap.addMarker(new MarkerOptions().title(nom).snippet(capacite + " places").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(pos));

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

                    //if du idnode
                }
                //for du idnode
            }
            //for du fichier
        }
        /***********************************************************************/
								/* Fin des marqueurs seul */
        					  /* Debut marqueurs polygone */
        /***********************************************************************/



    }
}


