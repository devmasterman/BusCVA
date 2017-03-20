package net.rutas.morelos.app.bo.impl;

import android.content.Context;
import android.graphics.Color;

import net.rutas.morelos.app.bo.IPreProcesarBO;
import net.rutas.morelos.app.bo.IRutasBO;
import net.rutas.morelos.app.exception.RutasException;
import net.rutas.morelos.app.model.Ruta;
import net.rutas.morelos.app.model.RutasCoordenadas;
import net.rutas.morelos.app.utils.PropertyReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by eroman on 15/11/16.
 */

public class PreProcesarBO implements IPreProcesarBO {

    private Properties properties;
    private PropertyReader propertyReader;
    private Context ctx;
    private IRutasBO iRutasBO;

    public PreProcesarBO(Context ctx) {
        this.ctx=ctx;
        propertyReader = new PropertyReader(ctx);
        properties = propertyReader.getMyProperties("propiedades.properties");
        iRutasBO=new RutasBO();
    }

    @Override
    public void crearRutas(HashMap<String, String> hmRutasCoordenas) throws RutasException {
        Ruta ruta=null;
        RutasCoordenadas rutasCoordenadas=null;
        for (Map.Entry<String, String> entry : hmRutasCoordenas.entrySet()){
            ruta = new Ruta(entry.getKey(), Color.parseColor(properties.getProperty(entry.getKey()+".color").toString()));
            ruta.save();
            rutasCoordenadas  = new RutasCoordenadas(entry.getValue(), ruta);
            rutasCoordenadas.save();
        }
    }

    @Override
    public void generarIntersecciones() throws RutasException {
        iRutasBO.generarIntersecciones();
    }
}
