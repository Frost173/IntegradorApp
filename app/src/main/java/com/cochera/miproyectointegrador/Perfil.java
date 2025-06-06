package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Perfil extends AppCompatActivity {

    private ImageButton btnPerfil, btnHome, btnCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil); // Asegúrate que el XML se llame así

        // Ajuste de padding para barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar botones de la barra inferior
        btnPerfil = findViewById(R.id.btnPerfil);
        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);

        // Acción botón Perfil: ya estás en Perfil, mostrar mensaje
        btnPerfil.setOnClickListener(v -> {
            // Opcional: mostrar mensaje o no hacer nada
        });

        // Acción botón Home: abrir Activity_estacionamientos
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, Activity_estacionamientos.class);
            startActivity(intent);
            finish();
        });

        // Acción botón Calendario: abrir CalendarioActivity
        btnCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, CalendarioActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

