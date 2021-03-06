package com.example.slim.parkme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
/*
public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static String pseudo;
    static String email;
    final String[] fragments ={
            "com.example.slim.parkme.Profil",
            "com.example.slim.parkme.MapsActivity",
            "com.example.slim.parkme.Commentaire",
            "com.example.slim.parkme.Parametres",
            "com.example.slim.parkme.Deconnexion"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView pseudoDrawerTV = (TextView) header.findViewById(R.id.pseudoDrawer);
        TextView mailDrawerTV = (TextView) header.findViewById(R.id.mailDrawer);
        pseudo = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        pseudoDrawerTV.setText("Bienvenue "+pseudo);
        mailDrawerTV.setText(email);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            tx.replace(R.id.id_fragment, Fragment.instantiate(DrawerActivity.this, fragments[0]));
            tx.commit();
        } else if(id == R.id.nav_map){
//            tx.replace(R.id.id_fragment, Fragment.instantiate(DrawerActivity.this, fragments[1]));
  //          tx.commit();
        } else if(id == R.id.nav_commentaire){
            tx.replace(R.id.id_fragment, Fragment.instantiate(DrawerActivity.this, fragments[2]));
            tx.commit();
        } else if(id == R.id.nav_parametre){
            tx.replace(R.id.id_fragment, Fragment.instantiate(DrawerActivity.this, fragments[3]));
            tx.commit();
        }
        else if (id == R.id.nav_deconnexion) {
            tx.replace(R.id.id_fragment, Fragment.instantiate(DrawerActivity.this, fragments[4]));
            tx.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
/*}*/
