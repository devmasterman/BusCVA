package net.rutas.morelos.app.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import net.rutas.morelos.app.R;
import net.rutas.morelos.app.adapters.MenuAdapter;
import net.rutas.morelos.app.bo.IPreProcesarBO;
import net.rutas.morelos.app.bo.impl.PreProcesarBO;
import net.rutas.morelos.app.utils.FileUtils;
import net.rutas.morelos.app.utils.IActivityUtil;

public class MenuActivity extends AppCompatActivity implements IActivityUtil {

    private  LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// para quitar titulo
        setContentView(R.layout.activity_menu);

        MenuAdapter menuAdapter = new MenuAdapter(this);
        final Context ctx=this;
        ListView listaItemsMenuPrincipal = (ListView) findViewById(R.id.listaItemsMenu);
        listaItemsMenuPrincipal.setAdapter(menuAdapter);
        iniciarComponentes();

        listaItemsMenuPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Intent i=null;
                switch (position) {
                    case 0:
                        showWindowModal("Modulo en Construcción", null);
                        break;

                    case 1:
                        /**
                         * se obtiene el objeto Location Manager y verifica si los servicios GPS Y RED están
                         * Disponibles, sino lo están nos manda al Settings.
                         */
                        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                            showWindowModal("Deseas activar el GPS para utilizar todas sus funciones?",
                                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }else if(!isOnline()){
                            showWindowModal("No estas conectado a una red, por favor verifica.",null);
                            return;
                        }
                        else {
                            i = new Intent(getApplicationContext(), DireccionesActivity.class);
                            startActivity(i);
                        }
                        break;

                    case 2:
                        showWindowModal("Modulo en Construcción", null);
                        break;

                    case 3:
                        showWindowModal("Deber? Enviar Info", null);
                        break;

                    case 4:
                        /**
                         * se obtiene el objeto Location Manager y verifica si los servicios GPS Y RED están
                         * Disponibles, sino lo están nos manda al Settings.
                         */
                        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                        boolean okas=isOnline();
                        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                            showWindowModal("Deseas activar el GPS para utilizar todas sus funciones?",
                                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                        else if(!isOnline()){
                            showWindowModal("No estas conectado a una red, por favor verifica.",null);
                            return;
                        }
                        else {
                            i = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(i);
                        }
                        break;

                    case 5:
                        finish();
                        break;

                    default:
                        showWindowModal("ERROR", null);
                        break;
                }
            }
        });
    }

    @Override
    public void showWindowModal(String msg, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if(intent!=null)
                    startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public boolean isOnline() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeConnection = connectivitymanager.getActiveNetworkInfo();
        boolean isOnline = (activeConnection != null) && activeConnection.isConnected();
        return isOnline;
    }

    @Override
    public void iniciarComponentes() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            IPreProcesarBO iPreProcesarBO = new PreProcesarBO(this);
            iPreProcesarBO.crearRutas(FileUtils.readFileAssets(this));
            iPreProcesarBO.generarIntersecciones();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }
}
