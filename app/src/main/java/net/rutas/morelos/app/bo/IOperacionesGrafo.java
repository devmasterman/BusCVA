package net.rutas.morelos.app.bo;

import net.rutas.morelos.app.model.Ruta;

import java.util.HashMap;
import java.util.List;

/**
 * Created by eroman on 18/11/16.
 * Ref: http://www.fceia.unr.edu.ar/estruc/2005/graffund.htm
 */

public interface IOperacionesGrafo {

    /**
     * Metodo que inicializa visitados
     */
    void inicializarVisitados();

    /**
     * Lista todas las aristas del grafo apuntando a sus vértices adiacentes
     */
    void enumerarArcos();

    /**
     *  Médodo para calcular los posibles caminos de una ruta origen
     *  Utilizando el algoritmo de busqueda ancha para un grafo.
     * @param ruta
     */
    void busquedaAncha(Ruta ruta);

    /**
     * Método para calcular los posibles caminos de una ruta origen
     * Utilizando el algoritmo de búsqueda ancha (recorre el árbol completamente)
     * @param ruta
     */
    void busquedaProfunda(Ruta ruta);

    /**
     * Lista todos los elementos en el grafo
     */
    void enumerarComponentes();


    /**
     * Médodo para calcular los posibles caminos de una ruta origen hacia una destino
     * Utilizando el algoritmo de busqueda ancha para un grafo.
     * @param origen
     * @param destino
     * @return
     */
    List<HashMap<Integer, List<Ruta>>> busquedaAncha(Ruta origen, Ruta destino);

}
