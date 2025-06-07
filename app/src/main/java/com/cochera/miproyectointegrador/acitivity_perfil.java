package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;

public class acitivity_perfil extends AppCompatActivity {

    private EditText editTextNombre, editTextCorreo, editTextDescripcion;
    private SharedPreferences sharedPreferences;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Obtener ID del usuario del Intent
        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Inicializar vistas
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        // Configurar barra inferior
        setupBottomBar();

        // Cargar datos del usuario
        cargarDatosUsuario();
    }

    private void setupBottomBar() {
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnCalendario = findViewById(R.id.btnCalendario);
        ImageButton btnPerfil = findViewById(R.id.btnPerfil);

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, Activity_estacionamientos.class)
                    .putExtra("usuarioId", usuarioId));
            finish();
        });

        btnCalendario.setOnClickListener(v -> {
            // Aquí iría la actividad del calendario
            Toast.makeText(this, "Funcionalidad de calendario", Toast.LENGTH_SHORT).show();
        });

        btnPerfil.setOnClickListener(v -> {
            // Ya estamos en el perfil
        });
    }

    private void cargarDatosUsuario() {
        // Obtener datos del usuario desde la base de datos
        DBHelper dbHelper = new DBHelper(this);
        Usuario usuario = dbHelper.obtenerUsuarioPorId(usuarioId);

        if (usuario != null) {
            editTextNombre.setText(usuario.getNombre() + " " + usuario.getApellido());
            editTextCorreo.setText(usuario.getCorreo());

            // Cargar descripción desde SharedPreferences
            sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String descripcion = sharedPreferences.getString("descripcion_" + usuarioId, "");
            editTextDescripcion.setText(descripcion);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardar la descripción cuando la actividad pasa a segundo plano
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("descripcion_" + usuarioId, editTextDescripcion.getText().toString());
        editor.apply();
    }
}
