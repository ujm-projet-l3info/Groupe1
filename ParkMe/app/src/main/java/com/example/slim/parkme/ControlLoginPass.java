/**
 * 
 */
package com.example.slim.parkme;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Class permettant de controler l'identifiant et le mot de passe saisie afin de permettre la connexion vers l'appliation;
 * @author BACO assandi
 *
 */
public class ControlLoginPass extends Activity{
	// Lien vers votre page php sur votre serveur
	private static final String	UPDATE_URL	= "http://assandi-baco.top/Configuration/parkme.php";
	
	/**
	 * Boite de dialogue lor de la tentative d'accès à la base de données.
	 */
	public ProgressDialog progressDialog;
	
	/**
	 * Variable permettant de récuperer le login saisie par l'utilisateur.
	 */
	private EditText loginText;
	
	/**
	 * Variable permettant de récuperer la mot de passe saisie par l'utilisateur.
	 */
	private EditText passwordText;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actconnex);
		
		// Initialisation de la barre de progression lors de l'acccès à la base de données.
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Patientez SVP...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);

		loginText = (EditText) findViewById(R.id.login);
		passwordText = (EditText) findViewById(R.id.pass);
		Button button = (Button) findViewById(R.id.connecter);

		button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				int tailleLogin = loginText.getText().length();
				int taillePassword = passwordText.getText().length();
				
				// Teste si le champ login et mot de passe ne sont pas vide, on  lance la tentative de connexion
				if (tailleLogin > 0 && taillePassword > 0){
					progressDialog.show();
					
					String user = loginText.getText().toString();
					String pass = loginText.getText().toString();
					// Tentative de connexion avec le login et mot de passe renseignée
					ConnexionLoginPass(user, pass);
				}
				else
					// Si les champs sont vides
					createDialog("Erreur", "Veuillez entrez votre login et mot de passe SVP");
			}
		});
		
		button = (Button) findViewById(R.id.quitter);
		button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				// Appel de la fonction pour quitter l'application.
				quitterApplication(false, null);
			}
		});

	}
	/**
	 * Méthode permettant de quitter l'application
	 * @param success
	 * @param i
	 */
	private void quitterApplication(boolean ok, Intent intent){
		// On envoie un résultat qui va permettre de quitter l'application
		setResult((ok) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, intent);
		finish();
	}
	
	
	/**
	 * Méthode permettant de créer un pop up.
	 * @param title
	 * @param text
	 */
	private void createDialog(String titre, String message){
		AlertDialog ad = new AlertDialog.Builder(this).setPositiveButton("Ok", null).setTitle(titre).setMessage(message).create();
		ad.show();

	}
	
	/**
	 * Méthode permettant de se connecter.
	 * @param login
	 * @param pass
	 */
	private void ConnexionLoginPass(final String login, final String pass){
		
		final String passCryptee = md5(pass);
		Thread thread = new Thread(){
			public void run(){

				Looper.prepare();
				//Connexion au serveur.
				//DefaultHttpClient client = new DefaultHttpClient();
				//HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

				//HttpResponse response;
				//HttpEntity entite;



				try{
					// On se connecte au serveur.
					/*HttpPost post = new HttpPost(UPDATE_URL);
					List<NameValuePair> loginPass = new ArrayList<NameValuePair>();
					loginPass.add(new BasicNameValuePair("login", login));
					loginPass.add(new BasicNameValuePair("password", passCryptee));
					post.setHeader("Content-Type", "application/x-www-form-urlencoded");
					// On passe le login et le mot de passe pour les envoyer au serveur en mode post.
					post.setEntity(new UrlEncodedFormEntity(loginPass, HTTP.UTF_8));
					// Récupération des données envoyés par le serveur
					response = client.execute(post);
					entite = response.getEntity();
					InputStream input = entite.getContent();
					lireDonnees(input);
					input.close();
					if (entite != null)
						entite.consumeContent();*/
					URL url = new URL(UPDATE_URL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					OutputStream ops = conn.getOutputStream();

				}
				catch (Exception e){
					progressDialog.dismiss();
					createDialog("Erreur", "Impossible d'établir la connexion avec le serveur, vérifiez votre connexion internet");
				}
				Looper.loop();
			}
		};
		thread.start();
	}
	
	/**
	 * Méthode permettant de lire les données reçus depuis le serveur.
	 * @param in
	 */
	private void lireDonnees(InputStream input){
		// On traduit le résultat d'un flux
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;
		try{
			sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();
			LoginContentHandler lch = new LoginContentHandler();
			xr.setContentHandler(lch);
			xr.parse(new InputSource(input));

		}
		catch (ParserConfigurationException e){
		}
		catch (SAXException e){
		}
		catch (IOException e){
		}
	}
	
	/**
	 * Méthode permettant de crypter un mot de passe en md5.
	 * @param pass
	 * @return String
	 */
	private String md5(String pass){
		MessageDigest digest;
		try{
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(pass.getBytes());
			byte[] a = digest.digest();
			int longueur = a.length;
			StringBuilder sb = new StringBuilder(longueur << 1);
			for (int i = 0; i < longueur; i++){
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Class interne permettant de traiter les données reçus depuis le serveur.
	 * @author BACO assandi
	 *
	 */
	private class LoginContentHandler extends DefaultHandler{
		private boolean	login = false;
		private int	idUtilisateur;
		private boolean	erreurServeur = false;

		public void startElement(String n, String localName, String qName, Attributes atts)

		throws SAXException{

			if (localName == "login")
				login = true;
			if (localName == "error"){
				progressDialog.dismiss();
				switch (Integer.parseInt(atts.getValue("value"))){
					case 1:
						createDialog("Erreur", "Impossible de se connecter à la base de données");
						break;
					case 2:
						createDialog("Erreur", "Table inexistant dans la base de données");
						break;
					case 3:
						createDialog("Erreur Connexion", "Login et Mot de passe invalide");
						break;
				}
				erreurServeur = true;

			}
			// Si la connexion s'est bien déroulé, on récupère l'identifiant de l'utilisateur.
			if (localName == "user" && login && atts.getValue("id") != ""){
				idUtilisateur = Integer.parseInt(atts.getValue("id"));
			}
		}
		
		public void endElement(String n, String localName, String qName) throws SAXException{
			if (localName == "login"){
				login = false;
				if (!erreurServeur){
					progressDialog.dismiss();
					Intent i = new Intent();
					i.putExtra("userid", idUtilisateur);
					quitterApplication(true, i);
				}
			}
		}

		public void characters(char ch[], int start, int length){
		}

		public void startDocument() throws SAXException{
		}

		public void endDocument() throws SAXException{
		}

	}

}