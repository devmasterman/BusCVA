package net.rutas.morelos.app.dao.impl;

import com.orm.query.Condition;
import com.orm.query.Select;

import net.rutas.morelos.app.dao.IRutasDAO;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.model.RutaInterseccion;
import net.rutas.morelos.app.model.RutasCoordenadas;

import java.util.List;

/**
 * Created by eroman on 13/11/16.
 */

public class RutasDAO implements IRutasDAO {
    @Override
    public RutaInterseccion buscarInterseccion(String subRuta) {
        return Select.from(RutaInterseccion.class).where(Condition.prop("sub_Ruta").eq(subRuta)).first();
    }

    @Override
    public List<RutaInterseccion> buscarIntereseccionesPorRuta(Ruta ruta) {
        return RutaInterseccion.find(RutaInterseccion.class, "ruta = ?", new String[]{ruta.getId().toString()});
    }

    @Override
    public Ruta buscarRuta(String ruta) {
        return Select.from(Ruta.class).where(Condition.prop("nombre").eq(ruta)).first();
    }

    @Override
    public Ruta buscarRuta(long id) {
        return Select.from(Ruta.class).where(Condition.prop("id").eq(id)).first();
    }

    @Override
    public List<Ruta> obtenerTodasLasRutas() {
        List<Ruta> lst = null;
        try {
            lst = Select.from(Ruta.class).list();
        }catch (Exception e){
            e.printStackTrace();
        }
        return   lst;
    }

    @Override
    public RutasCoordenadas buscarRutasCoordenadas(Ruta ruta) {
        return Select.from(RutasCoordenadas.class).where(Condition.prop("ruta").eq(ruta.getId().toString())).first();
    }

    @Override
    public List<RutaInterseccion> obtenerTodasLasIntersecciones() {
        return Select.from(RutaInterseccion.class).list();
    }
}
