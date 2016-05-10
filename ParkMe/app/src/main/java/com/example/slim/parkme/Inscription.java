package com.example.slim.parkme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Inscription extends Activity {

    EditText name, password, email;
    String Name, Password, Email;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
    }

    public void register_register(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        Email = email.getText().toString();
        if(Name.equals("") || Password.equals("") || Email.equals("")){
            Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires.", Toast.LENGTH_LONG).show();
        } else {
            BackGround b = new BackGround();
            b.execute(Name, Password, Email);
        }
    }
    public void inscription_login(View v){
        startActivity(new Intent(this, Connexion.class));
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String email = params[2];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://assandi-baco.top/Configuration/register.php");
                String urlParams = "name="+name+"&password="+password+"&email="+email;

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
                s="Inscription réussie avec succès.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }
    }

}