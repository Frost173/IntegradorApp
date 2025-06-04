package com.cochera.miproyectointegrador;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Estacionamiento;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class Activity_estacionamientos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon, imageView, imageView2, imageView5;
    private EditText etBuscador;
    private ImageButton btnReservas, btnHome, btnCalendario, btnPerfil;
    private RecyclerView recyclerView;
    private EstacionamientoAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionamientos);

        recyclerView = findViewById(R.id.recyclerEstacionamientos); // asegúrate de agregar este ID al layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        List<Estacionamiento> estacionamientos = dbHelper.obtenerEstacionamientos();
        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        adapter = new EstacionamientoAdapter(Activity_estacionamientos.this, estacionamientos,usuarioId);
        recyclerView.setAdapter(adapter);

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
//        imageView = findViewById(R.id.imageView);
//        imageView2 = findViewById(R.id.imageView2);
//        imageView5 = findViewById(R.id.imageView5);
        etBuscador = findViewById(R.id.etBuscador);
        btnReservas = findViewById(R.id.btnReservas);
        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Acción del menú hamburguesa (puedes abrir un Drawer si tienes uno)
        menuIcon.setOnClickListener(view -> {
            // Aquí puedes abrir un menú lateral si lo implementas
            Toast.makeText(this, "Menú presionado", Toast.LENGTH_SHORT).show();
        });



        // Acción del botón reservas
        btnReservas.setOnClickListener(view -> {
            Intent intent = new Intent(Activity_estacionamientos.this, Activity_reservas.class);
            startActivity(intent);
        });

        // Acción de búsqueda (puedes implementar el filtro)
        etBuscador.setOnEditorActionListener((v, actionId, event) -> {
            String texto = etBuscador.getText().toString();
            Toast.makeText(this, "Buscando: " + texto, Toast.LENGTH_SHORT).show();
            return true;
        });

        // Acciones para imágenes de estacionamientos
//        imageView.setOnClickListener(v -> Toast.makeText(this, "Estacionamiento Victoria", Toast.LENGTH_SHORT).show());
//        imageView2.setOnClickListener(v -> Toast.makeText(this, "Estacionamiento Bayóvar", Toast.LENGTH_SHORT).show());
//        imageView5.setOnClickListener(v -> Toast.makeText(this, "Estacionamiento Surco", Toast.LENGTH_SHORT).show());

        // Barra inferior - navegación
        btnHome.setOnClickListener(v -> Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show());
        btnCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_control_espacios.class);

            startActivity(intent);
        });
        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, Perfil.class);
            startActivity(intent);
        });
    }
}