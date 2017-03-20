package net.rutas.morelos.app.dao;

import net.rutas.morelos.app.model.Mapa;

import java.util.HashMap;

/**
 * Created by eroman on 20/10/16.
 */

public interface IMapaDAO {

    Mapa buscarMapa(String idRuta);
    Mapa buscarMapa(int idRuta);
    void insertaMapas(HashMap<String, String> mMapas);
}
