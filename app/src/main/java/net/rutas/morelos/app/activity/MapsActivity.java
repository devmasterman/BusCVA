package net.rutas.morelos.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.rutas.morelos.app.R;
import net.rutas.morelos.app.bo.IMapaBO;
import net.rutas.morelos.app.bo.IRutasBO;
import net.rutas.morelos.app.bo.impl.MapaBO;
import net.rutas.morelos.app.bo.impl.RutasBO;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.utils.Constantes;
import net.rutas.morelos.app.utils.IActivityMap;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends BaseDrawerActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, IActivityMap, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap; //Servicio del mapa
    private GoogleApiClient apiClient; //Servicio App Client
    private double latitude, longitude; // Latitud y Logintud que actualemente tenemos.
    private Marker markerName; //Varible de instancia para crear un Marker Place
    private LocationRequest mLocationRequest; //Guarda la petición al servicio Location
    private CameraPosition cameraPosition; //Guardamos la poscicón de la camara
    private IRutasBO iRutasBO; //para obtener todas las rutas disponibles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_maps, frameLayout);

        /**
         * Establecer Titulo
         */
        setTitle("Mapas");
        //setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        iRutasBO = new RutasBO();
        createLocationRequest();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();

    }

    /**
     * Dibujamos todos los mapas de forma polimórfica.
     */
    private void dibujaMapas() {
        List<Ruta> lstRutas = iRutasBO.obtenerTodasLasRutas();
        System.out.print("QUE?");
        List<IMapaBO> lstMapasBO = new ArrayList<>();
        //Obtenemos todas las rutas a dibujar de manera dinámica
        for (Ruta ruta : lstRutas)
            lstMapasBO.add(new MapaBO(ruta));

        //Dibujamos todas las rutas
        for (IMapaBO iMapaBO : lstMapasBO)
            iMapaBO.dibujarMapa(mMap);

        //Configutamos las propiedades del mapa
        configuraMapaUI();

        cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(Constantes.CAMERA_ZOOM).build();
        /*markerName = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude)));*/
        //Enfocamos la camara para dar efecto de zoom en el mapa
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
    }

    private void configuraMapaUI(){
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

        /**
         * Propiedades Utiles para futuros cambios
         */
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setCompassEnabled(true);
        //mMap.getUiSettings().setRotateGesturesEnabled(true);
        //mMap.getUiSettings().setScrollGesturesEnabled(true);
        //mMap.getUiSettings().setTiltGesturesEnabled(true);
        //mMap.getUiSettings().setZoomGesturesEnabled(true);
        //or myMap.getUiSettings().setAllGesturesEnabled(true);

        //mMap.setTrafficEnabled(true);
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        /**
         * Sobreescribimos la posición del boton de location, ya que por el Drawer Menu no se alcanza a ver.
         */
        View locationButton = ((View) this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                locationButton.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 30);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Si la aplicación cliente no está lista, lo forzamos.
        if (apiClient != null) {
            apiClient.connect();
        }
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
                dibujaMapas();

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                Log.e(Constantes.LOGTAG, "Permiso denegado");
            }
        }
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
    public void onLocationChanged(Location location) {
        //Dibujamos un nuevo Marker Place cada que nos cambiemos de posición.
        if(markerName!=null){
            markerName.remove();
        }
        actualizarPosicion(location);
        dibujaMapas();
    }

    @Override
    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constantes.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constantes.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Constantes.DISPLACEMENT);
    }

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
}
