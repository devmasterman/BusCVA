package net.rutas.morelos.app.utils;

import android.content.Intent;

/**
 * Created by eroman on 28/10/16.
 */

public interface IActivityUtil {
    /**
     * Método para iniciar por primera vez los componentes de un Activity
     */
    void iniciarComponentes();

    /**
     * Método para mostrar un cuadro de diálogo y redirigir a otra Activity
     * @param msg
     * @param i
     */
    void showWindowModal(String msg, Intent i);

    boolean isOnline();
}
