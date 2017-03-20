package net.rutas.morelos.app.dao;

import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.model.RutaInterseccion;
import net.rutas.morelos.app.model.RutasCoordenadas;

import java.util.List;

/**
 * Created by eroman on 13/11/16.
 */

public interface IRutasDAO {
    RutaInterseccion buscarInterseccion(String subRuta);
    List<RutaInterseccion> buscarIntereseccionesPorRuta(Ruta ruta);
    Ruta buscarRuta(String ruta);
    Ruta buscarRuta(long id);
    List<Ruta> obtenerTodasLasRutas();
    RutasCoordenadas buscarRutasCoordenadas(Ruta ruta);
    List<RutaInterseccion> obtenerTodasLasIntersecciones();
}
