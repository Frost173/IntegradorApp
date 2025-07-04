package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AjustesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes); // Asegúrate que este layout existe

        // Referencias a los layouts y botón
        LinearLayout layoutSeguridad = findViewById(R.id.layoutSeguridad);
        LinearLayout layoutTema = findViewById(R.id.layoutTema);
        LinearLayout layoutPolitica = findViewById(R.id.layoutPolitica);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Seguridad
        layoutSeguridad.setOnClickListener(v -> {
            Intent intent = new Intent(AjustesActivity.this, Activity_seguridad.class);
            startActivity(intent);
        });

        // Cambiar tema
        layoutTema.setOnClickListener(v -> {
            Intent intent = new Intent(AjustesActivity.this, CambiarTemaActivity.class);
            startActivity(intent);
        });

        // Política de privacidad
        layoutPolitica.setOnClickListener(v -> {
            Intent intent = new Intent(AjustesActivity.this, PoliticaPrivacidadActivity.class);
            startActivity(intent);
        });

        // ✅ Cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            // Cerrar sesión en Firebase Auth
            FirebaseAuth.getInstance().signOut();

            // Limpiar SharedPreferences locales
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Toast.makeText(AjustesActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

            // Redirigir al Login y borrar historial de navegación
            Intent intent = new Intent(AjustesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
