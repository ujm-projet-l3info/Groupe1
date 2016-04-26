package com.example.slim.parkme;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe qui contient la liste des places signalées
 * Created by slim on 16/04/2016.
 */
public class ListPlaces extends ArrayList<Place> {

    /**
     * Methode qui va mettre a jour la fiabilité des places.
     */
    public void updateFiab(){
        for (int i = 0; i < this.size(); i++) {
            if (((new Date()).getTime()/1000)-(this.get(i).getDate().getTime()/1000)<600){
                if (this.get(i).getFiabilité()==0){
                    this.remove(i);
                }
                else
                    this.get(i).setFiabilité(this.get(i).getFiabilité()-1);
            }
        }
    }

    /**
     * Dessine sur la carte un marqueur pour chaque places signalées
     * @param m : GoogleMap
     */
    public void draw(GoogleMap m){
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getFiabilité()==1)
                m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(this.get(i).getLatlong()));
            if (this.get(i).getFiabilité()==2)
                m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(this.get(i).getLatlong()));
            if (this.get(i).getFiabilité()==3)
                m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(this.get(i).getLatlong()));
        }
    }


}
