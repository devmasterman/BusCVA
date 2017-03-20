package net.rutas.morelos.app.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by eroman on 27/10/16.
 */

public class FileUtils  {

    /**
     * Leemos un archivo json con las rutas a dibujar
     * Se cargarán por primera vez a la BD.
     * Verificar después si es necesario el guardarlos en BD.
     * @param ctx
     * @return
     */
    public static HashMap<String, String> readFileAssets(Context ctx){
        HashMap<String, String> filesMap = new HashMap<>();
        AssetManager assetManager = ctx.getAssets();
        String[] files=null;
        String json;
        try {
            files = assetManager.list("Files");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // To load text file
        InputStream input=null;
        try {
            for(String fileName: files) {
                input = assetManager.open("Files"+ File.separator+fileName);

                int size = input.available();
                byte[] buffer = new byte[size];
                input.read(buffer);


                // byte buffer into a string
                json = new String(buffer);
                filesMap.put((fileName.split("\\."))[0], json);
            }
            input.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return filesMap;
    }
}
