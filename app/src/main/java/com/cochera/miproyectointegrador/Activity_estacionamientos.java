package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Estacionamiento;
import com.google.android.material.navigation.NavigationView;


import java.util.List;

public class Activity_estacionamientos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;
    private EditText etBuscador;
    private ImageButton btnReservas, btnHome, btnCalendario, btnPerfil;
    private RecyclerView recyclerView;
    private EstacionamientoAdapter adapter;
    private DBHelper dbHelper;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionamientos);

        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerEstacionamientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar base de datos y obtener lista de estacionamientos
        dbHelper = new DBHelper(this);
        List<Estacionamiento> estacionamientos = dbHelper.obtenerEstacionamientos();

        // Obtener usuarioId pasado desde LoginActivity u otra actividad
        int usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Configurar adapter con datos y usuarioId
        adapter = new EstacionamientoAdapter(Activity_estacionamientos.this, estacionamientos, usuarioId);
        recyclerView.setAdapter(adapter);

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view); // Asegúrate que el id es nav_view en tu XML
        menuIcon = findViewById(R.id.menuIcon);
        etBuscador = findViewById(R.id.etBuscador);
        btnReservas = findViewById(R.id.btnReservas);
        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Acción menú hamburguesa: abrir drawer
        menuIcon.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
/*
        // Manejar selección de ítems del Navigation Drawer (opcional)
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_reservas:
                    Toast.makeText(this, "Reservas seleccionado", Toast.LENGTH_SHORT).show();
                    // Aquí puedes abrir la actividad o fragmento correspondiente
                    break;
                case R.id.nav_historial:
                    Toast.makeText(this, "Historial seleccionado", Toast.LENGTH_SHORT).show();
                    break;
                // Agrega más casos según tu menú
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }); */

        // Acción botón Calendario: abre CalendarioActivity
        btnCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_estacionamientos.this, CalendarioActivity.class);
            startActivity(intent);
        });

        // Acción botón Reservas: abre Activity_reservas
        btnReservas.setOnClickListener(view -> {
            Intent intent = new Intent(Activity_estacionamientos.this, Activity_reservas.class);
            startActivity(intent);
        });

        // Acción búsqueda en EditText
        etBuscador.setOnEditorActionListener((v, actionId, event) -> {
            String texto = etBuscador.getText().toString();
            Toast.makeText(this, "Buscando: " + texto, Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar lógica para filtrar la lista del RecyclerView
            return true;
        });

        // Barra inferior - navegación
        btnHome.setOnClickListener(v ->
                Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
        );

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, Perfil.class);
            startActivity(intent);
        });
    }
}
