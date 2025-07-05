package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;

public class ActivityPerfilAdmin extends AppCompatActivity {

    private ImageButton btnHome, btnCalendario, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin); // tu XML perfil que enviaste

        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPerfil = findViewById(R.id.btnPerfil);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityPerfilAdmin.this, ActivityAdminint.class);
            startActivity(intent);
            finish(); // opcional: evita que se acumule esta actividad en la pila
        });


        btnCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityPerfilAdmin.this, ActivityHistorialReserva.class);
            startActivity(intent);
            finish(); // opcional: evita que se acumule esta actividad en la pila
        });


        btnPerfil.setOnClickListener(v -> {
//            Toast.makeText(ActivityPerfilAdmin.this, "Ya estás en Perfil", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityPerfilAdmin.this, ActivityPerfil.class);
            startActivity(intent);
            finish();
        });
    }
}

