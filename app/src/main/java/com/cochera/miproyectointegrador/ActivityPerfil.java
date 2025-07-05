package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;

public class ActivityPerfil extends AppCompatActivity {

    private EditText editTextNombre, editTextCorreo, editTextTelefono;
    private SharedPreferences sharedPreferences;
    private int usuarioId;
//    private static final String TAG = "ActivityPerfil"; // 👈 Etiqueta para Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toast.makeText(this, "Ya estás en pERFIL", Toast.LENGTH_SHORT).show();

        // Obtener el ID del usuario desde el intent
        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Inicializar vistas
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextTelefono = findViewById(R.id.editTextTelefono);

        // Configurar barra inferior
          setupBottomBar();

        // Cargar datos del usuario
            cargarDatosUsuario();
    }


    private void setupBottomBar() {
        try {
            ImageButton btnHome = findViewById(R.id.btnHome);
            ImageButton btnCalendario = findViewById(R.id.btnCalendario);
            ImageButton btnPerfil = findViewById(R.id.btnPerfil);

            btnHome.setOnClickListener(v -> {
                Intent intent = new Intent(this, Activity_estacionamientos.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
                finish();
            });

            btnCalendario.setOnClickListener(v ->
                    Toast.makeText(this, "Funcionalidad de calendario", Toast.LENGTH_SHORT).show());

            btnPerfil.setOnClickListener(v ->
                    Toast.makeText(this, "Ya estás en el perfil", Toast.LENGTH_SHORT).show());

        } catch (Exception e) {
            Log.e("", "Error en la barra inferior: " + e.getMessage());
        }
    }

    private void cargarDatosUsuario() {
        try {
            DBHelper dbHelper = new DBHelper(this);
            Usuario usuario = dbHelper.obtenerUsuarioPorId(usuarioId);

            if (usuario != null) {
                Log.d("TAG", "Usuario encontrado: " + usuario.getNombre());

                String nombre = usuario.getNombre() != null ? usuario.getNombre() : "";
                String apellido = usuario.getApellido() != null ? usuario.getApellido() : "";
                editTextNombre.setText(nombre + " " + apellido);

                String correo = usuario.getCorreo() != null ? usuario.getCorreo() : "";
                editTextCorreo.setText(correo);

                // SharedPreferences para cargar teléfono
                sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                if (sharedPreferences != null) {
                    String telefono = sharedPreferences.getString("telefono_" + usuarioId, "");
                    editTextTelefono.setText(telefono);
                } else {
                    Log.w("TAG", "SharedPreferences es null");
                }

            } else {
                Log.e("", "Usuario no encontrado con ID: " + usuarioId);
                Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("TAG", "Error al cargar datos del usuario", e);
            Toast.makeText(this, "Error al cargar perfil: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("telefono_" + usuarioId, editTextTelefono.getText().toString());
                editor.apply();
            }
        } catch (Exception e) {
            Log.e("TAG", "Error al guardar teléfono en SharedPreferences: " + e.getMessage());
        }
    }
}
