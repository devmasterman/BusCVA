package net.rutas.morelos.app.model;

import com.orm.SugarRecord;

/**
 * Created by eroman on 15/11/16.
 */

public class RutasCoordenadas extends SugarRecord {

    private String json;
    private Ruta ruta;

    public RutasCoordenadas() {
    }

    public RutasCoordenadas(String json, Ruta ruta) {
        this.json = json;
        this.ruta = ruta;
    }

    public String getJson() {
        return json;
    }

    public Ruta getRuta() {
        return ruta;
    }
}
