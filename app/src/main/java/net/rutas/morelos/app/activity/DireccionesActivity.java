package net.rutas.morelos.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import net.rutas.morelos.app.R;
import net.rutas.morelos.app.bo.IPreProcesarBO;
import net.rutas.morelos.app.bo.IRutasBO;
import net.rutas.morelos.app.bo.impl.PreProcesarBO;
import net.rutas.morelos.app.bo.impl.RutasBO;
import net.rutas.morelos.app.dao.IRutasDAO;
import net.rutas.morelos.app.dao.impl.RutasDAO;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.utils.Constantes;
import net.rutas.morelos.app.utils.FileUtils;
import net.rutas.morelos.app.utils.IActivityMap;
import net.rutas.morelos.app.utils.IActivityUtil;
import net.rutas.morelos.app.utils.PropertyReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DireccionesActivity extends BaseDrawerActivity implements IActivityUtil, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, IActivityMap, AdapterView.OnItemSelectedListener,  com.google.android.gms.location.LocationListener, View.OnClickListener {

    private GoogleApiClient apiClient; //Servicio App Client
    private LocationRequest mLocationRequest; //Guarda la petición al servicio Location
    private double latitude, longitude; // Latitud y Logintud que actualemente tenemos.
    private Button btnBuscarDireccion; //Boton para buscar la dirección
    private Button btnBuscarRuta;
    private Button btnIntersectar;
    private ProgressDialog progressDialog; //Ventana de progreso, para el load Mask.
    private Properties properties; //Obtenemos una referencia a las propiedades
    private PropertyReader propertyReader; //Para leer l utilidad de los properties
    private IRutasBO iRutasBO;
    private Spinner origenSpinner;
    private Spinner destinoSpinner;

    private Ruta rutaOrigen=null; //Ruta Origen a buscar
    private Ruta rutaDestino=null;//Ruta destino a buscar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_direcciones, frameLayout);

        /**
         * Establecer Titulo
         */
        setTitle("Direcciones");
        //setContentView(R.layout.activity_direcciones);

        iRutasBO = new RutasBO();
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("propiedades.properties");

        createLocationRequest();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();

        btnBuscarDireccion = (Button) findViewById(R.id.idBuscarDireccion);
        btnBuscarRuta = (Button) findViewById(R.id.idBuscarRuta);
        btnIntersectar = (Button) findViewById(R.id.idBtnIntersect);
        btnBuscarDireccion.setOnClickListener(this);
        btnBuscarRuta.setOnClickListener(this);
        btnIntersectar.setOnClickListener(this);

        origenSpinner = (Spinner) findViewById(R.id.origen_spinner);
        destinoSpinner = (Spinner) findViewById(R.id.destino_spinner);

        //origenSpinner.setOnItemSelectedListener(this);
        //destinoSpinner.setOnItemSelectedListener(this);

        adapterSpinnerOrigen();
        adapterSpinnerDestino();


        if(rutaOrigen==null) {
            origenSpinner.setSelection(0);
            rutaOrigen=(Ruta)origenSpinner.getSelectedItem();
        }
        if(rutaDestino==null) {
            destinoSpinner.setSelection(0);
            rutaDestino=(Ruta)destinoSpinner.getSelectedItem();
        }
        iniciarComponentes();

        //origenSpinner.setOnItemSelectedListener(this);
        //destinoSpinner.setOnItemSelectedListener(this);
    }

    private void adapterSpinnerOrigen(){
        List<Ruta> listaRutasOrigen=iRutasBO.obtenerTodasLasRutas();
        ArrayAdapter<Ruta> adapter=new ArrayAdapter<Ruta>(this,
                android.R.layout.simple_spinner_item, listaRutasOrigen);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        origenSpinner.setAdapter(adapter);
        origenSpinner.setOnItemSelectedListener(this);
        adapter.notifyDataSetChanged();
    }


    private void adapterSpinnerDestino(){
        List<Ruta> listaRutasDestino=iRutasBO.obtenerTodasLasRutas();
        ArrayAdapter<Ruta> adapter=new ArrayAdapter<Ruta>(this,
                android.R.layout.simple_spinner_item, listaRutasDestino);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinoSpinner.setAdapter(adapter);
        destinoSpinner.setOnItemSelectedListener(this);
        adapter.notifyDataSetChanged();
    }

    /**
     * Método para proveer la localización por defecto.
     */
    @Override
    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constantes.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constantes.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Constantes.DISPLACEMENT);
    }

    /**
     * Actializa la posición
     *
     * @param location
     */
    @Override
    public void actualizarPosicion(Location location) {
        if (location != null) {
            System.out.print(String.valueOf(location.getLatitude()));
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        } else {
            Log.e(Constantes.LOGTAG, "Latitud y Longitud desconocidas");
        }
    }

    /**
     *
     */
    @Override
    public void validarPermisosGPS() {
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constantes.PETICION_PERMISO_LOCALIZACION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            if (lastLocation != null) {
                actualizarPosicion(lastLocation);
            }
        }
    }

    /**
     * Método para iniciar por primera vez los componentes de un Activity
     */
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

    /**
     * Método para mostrar un cuadro de diálogo y redirigir a otra Activity
     *
     * @param msg
     * @param i
     */
    @Override
    public void showWindowModal(String msg, Intent i) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        validarPermisosGPS();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services
        Log.e(Constantes.LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(Constantes.LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onLocationChanged(Location location) {
        actualizarPosicion(location);
    }

    @Override
    protected void onStart() {
        //Nos aseguramos de que se inicie el servicio
        super.onStart();
        if (apiClient != null) {
            apiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constantes.PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido para los mapas
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                actualizarPosicion(lastLocation);
                //dibujaMapas();

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                Log.e(Constantes.LOGTAG, "Permiso denegado");
            }
        }
    }

    private String buscarRutaCercana(){
        String result="La(s) Ruta(s) más cercana es : \n";
        List<Ruta> lstRutasCercanas=iRutasBO.obtenerRutasCercanas(new LatLng(latitude, longitude));
        if(lstRutasCercanas.isEmpty())
            return "ERROR_100";

        for(Ruta ruta: lstRutasCercanas)
            result+=ruta+"\n";
        /*ICoordenadasRutaBO arrayCoordenadasRutaBO[] = {new CoordenadasRutaBO("R02.json"), new CoordenadasRutaBO("R08.json"),
                new CoordenadasRutaBO("R15.json"), new CoordenadasRutaBO("R19.json"), new CoordenadasRutaBO("R17.json"),  new CoordenadasRutaBO("R13.json")};
        List<LatLng> lstLan=null;
        boolean rutaIntersectada=false;
        for (ICoordenadasRutaBO coordenadasRutaBO : arrayCoordenadasRutaBO) {
            lstLan = coordenadasRutaBO.obtenerListaDeCoordenadas();
            if(PolyUtil.isLocationOnPath(new LatLng(latitude, longitude), lstLan, true, 200)){
                //Encontramos la ruta más cercana
                rutaIntersectada=true;
                result+=coordenadasRutaBO.obtenerNombreRuta()+",";
            }
        }
        return result=rutaIntersectada?result:"ERROR_100";*/
        return  result;
    }

    private String rastreoDestino(){
        /*LatLng origen = new  LatLng(latitude, longitude);
        LatLng destino = new  LatLng(latitude, longitude);
        ICoordenadasRutaBO arrayCoordenadasRutaBO[] = {new CoordenadasRutaBO("R02.json"), new CoordenadasRutaBO("R08.json"),
                new CoordenadasRutaBO("R15.json"), new CoordenadasRutaBO("R19.json"), new CoordenadasRutaBO("R17.json")};
        List<LatLng> lstLan=null;
        for (ICoordenadasRutaBO coordenadasRutaBO : arrayCoordenadasRutaBO) {
            lstLan = coordenadasRutaBO.obtenerListaDeCoordenadas();
            if(PolyUtil.isLocationOnPath(destino, lstLan, true, 100)){
                //Encontramos la ruta más cercana
            }
        }*/

        return null;

    }
    private String encontrarDireccion()  {
        String result=null;
        return result;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        // detect the view that was "clicked"
        switch (view.getId()) {
            case R.id.idBuscarDireccion:
                new LoadingMask().execute(R.id.idBuscarDireccion);
                break;
            case R.id.idBuscarRuta:
                new LoadingMask().execute(R.id.idBuscarRuta);
                break;
            case R.id.idBtnIntersect:
                new LoadingMask().execute(R.id.idBtnIntersect);
                break;
        }

    }

    private String buscarIntersecciones(){
        IRutasDAO iRutasDAO = new RutasDAO();
        String result="El stack de las rutas es: \n";
        //Ruta rutaOrigen = iRutasDAO.buscarRuta("R17");
        //Ruta rutaDestino = iRutasDAO.buscarRuta("R17");
        //IOperacionesGrafo operacionesGrafo = new Grafo();
        //operacionesGrafo.inicializarVisitados();
        //origenSpinner.
        List<HashMap<Integer, List<Ruta>>> hMapRutas=iRutasBO.obtenerRastreoDeRutas(rutaOrigen, rutaDestino);

        if(hMapRutas.isEmpty())
            return "No fue posible obtener el rastreo de la ruta";

        for(HashMap<Integer, List<Ruta>> lstHm: hMapRutas){
            for(Map.Entry<Integer, List<Ruta>> entry : lstHm.entrySet()){
                result+="\nPosiblidad: "+entry.getKey()+"\n";
                for(Ruta ruta: entry.getValue()){
                    result+=ruta+"->";
                }
            }
        }



        return result;
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view==null){
            return;
        }
        switch (parent.getId()){
            case R.id.origen_spinner:
                rutaOrigen=  (Ruta)parent.getItemAtPosition(position);
                break;
            case R.id.destino_spinner:
                rutaDestino=(Ruta)parent.getItemAtPosition(position);
                break;
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Clase Privada, para cargar la mascará de loading.
     * Los tres paramétros del AsyncTask, son:
     * 1.- String es para el paramétro en el método doInBackground
     * 2.- Void es para el paramétro en el método onProgressUpdate
     * 3.- El último String es para el paramétro del método onPostExecute que es la respuesta.
     */
    private class LoadingMask extends AsyncTask<Integer, Void, String> {

        /**
         * Con este método ejecutamos la operación, con la cual se puede tardar mucho tiempo
         * La variable param se usa para distinguir el parmétro enviado desde los botones.
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(Integer... params) {
            String result=null;
            switch (params[0]){
                case R.id.idBuscarDireccion:
                    result = encontrarDireccion();
                    break;
                case R.id.idBuscarRuta:
                    result = buscarRutaCercana();
                    break;
                case R.id.idBtnIntersect:
                    result = buscarIntersecciones();
                    break;
            }
           return result;
        }

        /**
         * En este método agregamos la lógica depués de que se proceso una tarea pesada.
         * El paramétro es la respuesta del método anterior (doInBackground)
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            String msg=null;
            if(Constantes.ERROR_100.equals(result))
                msg=properties.getProperty("ruta.error.101");
            else if(Constantes.ERROR_101.equals(result))
                msg=properties.getProperty("ubicacion.error.100");
            else
                msg=result;
            showWindowModal(msg,null);
        }

        /**
         * Este método inicializa el progress Dialog, y es el primero en ejecutarse en el Task
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DireccionesActivity.this, "", "Buscando direcciones...");
        }

        /**
         * Este método es para llevar el control y la cuenta de lo que se está procesando
         * Por el momento no le damos ninguna implementación.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
