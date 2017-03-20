package net.rutas.morelos.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import net.rutas.morelos.app.R;

public class ManualUsuarioActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_manual_usuario, frameLayout);

        /**
         * Establecer Titulo
         */
        setTitle("Manual de Usuario");

        TextView tv = (TextView) findViewById(R.id.tv_long);
        tv.setMovementMethod(new ScrollingMovementMethod());
    }
}
