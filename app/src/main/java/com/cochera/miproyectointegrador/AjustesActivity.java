package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes); // Cambia por el nombre real de tu layout

        // Referencias a los layouts y botón
        LinearLayout layoutSeguridad = findViewById(R.id.layoutSeguridad);
        LinearLayout layoutTema = findViewById(R.id.layoutTema);
        LinearLayout layoutPolitica = findViewById(R.id.layoutPolitica);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Seguridad
        layoutSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjustesActivity.this, Activity_seguridad.class);
                startActivity(intent);
            }
        });


        // Cambiar tema
        layoutTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjustesActivity.this, CambiarTemaActivity.class);
                startActivity(intent);
            }
        });

        // Política de privacidad
        layoutPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AjustesActivity.this, PoliticaPrivacidadActivity.class);
                startActivity(intent);
            }
        });

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar SharedPreferences donde guardas sesión (ajusta el nombre "userPrefs" al que uses)
                getSharedPreferences("userPrefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();

                Toast.makeText(AjustesActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                // Ir a LoginActivity y limpiar la pila para que no pueda volver con back
                Intent intent = new Intent(AjustesActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
