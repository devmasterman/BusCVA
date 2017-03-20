package net.rutas.morelos.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.rutas.morelos.app.R;

public class AcercaDeActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_acerca_de, frameLayout);

        /**
         * Establecer Titulo
         */
        setTitle("Acerca de ...");
    }
}
