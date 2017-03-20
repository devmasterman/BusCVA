package net.rutas.morelos.app.bo;


import com.google.android.gms.maps.model.LatLng;

import net.rutas.morelos.app.exception.CoordenadasException;
import net.rutas.morelos.app.exception.RutasException;
import net.rutas.morelos.app.model.Ruta;

import java.util.HashMap;
import java.util.List;

/**
 * Created by eroman on 7/11/16.
 */

public interface IRutasBO {

    List<Ruta> obtenerRutasCercanas(LatLng posicion);

    HashMap<Integer, List<Ruta>> obtenerRastreoDeRutas(LatLng origen, LatLng destino);

    List<HashMap<Integer, List<Ruta>>> obtenerRastreoDeRutas(Ruta origen, Ruta destino);

    boolean exitseInterseccion(LatLng posicion, List<LatLng> puntos, int tolerancia);

    void generarIntersecciones() throws RutasException;

    List<Ruta> obtenerTodasLasRutas();

    List<ICoordenadasRutasBO> obtenerCoordenadasDeRutas() throws CoordenadasException;

    List<ICoordenadasRutasBO> convierteRutasACoordenadas(List<Ruta> rutas) throws CoordenadasException;
}
