package net.rutas.morelos.app.dao.impl;

import com.orm.query.Condition;
import com.orm.query.Select;

import net.rutas.morelos.app.dao.IMapaDAO;
import net.rutas.morelos.app.model.Mapa;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eroman on 20/10/16.
 */

public class MapaDAO implements IMapaDAO {

    public MapaDAO() {
    }

    @Override
    public Mapa buscarMapa(String idRuta) {
        return Select.from(Mapa.class).where(Condition.prop("route").eq(idRuta)).first();

    }

    @Override
    public Mapa buscarMapa(int idRuta) {
        return Select.from(Mapa.class).where(Condition.prop("id").eq(idRuta)).first();
    }

    @Override
    public void insertaMapas(HashMap<String, String> mMapas) {

        for (Map.Entry<String, String> entry : mMapas.entrySet()){
            Mapa mapa = new Mapa(entry.getKey(), entry.getValue());
            mapa.save();
        }
    }
}
