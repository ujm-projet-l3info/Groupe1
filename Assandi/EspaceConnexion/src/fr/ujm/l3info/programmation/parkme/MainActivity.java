package fr.ujm.l3info.programmation.parkme;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView connecter;
	public static final int RESULTAT_MAIN_ACTIVITY = 1;
    
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
 
 		//Appel de la page de Controle du login et du mot de passe
        startActivityForResult(new Intent(MainActivity.this, ControlLoginPass.class), RESULTAT_MAIN_ACTIVITY);
 
        connecter = new TextView(this);
        setContentView(connecter);
    }
 
    private void startup(Intent i) {
		// Récupèration de l'identifiant envoyé depuis la page ControlLoginPass        
		int user = i.getIntExtra("userid", -1);
		 
		// Affichage de l'identifiant en question.
        connecter.setText("Connexion réussie. \n Votre identifiant est : "+ String.valueOf(user));
    }
 
 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        if(requestCode == RESULTAT_MAIN_ACTIVITY && resultCode == RESULT_CANCELED){  
            finish(); 
        }
        else { 
            startup(data);
        }
    }
}