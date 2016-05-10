package com.example.slim.parkme;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

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

                if (this.get(i).getFiabilité()==0){
                    this.remove(i);
                }
                else
                    this.get(i).setFiabilité(this.get(i).getFiabilité()-1);

        }
    }

    /**
     * Dessine sur la carte un marqueur pour chaque places signalées
     * @param m : GoogleMap
     */
    public void draw(GoogleMap m){
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getFiabilité()==1)
                m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.placerouge)).position(this.get(i).getLatlong()));
            if (this.get(i).getFiabilité()==2)
                m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.placejaune)).position(this.get(i).getLatlong()));
            if (this.get(i).getFiabilité()==3)
                m.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.placevert)).position(this.get(i).getLatlong()));
        }
    }

    public boolean add2(Place p){
        Location l1= new Location("place1");
        Location l2= new Location("place2");
        l1.setLatitude(p.getLatlong().latitude);
        l1.setLongitude(p.getLatlong().longitude);

        for (int i =0; i<this.size();i++){
            l2.setLatitude(this.get(i).getLatlong().latitude);
            l2.setLongitude(this.get(i).getLatlong().longitude);
            //System.out.println(l1.distanceTo(l2)+"       "+i);
            if (l1.distanceTo(l2)<5){
                if (this.get(i).getFiabilité()!=3){
                    this.get(i).setFiabilité(3);
                    return true;
                }
                return false;
            }
        }
        this.add(p);
        return true;
    }

}
