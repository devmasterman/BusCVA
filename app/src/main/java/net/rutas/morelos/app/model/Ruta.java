package net.rutas.morelos.app.model;

import com.orm.SugarRecord;


/**
 * Created by eroman on 13/11/16.
 */

public class Ruta extends SugarRecord {

    private String nombre;
    private int color;

    public Ruta(){

    }

    public Ruta(String ruta) {
        this.nombre = ruta;
    }

    public Ruta(String ruta, int color) {
        this.nombre = ruta;
        this.color=color;
    }

    public String getNombre() {
        return nombre;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
