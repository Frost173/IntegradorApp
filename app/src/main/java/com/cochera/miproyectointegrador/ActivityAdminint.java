package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAdminint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminint);

        // Inicializando botones
        Button btnTarifas = findViewById(R.id.btnTarifas);
        Button btnGestionEspacio = findViewById(R.id.btnGestionEspacio);
        ImageButton btnPerfil = findViewById(R.id.btnPerfil); // Botón para ir al perfil
        ImageButton btnCalendario = findViewById(R.id.btnCalendario);



        // Acción botón Tarifas
        btnTarifas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAdminint.this, TarifarioActivity.class);
                startActivity(intent);
            }
        });

        // Acción botón Gestión de Espacio
        btnGestionEspacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAdminint.this, activity_control_espacios.class);
                startActivity(intent);
            }
        });

        // Acción botón Perfil
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAdminint.this, Perfil.class);
                startActivity(intent);
            }
        });
        // Acción botón Perfil
        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAdminint.this, Activity_reservas.class);
                startActivity(intent);
            }
        });
    }
}



