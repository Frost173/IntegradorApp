package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Activity_estacionamientos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.Activity_estacionamientos);

        // Configura el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializa DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Configura el toggle para el menú hamburguesa
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Maneja los clicks en las opciones del menú lateral con if-else
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_inicio) {
                    startActivity(new Intent(Activity_estacionamientos.this, MainActivity.class));
                } else if (id == R.id.nav_perfil) {
                    startActivity(new Intent(Activity_estacionamientos.this, InterfazCliente.class));
                } else if (id == R.id.nav_reservas) {
                    startActivity(new Intent(Activity_estacionamientos.this, Activity_reservas.class));
                } else if (id == R.id.nav_cerrar_sesion) {
                    Toast.makeText(Activity_estacionamientos.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar lógica para cerrar sesión
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Botón para ir a reservas
        ImageButton btnReservas = findViewById(R.id.btnReservas);
        btnReservas.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_estacionamientos.this, Activity_reservas.class);
            startActivity(intent);
        });
    }

    // Para cerrar el menú si está abierto al presionar atrás
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
