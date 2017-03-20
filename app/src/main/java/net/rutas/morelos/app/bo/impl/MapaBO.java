package net.rutas.morelos.app.bo.impl;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import net.rutas.morelos.app.bo.ICoordenadasRutasBO;
import net.rutas.morelos.app.bo.IMapaBO;
import net.rutas.morelos.app.model.Ruta;

import java.util.List;

/**
 * Created by eroman on 15/11/16.
 */

public class MapaBO implements IMapaBO {
    private Ruta ruta; //La ruta a procesar
    private ICoordenadasRutasBO iCoordenadasRutasBO;

    public MapaBO() {
    }

    public MapaBO(Ruta ruta) {
        this.ruta = ruta;
        iCoordenadasRutasBO=new CoordenadasRutasBO(ruta);
    }

    @Override
    public void dibujarMapa(GoogleMap mMap) {
        mMap.addPolyline(obtenerPuntosRuta());
    }

    private PolylineOptions obtenerPuntosRuta(){
        PolylineOptions poliLineas = new PolylineOptions().width(5).color(ruta.getColor()).geodesic(true);
        List<LatLng> lstCoordenadas = iCoordenadasRutasBO.obtenerListaDeCoordenadas();
        for(LatLng punto: lstCoordenadas){
            poliLineas.add(punto);
        }
        return poliLineas;
    }
}
