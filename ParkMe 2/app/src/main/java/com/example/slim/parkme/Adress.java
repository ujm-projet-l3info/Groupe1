package com.example.slim.parkme;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by slim on 08/05/2016.
 */
public class Adress {
    String nom;
    LatLng latlong;

    Adress(String n,LatLng ln){
        nom = n;
        latlong = ln;
    }

    @Override
    public String toString(){
        return nom;
    }
}
