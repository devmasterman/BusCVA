package net.rutas.morelos.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.rutas.morelos.app.R;
import net.rutas.morelos.app.bo.IPreProcesarBO;
import net.rutas.morelos.app.bo.impl.PreProcesarBO;
import net.rutas.morelos.app.utils.FileUtils;
import net.rutas.morelos.app.utils.IActivityUtil;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IActivityUtil {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_drawer);;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        iniciarComponentes();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.nav_routes) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(), DireccionesActivity.class));
            finish();
        } else if (id == R.id.nav_map) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            finish();
        } else if (id == R.id.nav_mylocation) {
            //startActivity(new Intent(getApplicationContext(), SlideshowActivity.class));
        } else if (id == R.id.nav_manual) {
            startActivity(new Intent(getApplicationContext(), ManualUsuarioActivity.class));
            finish();
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(getApplicationContext(), AcercaDeActivity.class));
            finish();
        } else if (id == R.id.nav_conf) {
            startActivity(new Intent(getApplicationContext(), ConfiguracionActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void iniciarComponentes() {
    }

    @Override
    public void showWindowModal(String msg, Intent i) {

    }

    @Override
    public boolean isOnline() {
        return false;
    }
}
