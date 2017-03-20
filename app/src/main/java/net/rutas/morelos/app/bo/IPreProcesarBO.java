package net.rutas.morelos.app.bo;

import net.rutas.morelos.app.exception.RutasException;

import java.util.HashMap;

/**
 * Created by eroman on 15/11/16.
 */

public interface IPreProcesarBO {
    void crearRutas(HashMap<String, String> hmRutasCoordenas)throws RutasException;
    void generarIntersecciones() throws RutasException;
}
