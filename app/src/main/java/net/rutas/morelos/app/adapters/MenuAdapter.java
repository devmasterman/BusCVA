package net.rutas.morelos.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.rutas.morelos.app.R;
import net.rutas.morelos.app.dto.MenuDTO;

/**
 * Created by eroman on 25/10/16.
 */

public class MenuAdapter extends ArrayAdapter<Object> {
    // Array con los Datos que se mostraran en la lista
    private static MenuDTO[] items_menu = new MenuDTO[] {
            new MenuDTO("Itinerario Rutas", "Mapa - Lugares por donde pasa",
                    /*R.drawable.mapa*/0),
            new MenuDTO("Ubicación Actual", "Sugiere qué ruta abordar",
                    /*R.drawable.mapa*/0),
            new MenuDTO("Taxi Seguro", "Radio Taxis",
                    /*R.drawable.mapa*/0),
            new MenuDTO("Números Emergencia", "Ambulancia, Policia, Bomberos",
                    /*R.drawable.mapa*/0),
            new MenuDTO("Mapa", "Mapa - Maps",
                   /*R.drawable.mapa*/0),
            new MenuDTO("Salir", "", 0) };

    // Constructor de la clase AdaptadorEIC
    Activity context;

    public MenuAdapter(Activity context) {
        super(context, R.layout.activity_menu_listitem, items_menu);
        this.context = context;
    }

    // Metodo para asignar cada elemento del listitem a los datos
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.activity_menu_listitem,
                null);

        TextView lblTitulo = (TextView) item
                .findViewById(R.id.tvTituloMenuPrincipal);
        lblTitulo.setText(items_menu[position].getTitulo());

        TextView lblSubtitulo = (TextView) item
                .findViewById(R.id.tvSubTituloMenuPrincipal);
        lblSubtitulo.setText(items_menu[position].getSubtitulo());

        ImageView lblImagen = (ImageView) item
                .findViewById(R.id.iViconMenuPrincipal);
        lblImagen.setImageResource(items_menu[position].getImagen());
        return (item);
    }
}
