package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAdminint extends AppCompatActivity {

    private Button btnDashboard, btnTarifas, btnHistorialReserva, btnReservas;
    private ImageButton btnHome, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminint);

        // Botones principales
        btnDashboard = findViewById(R.id.btnDashboard);
        btnTarifas = findViewById(R.id.btnTarifas);
        btnHistorialReserva = findViewById(R.id.btnHistorialReserva);
        btnReservas = findViewById(R.id.btnReservas);  // Botón presente, aún sin funcionalidad

        // Botones inferiores
        btnHome = findViewById(R.id.btnHome);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Ir al Dashboard
        btnDashboard.setOnClickListener(v -> {
            Intent dashboardIntent = new Intent(ActivityAdminint.this, DashboardActivity.class);
            startActivity(dashboardIntent);
        });

        // Ir a Tarifas
        btnTarifas.setOnClickListener(v -> {
            Intent tarifasIntent = new Intent(ActivityAdminint.this, TarifasActivity.class);
            startActivity(tarifasIntent);
        });

        // Ir al historial de reservas
        btnHistorialReserva.setOnClickListener(v -> {
            Intent historialIntent = new Intent(ActivityAdminint.this, ActivityHistorialReserva.class);
            startActivity(historialIntent);
        });

        // Botón Reservas aún sin funcionalidad
        btnReservas.setOnClickListener(v -> {
            Log.d("ActivityAdminint", "Botón de Reservas presionado, funcionalidad aún no implementada.");
        });

        // Botón inferior Home
        btnHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(ActivityAdminint.this, ActivityAdminint.class);
            startActivity(homeIntent);
        });

        // Ir al perfil del admin
        btnPerfil.setOnClickListener(v -> {
            Intent perfilIntent = new Intent(ActivityAdminint.this, ActivityPerfilAdmin.class);
            startActivity(perfilIntent);
        });
    }
}
