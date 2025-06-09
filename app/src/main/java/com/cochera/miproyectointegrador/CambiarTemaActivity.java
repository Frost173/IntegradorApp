package com.cochera.miproyectointegrador;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class CambiarTemaActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs_tema";
    private static final String KEY_TEMA = "tema";

    private RadioGroup radioGroupTema;
    private RadioButton rbSistema, rbClaro, rbOscuro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar tema guardado antes de setContentView
        aplicarTemaGuardado();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oscuro_claro);

        // Activar flecha para volver atrás en la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cambiar Tema");
        }

        // Referencias
        radioGroupTema = findViewById(R.id.radioGroupTema);
        rbSistema = findViewById(R.id.rbSistema);
        rbClaro = findViewById(R.id.rbClaro);
        rbOscuro = findViewById(R.id.rbOscuro);
        ImageButton btnBack = findViewById(R.id.btnBack); // 🔹 Añadido

        // Manejar clic en el botón atrás (ImageButton personalizado)
        btnBack.setOnClickListener(v -> finish()); // 🔹 Añadido

        // Cargar tema guardado
        int temaGuardado = obtenerTemaGuardado();
        switch (temaGuardado) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                rbClaro.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                rbOscuro.setChecked(true);
                break;
            default:
                rbSistema.setChecked(true);
                break;
        }

        // Cambiar tema al seleccionar
        radioGroupTema.setOnCheckedChangeListener((group, checkedId) -> {
            int modo;
            if (checkedId == R.id.rbClaro) {
                modo = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.rbOscuro) {
                modo = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                modo = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }

            guardarTema(modo);
            AppCompatDelegate.setDefaultNightMode(modo);
            recreate(); // Reinicia para aplicar cambios
        });
    }


    // Método para manejar la flecha de "atrás"
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Finaliza esta actividad
        return true;
    }

    private void aplicarTemaGuardado() {
        int modo = obtenerTemaGuardado();
        AppCompatDelegate.setDefaultNightMode(modo);
    }

    private int obtenerTemaGuardado() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(KEY_TEMA, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    private void guardarTema(int modo) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(KEY_TEMA, modo).apply();
    }
}
