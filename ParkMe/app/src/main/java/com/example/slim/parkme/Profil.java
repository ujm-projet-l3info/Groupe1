package com.example.slim.parkme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.TextView;
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
public class Profil extends Activity {
    private EditText pseudoTV, emailTV, pass, passConfirm;
    private Button b_modifier;
    private ImageButton b_ret;
    private String passe, passeConfirm, pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        pseudoTV = (EditText) findViewById(R.id.editPseudoNo);
        emailTV = (EditText) findViewById(R.id.edit_mail_no);
        pass = (EditText) findViewById(R.id.new_password);
        passConfirm = (EditText) findViewById(R.id.confirm_new_password);
        b_modifier = (Button) findViewById(R.id.b_modifier);

        b_ret =(ImageButton) findViewById(R.id.retour);
        b_ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pseudoTV.setText(MapsActivity.pseudo);
        emailTV.setText(MapsActivity.email);

        b_modifier.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                change_pass();
            }
        });

    }
/*
    @Override
    public void onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profil =  inflater.inflate(R.layout.profil, container, false);
        pseudoTV = (EditText) profil.findViewById(R.id.editPseudoNo);
        emailTV = (EditText) profil.findViewById(R.id.edit_mail_no);
        pass = (EditText) profil.findViewById(R.id.new_password);
        passConfirm = (EditText) profil.findViewById(R.id.confirm_new_password);
        b_modifier = (Button) profil.findViewById(R.id.b_modifier);

        pseudoTV.setText(DrawerActivity.pseudo);
        emailTV.setText(DrawerActivity.email);

        b_modifier.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                change_pass();
            }
        });

        return profil;
    }
*/
    public void change_pass(){
        passe = pass.getText().toString();
        passeConfirm = passConfirm.getText().toString();
        pseudo = MapsActivity.pseudo;

        if(passe.equals("") || passeConfirm.equals("")){
            Toast.makeText(getApplicationContext(), "Champs obligatoires pour modifier le mot de passe", Toast.LENGTH_LONG).show();
        } else {
            if(!passe.equals(passeConfirm)){
                Toast.makeText(getApplicationContext(), "Le nouveau mot de passe et sa confirmation ne sont pas identiques.", Toast.LENGTH_LONG).show();
            } else {
                BackGround b = new BackGround();
                b.execute(pseudo,passe);
            }
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://assandi-baco.top/Configuration/changePass.php");
                String urlParams = "name="+name+"&password="+password;

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
                s="Votre mot de passe a bien été modifié.";
            }
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
