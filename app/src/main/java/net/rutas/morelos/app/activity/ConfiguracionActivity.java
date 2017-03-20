package net.rutas.morelos.app.activity;

import android.os.Bundle;

import net.rutas.morelos.app.R;

public class ConfiguracionActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_configuracion, frameLayout);

        /**
         * Establecer Titulo
         */
        setTitle("Configuración");

    }
}
