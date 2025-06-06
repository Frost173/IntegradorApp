package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    private ImageButton btnFlecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity); // Asegúrate que este es el nombre correcto de tu layout

        btnFlecha = findViewById(R.id.btnFlecha);

        btnFlecha.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, Activity_estacionamientos.class);
            startActivity(intent);
            finish(); // Opcional: cerrar esta actividad para no volver con back
        });
    }
}


