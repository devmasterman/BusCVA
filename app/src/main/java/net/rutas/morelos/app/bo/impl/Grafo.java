package net.rutas.morelos.app.bo.impl;

import android.util.Log;

import net.rutas.morelos.app.bo.IOperacionesGrafo;
import net.rutas.morelos.app.dao.IRutasDAO;
import net.rutas.morelos.app.dao.impl.RutasDAO;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.model.RutaInterseccion;
import net.rutas.morelos.app.utils.Constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by eroman on 18/11/16.
 * Clase para encontrar las rutas más cercanas
 * Utilizando la teoría de grafos.
 */

public class Grafo implements IOperacionesGrafo {

    private static boolean[] visitados; //Verifica los vértices visitados en cada arista.
    private static int totalPosibilidades; //Almacena todos los caminos posibles para una ruta destino
    private List<Ruta>[] grafo; //Representa la entidad Grafo
    private IRutasDAO iRutasDAO; //Referencia al acceso a datos para las rutas

    public Grafo() {
        iRutasDAO = new RutasDAO();
        inicializarGrafo();
    }

    /**
     * Método privado que transforma las rutas en una representación de Grafo.
     */
    private void inicializarGrafo(){
        List<RutaInterseccion> lstIntersecciones=null;
        List<Ruta> rutas = iRutasDAO.obtenerTodasLasRutas();
        lstIntersecciones= iRutasDAO.obtenerTodasLasIntersecciones();
        grafo=new List[rutas.size()];
        List<Ruta> vertice = null; //Representa un vértice
        for(int i=0; i<rutas.size();i++){
            vertice=new LinkedList<>();
            lstIntersecciones=iRutasDAO.buscarIntereseccionesPorRuta(rutas.get(i));
            for(RutaInterseccion rush: lstIntersecciones) {
                vertice.add(iRutasDAO.buscarRuta(rush.getSubRuta()));//Se crean las aristas para el grafo
            }
            //Agregar al grafo
            grafo[i]=vertice;
        }
    }

    /**
     * Metodo que inicializa visitados
     */
    @Override
    public void inicializarVisitados() {
        Grafo.visitados = new boolean[grafo.length];
        for ( int i = 0; i < grafo.length; i = i + 1 )
        {  Grafo.visitados[i] = false;
        }
    }

    /**
     * Lista todas las aristas del grafo apuntando a sus vértices adiacentes
     */
    @Override
    public void enumerarArcos() {
        List visitado = null;
        ListIterator iter = null;
        for ( int i = 0; i < grafo.length; i = i + 1 )
        {   visitado = grafo[i];
            iter = visitado.listIterator(0);
            while ( iter.hasNext() )
            {
                System.out.println("Arco del Vertice "+ (i+1)+" al Vertice "+(String)iter.next());
            }
        }
    }

    /**
     *  Médodo para calcular los posibles caminos de una ruta origen
     *  Utilizando el algoritmo de busqueda ancha para un grafo.
     * @param ruta
     */
    @Override
    public void busquedaAncha(Ruta ruta) {
        LinkedList avisitar = new LinkedList();
        List visitado = null;
        ListIterator iter = null;
        Ruta rotulo = null;
        Integer entero = null;
        int vertice = 0;
        Grafo.visitados[ruta.getId().intValue()-1] = true;
        System.out.println("Busqueda Ancha - Vertice "+ruta.getId());
        Log.i(Constantes.LOGTAG,"Busqueda Ancha - Vertice "+ruta.getId());
        visitado = grafo[ruta.getId().intValue()-1];
        iter = visitado.listIterator();
        while ( iter.hasNext() )
        {  rotulo = (Ruta)iter.next();
            entero = new Integer(rotulo.getId().intValue());
            vertice = entero.intValue();
            avisitar.add(entero);
        }

        while ( !avisitar.isEmpty() )
        {  entero = (Integer)avisitar.removeFirst();
            vertice = entero.intValue();
            if ( visitados[vertice-1] == false )
            {  Grafo.visitados[vertice-1] = true;
                System.out.println("Busqueda Ancha - Vertice "+vertice);
                Log.i(Constantes.LOGTAG,"Busqueda Ancha - Vertice "+vertice);
                visitado = grafo[vertice-1];
                iter = visitado.listIterator();
                while ( iter.hasNext() )
                {  rotulo = (Ruta)iter.next();
                    entero = new Integer(rotulo.getId().intValue());
                    vertice = entero.intValue();
                    avisitar.add(entero);
                }
            }
        }
    }

