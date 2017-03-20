package net.rutas.morelos.app.bo.impl;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import net.rutas.morelos.app.bo.ICoordenadasRutasBO;
import net.rutas.morelos.app.bo.IOperacionesGrafo;
import net.rutas.morelos.app.bo.IRutasBO;
import net.rutas.morelos.app.bo.Transporte;
import net.rutas.morelos.app.dao.IRutasDAO;
import net.rutas.morelos.app.dao.impl.RutasDAO;
import net.rutas.morelos.app.exception.CoordenadasException;
import net.rutas.morelos.app.exception.RutasException;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.model.RutaInterseccion;
import net.rutas.morelos.app.utils.Constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eroman on 7/11/16.
 */

public class RutasBO extends Transporte implements IRutasBO {

   /* private List<ICoordenadasRutaBO> listaCoordenadasRutaBO;
    private List<ICoordenadasRutaBO> listaCoordenadasRutaCercanasBO;*/
    private IRutasDAO iRutasDAO;

    private static int totalRutas;

    //Nuevos
    private  Ruta ruta;

    public RutasBO() {
      //  listaCoordenadasRutaBO = new ArrayList<>();
        //listaCoordenadasRutaCercanasBO = new ArrayList<>();
        iRutasDAO=new RutasDAO();
        //agregaRutas();

    }
    public RutasBO(Ruta ruta) {
        this.ruta=ruta;
        iRutasDAO=new RutasDAO();

    }


    @Override
    public List<Ruta> obtenerRutasCercanas(LatLng posicion) {
        List<Ruta> rutasCercanas=new ArrayList<>();
        List<ICoordenadasRutasBO> lstICoordenadasRutasBO = obtenerCoordenadasDeRutas();
        for(ICoordenadasRutasBO coordenadasRutasBO : lstICoordenadasRutasBO)
            if (exitseInterseccion(posicion, coordenadasRutasBO.obtenerListaDeCoordenadas(), Constantes.TOLERANCIA_RUTAS_CERCANAS))
                rutasCercanas.add(coordenadasRutasBO.getRuta());

        return rutasCercanas;
    }

    @Override
    public HashMap<Integer, List<Ruta>> obtenerRastreoDeRutas(LatLng origen, LatLng destino) {
        HashMap<Integer, List<Ruta>> stackRutas = new HashMap<>();
        List<Ruta> listaRutasStack = null;
        List<RutaInterseccion> lstRutasIntersecciones=null;
        List<RutaInterseccion> lstRutasInterseccionesAll=null;
        totalRutas = obtenerTodasLasRutas().size();

        //1.- Buscamos las rutas más cercanas, Verificamos si el destino se encuentra entre las rutas cercanas, de ser asi,
        // damos por hecho que encontramos el destino.
        List<ICoordenadasRutasBO> lstCoordenadasRutasBO = convierteRutasACoordenadas(obtenerRutasCercanas(origen));

        for(int i=0; i<lstCoordenadasRutasBO.size(); i++){
            //totalRutas--;
            if(exitseInterseccion(destino, lstCoordenadasRutasBO.get(i).obtenerListaDeCoordenadas(),
                    Constantes.TOLERANCIA_RUTAS_INTERSECTADAS)){
                listaRutasStack=new ArrayList<>();
                listaRutasStack.add(lstCoordenadasRutasBO.get(i).getRuta());
                stackRutas.put(i, listaRutasStack);

            }
        }

        if(!stackRutas.isEmpty())
            return stackRutas;


        lstRutasIntersecciones=iRutasDAO.buscarIntereseccionesPorRuta(iRutasDAO.buscarRuta("R13"));
        lstRutasInterseccionesAll=iRutasDAO.obtenerTodasLasIntersecciones();

        //Recorremos las rutas cercanas
        for (ICoordenadasRutasBO coordenadasRutasBO : lstCoordenadasRutasBO){
            //Obtenemos sus intersecciones
            lstRutasIntersecciones=iRutasDAO.buscarIntereseccionesPorRuta(iRutasDAO.
                    buscarRuta(coordenadasRutasBO.getRuta().getNombre()));

            //Buscamos si en las intersecciones se encuntrea el destino
            for(RutaInterseccion rutaInterseccion: lstRutasIntersecciones){

            }

        }




        //2.- Separamos las rutas cercanas de las retantes
        /*if(!lstRutasCercanas.isEmpty())
            for(String rutaCercana: lstRutasCercanas){
                for(int i=0; i<listaCoordenadasRutaBO.size();i++){
                    if(rutaCercana.equals(listaCoordenadasRutaBO.get(i).obtenerNombreRuta())){
                        listaCoordenadasRutaCercanasBO.add(listaCoordenadasRutaBO.get(i));
                        listaCoordenadasRutaBO.remove(i);
                    }
                }
            }

        //3.- Verificamos si el destino se encuentra entre las rutas cercanas, de ser asi,
        // damos por hecho que encontramos el destino.
        int key=0;
        for(ICoordenadasRutaBO rutaCrecanaBO: listaCoordenadasRutaCercanasBO){
            listaRutasStack=new ArrayList<>();
            if(exitseInterseccion(destino, rutaCrecanaBO.obtenerListaDeCoordenadas())){
                listaRutasStack.add(rutaCrecanaBO.obtenerNombreRuta());
                stackRutas.put(++key, listaRutasStack);
            }
        }
        if(!stackRutas.isEmpty())
            return stackRutas;

        //4.- Buscamos en las interesecciones entre las cercanas y las restantes
        // Podemos usar Sugar para almacenar la info

*/


        return stackRutas;
    }

