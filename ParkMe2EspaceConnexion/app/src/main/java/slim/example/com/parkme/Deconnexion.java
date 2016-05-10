package slim.example.com.parkme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by BACO ASSANDI on 09/05/2016.
 */
public class Deconnexion extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View deconnexion =  inflater.inflate(R.layout.connexion, container, false);
        System.exit(0);
        return deconnexion;
    }
}