    /**
     * Método para calcular los posibles caminos de una ruta origen
     * Utilizando el algoritmo de búsqueda ancha (recorre el árbol completamente)
     * @param ruta
     */
    @Override
    public void busquedaProfunda(Ruta ruta) {
        List visitado = null;
        ListIterator iter = null;
        String rotulo = null;
        Integer nodo = null;
        int indice = 0;

        if ( Grafo.visitados[ruta.getId().intValue()-1] == false )
        {
            System.out.println("Busqueda Profunda - Vertice "+ruta.getId());
        }
        Grafo.visitados[ruta.getId().intValue()-1] = true;
        visitado = grafo[ruta.getId().intValue()-1];
        iter = visitado.listIterator(0);

        while ( iter.hasNext() )
        {  rotulo = (String)iter.next();
            nodo = new Integer(rotulo);
            indice = nodo.intValue();
            if ( Grafo.visitados[indice-1] == false )
            {  System.out.println("Depth First Search - Vertice "+rotulo);
                Grafo.visitados[indice-1] = true;
                busquedaProfunda(ruta); // Recursividad del metodo, verificamos esta parte despues
            }
        }
    }

    /**
     * Lista todos los elementos en el grafo
     */
    @Override
    public void enumerarComponentes() {
        int componente = 0;
        for ( int i = 0; i < grafo.length; i = i + 1 )
        {  componente = componente + 1;
            if ( Grafo.visitados[i] == false )
            {
                busquedaProfunda(grafo[i+1].get(i)); //Igual verificamos está parte
                System.out.println("Enumeracion de Componentes - Componente : "+componente);
            }
        }
    }

    /**
     * Médodo para calcular los posibles caminos de una ruta origen hacia una destino
     * Utilizando el algoritmo de busqueda ancha para un grafo.
     * @param origen
     * @param destino
     * @return
     */
    @Override
    public List<HashMap<Integer, List<Ruta>>> busquedaAncha(Ruta origen, Ruta destino) {
        totalPosibilidades=0;
        List<HashMap<Integer, List<Ruta>>> listaMapaRutas=new ArrayList<>();
        LinkedList avisitar = new LinkedList();
        List visitado = null;
        ListIterator iter = null;
        Ruta rotulo = null;
        Integer entero = null;
        int vertice = 0;
        List<Ruta> stackTraceRuta = new ArrayList<>();

        HashMap<Integer, List<Ruta>> hashRutas = new HashMap<>();
        Grafo.visitados[origen.getId().intValue()-1] = true;
        Log.i(Constantes.LOGTAG,"Busqueda Ancha - Vertice "+origen.getId().intValue());
        visitado = grafo[origen.getId().intValue()-1];
        iter = visitado.listIterator();
        stackTraceRuta.add(origen);

        while ( iter.hasNext() )
        {
            rotulo = (Ruta)iter.next();
            entero = new Integer(rotulo.getId().intValue());
            vertice = entero.intValue();
            avisitar.add(entero);
            if(entero==destino.getId().intValue()){
                stackTraceRuta.add(rotulo);
                totalPosibilidades++;
                hashRutas.put(totalPosibilidades, stackTraceRuta);
            }
        }
        if(!hashRutas.isEmpty())
            listaMapaRutas.add(hashRutas);
        while ( !avisitar.isEmpty() )
        {
            entero = (Integer)avisitar.removeFirst();
            vertice = entero.intValue();
            if ( visitados[vertice-1] == false )
            {
                hashRutas = new HashMap<>();
                stackTraceRuta = new ArrayList<>();
                stackTraceRuta.add(origen);
                Grafo.visitados[vertice-1] = true;
                Log.i(Constantes.LOGTAG,"Busqueda Ancha - Vertice "+vertice);
                Integer verticeAux=vertice;
                visitado = grafo[vertice-1];
                iter = visitado.listIterator();
                while ( iter.hasNext() )
                {
                    rotulo = (Ruta) iter.next();
                    entero = new Integer(rotulo.getId().intValue());
                    vertice = entero.intValue();
                    avisitar.add(entero);
                    if(entero==destino.getId().intValue()){
                        stackTraceRuta.add(iRutasDAO.buscarRuta(verticeAux.longValue()));
                        stackTraceRuta.add(rotulo);
                        totalPosibilidades++;
                        hashRutas.put(totalPosibilidades, stackTraceRuta);
                    }
                }
                if(!hashRutas.isEmpty())
                    listaMapaRutas.add(hashRutas);
            }
        }
        return listaMapaRutas;
    }
}
