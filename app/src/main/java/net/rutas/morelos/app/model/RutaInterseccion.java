package net.rutas.morelos.app.model;

import com.orm.SugarRecord;

/**
 * Created by eroman on 13/11/16.
 */

public class RutaInterseccion extends SugarRecord {

    private String subRuta;
    private Ruta ruta;

    public RutaInterseccion(){

    }

    public RutaInterseccion(String subRuta, Ruta ruta) {
        this.subRuta = subRuta;
        this.ruta = ruta;
    }

    public String getSubRuta() {
        return subRuta;
    }

    public Ruta getRuta() {
        return ruta;
    }
}
