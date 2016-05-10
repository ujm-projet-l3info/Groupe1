package com.example.slim.parkme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connexion extends Activity {

    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String PSEUDO=null, PASSWORD=null, EMAIL=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);
        name = (EditText) findViewById(R.id.main_name);
        password = (EditText) findViewById(R.id.main_password);
    }

    public void main_register(View v){
        startActivity(new Intent(this,Inscription.class));
    }

    public void main_login(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        if (Name.equals("") || Password.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez renseigner les champs.", Toast.LENGTH_LONG).show();
        }else {
            BackGround b = new BackGround();
            b.execute(Name, Password);
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
                URL url = new URL("http://assandi-baco.top/Configuration/login.php");
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
            String err=null;
            Name = name.getText().toString();
            Password = password.getText().toString();

            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                PSEUDO = user_data.getString("name");
                PASSWORD = user_data.getString("password");
                EMAIL = user_data.getString("email");
                System.err.println("Pseudo : "+Name+"Pass : "+Password);
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }
            if(Name.equals(PSEUDO) && Password.equals(PASSWORD)) {
                Intent i = new Intent(ctx, MapsActivity.class);
                i.putExtra("name", PSEUDO);
                i.putExtra("password", PASSWORD);
                i.putExtra("email", EMAIL);
                i.putExtra("err", err);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Le pseudo ou le mot de passe est incorrect.", Toast.LENGTH_LONG).show();
            }
        }
    }
}