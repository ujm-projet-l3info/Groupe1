package com.example.slim.parkme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

//import com.google.android.map.GeoPoint;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Button search;
    private EditText recherche;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /**
     * Latitude longitude position courante
     */
    LatLng latLng;

    AlertDialog.Builder helpBuilder;
    ParseurJson parseur;
    Resources resources;
    AutoCompleteTextView autoComp;
    /**
     * Boutton pour signaler une place
     */
    FloatingActionButton signPlace;

    /**
     * Listes des places
     */
    ListPlaces listPlaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listPlaces = new ListPlaces();

        setUpMapIfNeeded();

        //search = (Button) findViewById(R.id.button);
        //recherche = (EditText) findViewById(R.id.editText);
        signPlace =(FloatingActionButton) findViewById(R.id.fab);



        signPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPlaces.add(new Place(latLng,1));
                listPlaces.draw(mMap);
            }
        });
        resources=this.getResources();

//        final String addresse = recherche.toString();
        latLng=new LatLng(0,0);

        helpBuilder = new AlertDialog.Builder(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        autoComp = (AutoCompleteTextView) findViewById(R.id.autoComp);

        //autoComp.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));


    }




    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            //Permet de voir sa position sur la carte
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setTrafficEnabled(true);
                //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                setUpMap();

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        // Get the name of the best provider

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        String provider = locationManager.getBestProvider(criteria,true);
        Location location = locationManager.getLastKnownLocation(provider);

        // Get latitude of the current location
       latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latLng.latitude,latLng.longitude),15.5f,45f,0f)));

        //File f =new File("parking.json");

        try {

                parseur = new ParseurJson("parking.json", this.getResources().getAssets());
                parseur.parse(mMap);
            // Initialisation des variables
            int position[] = new int[100];
            int conte = 0;

            PolygonOptions polygone = new PolygonOptions();


            // Parcour la liste des idway
            for(int m = 0; m < parseur.jsonIdWay.size(); m++){

                // Parcour la liste des jsonIdWay22
                for(int n = 0; n < parseur.jsonIdWay2.size(); n++){

                    // Arret pour les id des polygones
                    if(!(parseur.jsonIdWay.get(m).equals(new JsonPrimitive(-1)))){
                        // Test si les id sont egaux
                        if(parseur.jsonIdWay.get(m).equals(parseur.jsonIdWay2.get(n))){

                            // Ajoute dans polygone les coordonnées d'un point
                            polygone.add(new LatLng(parseur.jsonLatIdWay.get(n).getAsDouble(),parseur.jsonLonIdWay.get(n).getAsDouble()));
                            // Stoque la position pour recuperer la position du 1ier point pour le polygone
                            position[conte] = n;
                            conte++;
                            // Stop la boucle vu qu'il n'y a que des Id unique
                            n = parseur.jsonIdWay2.size()-1;

                        }
                        // On est arrivé au dernier point du polygone
                    }else if(parseur.jsonIdWay.get(m).equals(new JsonPrimitive(-1))){
                        // On ajoute le 1ier point pour fermer le polygone
                        polygone.add(new LatLng(parseur.jsonLatIdWay.get(position[0]).getAsDouble(),parseur.jsonLonIdWay.get(position[0]).getAsDouble()));

                        // La couleur du polygone
                        polygone.fillColor( ContextCompat.getColor(this, R.color.colorZoneParking));
                        polygone.strokeColor( ContextCompat.getColor(this, R.color.colorZoneParkingFonce) );
                        polygone.strokeWidth( 5 );

                        mMap.addPolygon( polygone );

                        // On cree un nouveau polygone, initialisation des variables
                         polygone = new PolygonOptions();

                        conte = 0;

                        n = parseur.jsonIdWay2.size()-1;
                    }

                    //if du idway
                }
                //for du idway
            }
        }catch (IOException e){

        }

        //On dessine maintenant toutes les places dispo signalées
        listPlaces.draw(mMap);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.slim.parkme/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.slim.parkme/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng= new LatLng(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
