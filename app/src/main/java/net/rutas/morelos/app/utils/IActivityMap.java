package net.rutas.morelos.app.utils;

import android.location.Location;

/**
 * Created by eroman on 4/11/16.
 */

public interface IActivityMap {

    /**
     * Método para proveer la localización por defecto.
     */
    void createLocationRequest();

    /**
     *Actializa la posición
     * @param location
     */
    void actualizarPosicion(Location location);

    /**
     *
     */
    void validarPermisosGPS();
}
