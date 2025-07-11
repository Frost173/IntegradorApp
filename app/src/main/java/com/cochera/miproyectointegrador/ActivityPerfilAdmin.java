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

public class ActivityPerfilAdmin extends AppCompatActivity {

    private ImageButton btnHome, btnCalendario, btnPerfil;
    private EditText editTextNombre, editTextCorreo, editTextPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);

        // Referencias de vista
        btnHome = findViewById(R.id.btnHome);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnPerfil = findViewById(R.id.btnPerfil);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextPerfil = findViewById(R.id.editTextPerfil);

        // Obtener ID de sesión guardado
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId != -1) {
            DBHelper dbHelper = new DBHelper(this);
            Usuario admin = dbHelper.obtenerUsuarioPorId(usuarioId);

            if (admin != null) {
                // Mostrar datos en los campos de perfil
                String nombreCompleto = admin.getNombre() + " " + admin.getApellido();
                editTextNombre.setText(nombreCompleto);
                editTextCorreo.setText(admin.getCorreo());
                editTextPerfil.setText(admin.getPerfil());
            } else {
                Toast.makeText(this, "No se encontró el usuario en la base de datos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show();
        }

        // Botones de navegación
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityAdminint.class));
            finish();
        });

        btnCalendario.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityHistorialReserva.class));
            finish();
        });

        btnPerfil.setOnClickListener(v -> {
            // Ya está en Perfil, no redirecciona
            Toast.makeText(this, "Ya estás en el perfil", Toast.LENGTH_SHORT).show();
        });
    }
}
