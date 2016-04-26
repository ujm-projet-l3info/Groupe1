package com.example.slim.parkme;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by slim on 16/04/2016.
 */

public class Place {
    private LatLng latlong;
    private int idUser;         //L'id de l'utilisateur qui a signalé la place
    private Date date;          //Date et heure du signalement
    private int fiabilité;      //Indice de fiabilité que la place soit encore dispo

    public Place(LatLng l, int idUser){
        latlong=l;
        this.idUser=idUser;
        date = new Date();
        fiabilité=3;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public LatLng getLatlong() {
        return latlong;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFiabilité() {
        return fiabilité;
    }

    public void setFiabilité(int fiabilité) {
        this.fiabilité = fiabilité;
    }

    public int getIdUser() {
        return idUser;
    }
}