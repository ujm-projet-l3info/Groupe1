package com.example.slim.parkme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BACO ASSANDI on 09/05/2016.
 */
public class Commentaire extends Activity {
    private EditText editCommentaire;
    private Button b_poster_commentaire;
    private String commentaireTape, champVide = "", pseudo;
    private ImageButton b_ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentaires);
        editCommentaire = (EditText) findViewById(R.id.edit_commentaire);

        b_poster_commentaire = (Button) findViewById(R.id.poster_commentaire);
        b_poster_commentaire.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                commentaireTape = editCommentaire.getText().toString();
                if (commentaireTape.equals(champVide)){
                    Toast.makeText(getApplicationContext(), "Veuillez tapé un commentaire s'il vous plaît.", Toast.LENGTH_LONG).show();
                } else {
                    register_commentaire();
                    Toast.makeText(getApplicationContext(), "Commentaire envoyé avec succès", Toast.LENGTH_LONG).show();
                }
            }
        });

        b_ret =(ImageButton) findViewById(R.id.retour);
        b_ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void register_commentaire(){
        commentaireTape = editCommentaire.getText().toString();
        pseudo = MapsActivity.pseudo;
        if(commentaireTape.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez tapé un commentaire s'il vous plaît.", Toast.LENGTH_LONG).show();
        } else {
            BackGround b = new BackGround();
            b.execute(commentaireTape, pseudo);
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String com = params[0];
            String name = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://assandi-baco.top/Configuration/commentaire.php");
                String urlParams = "com="+com+"&pseudo="+name;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                s="Commentaire envoyé avec succès.";
            }
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
