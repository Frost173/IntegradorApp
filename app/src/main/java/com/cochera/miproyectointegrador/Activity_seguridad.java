package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // Cambiado a Button
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_seguridad extends AppCompatActivity {

    ImageButton btnBack, btnFlecha1;
    Button btnAceptar;  // Cambiado a Button
    private static final int REQUEST_CAMBIAR_CONTRASENA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seguridad);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnFlecha1 = findViewById(R.id.btnFlecha1);
        btnFlecha1.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_seguridad.this, ActivityCambiarContrasena.class);
            startActivityForResult(intent, REQUEST_CAMBIAR_CONTRASENA);
        });

        btnAceptar = findViewById(R.id.btnAceptar);  // Ahora Button, no ImageButton
        btnAceptar.setOnClickListener(v -> finish()); // Al hacer clic, vuelve a la actividad anterior
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMBIAR_CONTRASENA && resultCode == RESULT_OK) {
            // Aquí puedes hacer algo cuando regresa de cambiar contraseña, si quieres
        }
    }
}
