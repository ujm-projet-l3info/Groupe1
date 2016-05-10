package slim.example.com.parkme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BACO ASSANDI on 10/05/2016.
 */
public class Parametres extends Fragment {

    private String pseudo;
    private Button b_supprimerCompte;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View supprProfil =  inflater.inflate(R.layout.parametres, container, false);

        b_supprimerCompte = (Button) supprProfil.findViewById(R.id.b_supprimer_compte);

        b_supprimerCompte.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Suppression de compte")
                        .setMessage("Voulez-vous vraiment supprimer votre compte?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                suppr_compte();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        return supprProfil;
    }

    public void suppr_compte(){
        pseudo = DrawerActivity.pseudo;
        BackGround b = new BackGround();
        b.execute(pseudo);

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://assandi-baco.top/Configuration/supprCompte.php");
                String urlParams = "name="+name;

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
                s="Votre compte a bien été supprimé.";
            }
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