    @Override
    public List<HashMap<Integer, List<Ruta>>> obtenerRastreoDeRutas(Ruta origen, Ruta destino) {
        IOperacionesGrafo iOperacionesGrafo = new Grafo();
        List<HashMap<Integer, List<Ruta>>> lstHmRastreo = new ArrayList<>();
        HashMap<Integer, List<Ruta>> hashMapRutaIgual = new HashMap<>();
        List<Ruta> rutaIgual = new ArrayList<>();
        if(origen.toString().equals(destino.toString())) {
            rutaIgual.add(destino);
            hashMapRutaIgual.put(1, rutaIgual);
            lstHmRastreo.add(hashMapRutaIgual);
            return lstHmRastreo;
        }else{
            iOperacionesGrafo.inicializarVisitados();
            return iOperacionesGrafo.busquedaAncha(origen, destino);
        }
    }

    @Override
    public boolean exitseInterseccion(LatLng posicion, List<LatLng> puntos, int tolerancia) {
        return PolyUtil.isLocationOnPath(posicion, puntos, true, tolerancia);
    }

    @Override
    public void generarIntersecciones() throws RutasException {
        List<ICoordenadasRutasBO> lstCoordenadasRutasBO =obtenerCoordenadasDeRutas();
        for(ICoordenadasRutasBO ruta : lstCoordenadasRutasBO){
            List<LatLng> posiciones= ruta.obtenerListaDeCoordenadas();
            siguienteSubRuta:
            for(ICoordenadasRutasBO subRuta : lstCoordenadasRutasBO){
                if(ruta.getRuta().getNombre().equals(subRuta.getRuta().getNombre()))
                    continue;
                for(LatLng pos: posiciones){
                    if(exitseInterseccion(pos,subRuta.obtenerListaDeCoordenadas(), Constantes.TOLERANCIA_RUTAS_INTERSECTADAS)){
                        RutaInterseccion rutaInterseccion = new RutaInterseccion(subRuta.getRuta().getNombre(), ruta.getRuta());
                        rutaInterseccion.save();
                        continue siguienteSubRuta;
                    }
                }
            }
        }
    }

    @Override
    public List<Ruta> obtenerTodasLasRutas() {
        return iRutasDAO.obtenerTodasLasRutas();
    }

    @Override
    public List<ICoordenadasRutasBO> obtenerCoordenadasDeRutas() throws CoordenadasException {
        List<Ruta> lstRutas =  obtenerTodasLasRutas();
        return convierteRutasACoordenadas(lstRutas);
    }

    @Override
    public List<ICoordenadasRutasBO> convierteRutasACoordenadas(List<Ruta> rutas) throws CoordenadasException {
        List<ICoordenadasRutasBO> lstCoordenadasRutasBO =  new ArrayList<>();
        List<Ruta> lstRutas = rutas;
        for(Ruta ruta : lstRutas)
            lstCoordenadasRutasBO.add(new CoordenadasRutasBO(ruta));
        return lstCoordenadasRutasBO;
    }

}
