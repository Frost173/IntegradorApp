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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityPerfil extends AppCompatActivity {

    private EditText editTextNombre, editTextCorreo, editTextTelefono;
    private SharedPreferences sharedPreferences;
    private int usuarioId;

    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toast.makeText(this, "Ya estás en PERFIL", Toast.LENGTH_SHORT).show();

        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextTelefono = findViewById(R.id.editTextTelefono);

        setupBottomBar();
        cargarDatosUsuario();            // desde SQLite
        cargarPerfilDesdeFirestore();    // desde Firestore
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
            Log.e("BottomBar", "Error en la barra inferior: " + e.getMessage());
        }
    }

    private void cargarDatosUsuario() {
        try {
            DBHelper dbHelper = new DBHelper(this);
            Usuario usuario = dbHelper.obtenerUsuarioPorId(usuarioId);

            if (usuario != null) {
                String nombre = usuario.getNombre() != null ? usuario.getNombre() : "";
                String apellido = usuario.getApellido() != null ? usuario.getApellido() : "";
                editTextNombre.setText(nombre + " " + apellido);

                String correo = usuario.getCorreo() != null ? usuario.getCorreo() : "";
                editTextCorreo.setText(correo);

                sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                if (sharedPreferences != null) {
                    String telefono = sharedPreferences.getString("telefono_" + usuarioId, "");
                    editTextTelefono.setText(telefono);
                }
            } else {
                Toast.makeText(this, "No se encontró el usuario local", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("SQLite", "Error al cargar datos del usuario", e);
            Toast.makeText(this, "Error al cargar perfil local: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void cargarPerfilDesdeFirestore() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "❌ Usuario no autenticado", Toast.LENGTH_SHORT).show();
            Log.e("Firestore", "FirebaseUser es null");
            return;
        }

        String uid = firebaseUser.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Log.d("Firestore", "Consultando perfil para UID: " + uid);

        firestore.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String nombre = document.getString("nombre");
                        String apellido = document.getString("apellido");
                        String correo = document.getString("correo");
                        String celular = document.getString("celular");

                        Log.d("Firestore", "Datos recibidos: " +
                                "nombre=" + nombre + ", apellido=" + apellido +
                                ", correo=" + correo + ", celular=" + celular);

                        editTextNombre.setText((nombre != null ? nombre : "") + " " + (apellido != null ? apellido : ""));
                        editTextCorreo.setText(correo != null ? correo : "");
                        editTextTelefono.setText(celular != null ? celular : "");

                    } else {
                        Toast.makeText(this, "⚠️ Perfil no encontrado en Firebase", Toast.LENGTH_SHORT).show();
                        Log.w("Firestore", "Documento no existe para UID: " + uid);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "❌ Error al obtener perfil: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Firestore", "Fallo en consulta Firestore", e);
                });
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
