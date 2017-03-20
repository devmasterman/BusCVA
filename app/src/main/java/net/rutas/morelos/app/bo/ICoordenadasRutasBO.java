package net.rutas.morelos.app.bo;

import com.google.android.gms.maps.model.LatLng;

import net.rutas.morelos.app.model.Ruta;

import java.util.List;

/**
 * Created by eroman on 16/11/16.
 */

public interface ICoordenadasRutasBO {
    List<LatLng> obtenerListaDeCoordenadas();
    Ruta getRuta();
}
