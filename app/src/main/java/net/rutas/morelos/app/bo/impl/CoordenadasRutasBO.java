package net.rutas.morelos.app.bo.impl;

import com.google.android.gms.maps.model.LatLng;

import net.rutas.morelos.app.bo.ICoordenadasRutasBO;
import net.rutas.morelos.app.dao.IRutasDAO;
import net.rutas.morelos.app.dao.impl.RutasDAO;
import net.rutas.morelos.app.dto.CoordenadasDTO;
import net.rutas.morelos.app.dto.CoordenadasRutaJsonDTO;
import net.rutas.morelos.app.exception.CoordenadasException;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.model.RutasCoordenadas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eroman on 16/11/16.
 */

public class CoordenadasRutasBO implements ICoordenadasRutasBO {

    private Ruta ruta;
    private List<LatLng> listaLatLong;

    public CoordenadasRutasBO(Ruta ruta) {
        this.ruta = ruta;
        listaLatLong = obtenerCoordenadas();
    }

    @Override
    public List<LatLng> obtenerListaDeCoordenadas() {
        return listaLatLong;
    }

    @Override
    public Ruta getRuta() {
        return ruta;
    }

    public List<LatLng> obtenerCoordenadas() throws CoordenadasException {
        List<LatLng> lstCoordenadas = new ArrayList<>();
        IRutasDAO iRutasDAO = new RutasDAO();
        RutasCoordenadas rutasCoordenadas = iRutasDAO.buscarRutasCoordenadas(ruta);
        CoordenadasRutaJsonDTO coordenadasRutaJsonDTO=obtenerParserJSON(rutasCoordenadas);
        for(CoordenadasDTO coordenadasDTO: coordenadasRutaJsonDTO.getCoordinates())
            lstCoordenadas.add(new LatLng(coordenadasDTO.getLat(), coordenadasDTO.getLon()));
        return  lstCoordenadas;
    }

    private  CoordenadasRutaJsonDTO obtenerParserJSON(RutasCoordenadas ruta){
        CoordenadasRutaJsonDTO coordenadasRutaJsonDTO = new CoordenadasRutaJsonDTO();
        try{
            JSONObject jsonRootObject = new JSONObject(ruta.getJson());
            coordenadasRutaJsonDTO.setTransport((jsonRootObject.optString("Transport").toString()));
            //Obtenemos el arreglo de coordenadas
            JSONArray jsonArray = jsonRootObject.optJSONArray("Coordinates");

            for(int i=0; i < jsonArray.length(); i++){
                CoordenadasDTO coordenadasDTO = new CoordenadasDTO();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                coordenadasDTO.setLat(Double.parseDouble(jsonObject.optString("lat").toString()));
                coordenadasDTO.setLon(Double.parseDouble(jsonObject.optString("lon").toString()));
                coordenadasRutaJsonDTO.addCoordinates(coordenadasDTO);
            }

        }catch (JSONException e) {e.printStackTrace();}
        return coordenadasRutaJsonDTO;
    }
}
