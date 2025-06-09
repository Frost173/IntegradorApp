package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAdminint extends AppCompatActivity {

    private Button btnTarifas, btnGestionEspacio;
    private ImageButton btnHome, btnHistorial, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminint);

        btnTarifas = findViewById(R.id.btnTarifas);
        btnGestionEspacio = findViewById(R.id.btnGestionEspacio);

        btnHome = findViewById(R.id.btnHome);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnTarifas.setOnClickListener(v -> {
            Intent tarifasIntent = new Intent(ActivityAdminint.this, TarifasActivity.class);
            startActivity(tarifasIntent);
        });

        btnGestionEspacio.setOnClickListener(v -> {
            Intent gestionIntent = new Intent(ActivityAdminint.this, ActivityAdminint.class); // Cambia a tu clase real
            startActivity(gestionIntent);
        });

        btnHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(ActivityAdminint.this, ActivityAdminint.class); // Cambia a tu actividad principal
            startActivity(homeIntent);
        });

        if (btnHistorial != null) {
            btnHistorial.setOnClickListener(v -> {
                Intent historialIntent = new Intent(ActivityAdminint.this, ActivityHistorialReserva.class);
                startActivity(historialIntent);
            });
        } else {
            Log.e("ActivityAdminint", "btnHistorial no encontrado en el layout");
        }

        // Aquí agregamos el listener para btnPerfil
        btnPerfil.setOnClickListener(v -> {
            Intent perfilIntent = new Intent(ActivityAdminint.this, ActivityPerfilAdmin.class);
            startActivity(perfilIntent);
        });
    }
}


