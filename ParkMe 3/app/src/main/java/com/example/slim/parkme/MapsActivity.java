package com.example.slim.parkme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements LocationListener,AdapterView.OnItemClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Button search;
    private EditText recherche;
    private String pseudo;
    private int Score;
    private Button test;

    private static final String LOG_TAG = "Google Places Autocomp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyAU9ShujnIg3IDQxtPr7Q1qOvFVdwNmWc4";

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
    /**
     * Boutton pour signaler une place
     */
    FloatingActionButton signPlace;
    FloatingActionButton refresh;

    /**
     * Listes des places
     */
    static ListPlaces listPlaces;

    RecupBD recupBD;
    EnvoiePlace envoieP;

    ArrayList<Adress> listAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listPlaces = new ListPlaces();

        pseudo = "slim";

        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoComp);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this));
        autoCompView.setOnItemClickListener(this);

        setUpMapIfNeeded();

        signPlace =(FloatingActionButton) findViewById(R.id.fab);
        refresh =(FloatingActionButton) findViewById(R.id.fabRefresh);

        final Context c = this;

        //Récuperation des places depuis la base de données
        recupBD = new RecupBD(listPlaces);

        //recupBD.b.execute();

        signPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listPlaces.add2(new Place(latLng,"1")))
                    Toast.makeText(c,"Place déja signalée",Toast.LENGTH_LONG).show();
                listPlaces.draw(mMap);
                envoieP = new EnvoiePlace(new Place(latLng,pseudo));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                //listPlaces=recupBD.listPlaces;
                //System.out.println(listPlaces.get(0).getLatlong().toString()+"  "+listPlaces.get(0).getFiabilité());
                listPlaces.draw(mMap);
                dessineParking();
            }
        });

        resources=this.getResources();

//        final String addresse = recherche.toString();
        latLng=new LatLng(0,0);

        helpBuilder = new AlertDialog.Builder(this);

        //On place un timer infini qui met a jour la fiabilité des places toutes les minutes
        new CountDownTimer(60000, 60000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                mMap.clear();
                dessineParking();

                recupBD = new RecupBD(listPlaces);
                listPlaces.draw(mMap);
                //recupBD.b.execute();

                this.start();
            }
        }.start();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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


    private void dessineParking(){
        try {

            parseur = new ParseurJson("parking.json", this.getResources().getAssets());
            parseur.parse(mMap);
            // Initialisation des variables
            int position[] = new int[100];
            double[] centroid = { 0.0, 0.0 };
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
                            	/* pour calculer le marqueur au centre du polygone */
                            centroid[0] += parseur.jsonLatIdWay.get(n).getAsDouble();
                            centroid[1] += parseur.jsonLonIdWay.get(n).getAsDouble();
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


						/* calcule le centre  */
                        centroid[0] = centroid[0] / conte;
                        centroid[1] = centroid[1] / conte;

                        conte = 0;
                        n = parseur.jsonIdWay2.size()-1;

                        /* ajoute le marqueur au centre du polygone avec l'image P */
                        Bitmap b= BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("p", "drawable", getPackageName()));
                        Bitmap resized = Bitmap.createScaledBitmap(b, 50, 50, false);
                        mMap.addMarker((new MarkerOptions().title("Parking exterieur").position(new LatLng(centroid[0],centroid[1])).icon(BitmapDescriptorFactory.fromBitmap(resized))));


					/* remet a 0 pour le prochain marqueur */
                        centroid[0] = 0;
                        centroid[1] = 0;
                    }

                    //if du idway
                }
                //for du idway
            }
        }catch (IOException e){

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
        //if (location!=null){
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latLng.latitude,latLng.longitude),15.5f,45f,0f)));
        //}


        //File f =new File("parking.json");

        dessineParking();

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



    public static LatLng getLocationFromString(String address)
            throws JSONException {

        // "http://maps.google.com/maps/api/geocode/json?address="+ URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false"

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("http://maps.google.com/maps/api/geocode/json?address="+ URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false");

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error  Places API URL", e);
            //return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            //return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            // Extract the Place descriptions from the results
            double lng = ((JSONArray) jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            return new LatLng(lat, lng);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
            return null;
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
//        Toast.makeText(this,listAddress.get(position).latlong.toString(), Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.placevert)).position(listAddress.get(position).latlong));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(listAddress.get(position).latlong,15.5f,45f,0f)));
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:fr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));


            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
             Log.e(LOG_TAG, "Error  Places API URL", e);
            return resultList;
        } catch (IOException e) {
             Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(new Adress(predsJsonArray.getJSONObject(i).getString("description"),getLocationFromString(predsJsonArray.getJSONObject(i).getString("description"))));
            }
        } catch (JSONException e) {
             Log.e(LOG_TAG, "Cannot process JSON results", e);
        }



        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context) {
            super(context, android.R.layout.simple_dropdown_item_1line);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index).toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        listAddress = resultList;

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

}
