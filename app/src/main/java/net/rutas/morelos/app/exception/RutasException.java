package net.rutas.morelos.app.exception;

/**
 * Created by eroman on 13/11/16.
 */

public class RutasException extends RuntimeException {
    // constructor sin argumentos
    public RutasException() {
        this("Error al implementar las Rutas.");
    } // fin del constructor de ExcepcionPilaVacia sin argumentos
    // constructor con un argumento

    public RutasException(String excepcion) {
        super(excepcion);
    } // fin del constructor de ExcepcionPilaVacia con un argumento
}
