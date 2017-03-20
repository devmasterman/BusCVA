package net.rutas.morelos.app.exception;

/**
 * Created by eroman on 20/10/16.
 */

public class CoordenadasException extends RuntimeException {
    // constructor sin argumentos
    public CoordenadasException() {
        this("No se puede trazar el mapa");
    } // fin del constructor de ExcepcionPilaVacia sin argumentos
    // constructor con un argumento

    public CoordenadasException(String excepcion) {
        super(excepcion);
    } // fin del constructor de ExcepcionPilaVacia con un argumento

}
