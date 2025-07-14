package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAdminint extends AppCompatActivity {

    private Button btnDashboard, btnTarifas, btnHistorialReserva, btnReservas;
    private Button btnChat, btnCerrarSesion;
    private ImageButton btnHome, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminint);

        // Botones principales
        btnDashboard = findViewById(R.id.btnDashboard);
        btnTarifas = findViewById(R.id.btnTarifas);
        btnHistorialReserva = findViewById(R.id.btnHistorialReserva);
        btnReservas = findViewById(R.id.btnReservas);
        btnChat = findViewById(R.id.btnChat);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion); // nuevo botón

        // Botones inferiores
        btnHome = findViewById(R.id.btnHome);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Ir al Dashboard
        btnDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });

        // Ir a Tarifas
        btnTarifas.setOnClickListener(v -> {
            startActivity(new Intent(this, TarifasActivity.class));
        });

        // Ir al historial de reservas
        btnHistorialReserva.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityHistorialReserva.class));
        });

        // Ir a reservas pendientes
        btnReservas.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityReservaPendiente.class));
        });

        // Ir al chat
        btnChat.setOnClickListener(v -> {
            Intent listaUsuariosIntent = new Intent(this, ListaUsuariosActivity.class);
            startActivity(listaUsuariosIntent);
        });

        // Ir a esta misma pantalla (Home)
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityAdminint.class));
        });

        // Ir al perfil
        btnPerfil.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityPerfilAdmin.class));
        });

        // Lógica de cierre de sesión
        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            preferences.edit().clear().apply(); // Borra datos de sesión
            Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Borra el historial de actividades
            startActivity(intent);
            finish(); // Finaliza esta actividad
        });
    }
}
