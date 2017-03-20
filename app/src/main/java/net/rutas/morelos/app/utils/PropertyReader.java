package net.rutas.morelos.app.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

/**
 * Created by eroman on 7/11/16.
 */

public class PropertyReader {
    private Context context;
    private Properties properties;

    public PropertyReader(Context context){
        this.context=context;
        properties = new Properties();
    }

    public Properties getMyProperties(String file){
        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(file);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(reader);

        }catch (Exception e){
            System.out.print(e.getMessage());
        }

        return properties;
    }
}
