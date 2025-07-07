package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.cochera.miproyectointegrador.Recover.ActivityRecover;
import com.cochera.miproyectointegrador.Register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etClave;
    private Button btnLogin, btnRegistro;
    private TextView tvRecover;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.editTextCorreolog);
        etClave = findViewById(R.id.editTextContrasenalog);
        btnLogin = findViewById(R.id.buttonLogin);
        btnRegistro = findViewById(R.id.buttonRegistrolog);
        tvRecover = findViewById(R.id.textRecover);
        dbHelper = new DBHelper(this);

        tvRecover.setOnClickListener(v ->
                startActivity(new Intent(this, ActivityRecover.class)));

        btnRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String clave = etClave.getText().toString().trim();

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(correo, clave)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                String uid = firebaseUser.getUid();

                                FirebaseFirestore.getInstance()
                                        .collection("Usuarios")
                                        .document(uid)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                String perfilFirebase = documentSnapshot.getString("perfil");

                                                if (perfilFirebase == null) {
                                                    Toast.makeText(this, "Perfil no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                // 🔹 Buscar en SQLite
                                                Usuario usuario = dbHelper.obtenerUsuarioPorUid(uid);

                                                // 🔹 Si no está, insertarlo desde Firebase
                                                if (usuario == null) {
                                                    Usuario nuevo = new Usuario();
                                                    nuevo.setUid(uid);
                                                    nuevo.setPerfil(perfilFirebase);
                                                    nuevo.setNombre(documentSnapshot.getString("nombre"));
                                                    nuevo.setApellido(documentSnapshot.getString("apellido"));
                                                    nuevo.setCorreo(documentSnapshot.getString("correo"));
                                                    nuevo.setCelular(documentSnapshot.getString("celular"));
                                                    nuevo.setContrasena(clave);
                                                    nuevo.setEstado("activo");
                                                    nuevo.setEdad(18);

                                                    boolean insertado = dbHelper.insertarUsuarioCompleto(nuevo);
                                                    if (insertado) {
                                                        usuario = dbHelper.obtenerUsuarioPorUid(uid);
                                                    } else {
                                                        Toast.makeText(this, "Error al guardar usuario local", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }

                                                // 🔹 Validar perfil
                                                if (usuario != null && usuario.getPerfil().equalsIgnoreCase(perfilFirebase)) {
                                                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                    prefs.edit().putInt("usuario_id", usuario.getId()).apply();

                                                    Toast.makeText(this, "Bienvenido, " + usuario.getNombre(), Toast.LENGTH_SHORT).show();

                                                    Intent intent = ("Administrador".equalsIgnoreCase(perfilFirebase))
                                                            ? new Intent(this, ActivityAdminint.class)
                                                            : new Intent(this, Activity_estacionamientos.class);

                                                    intent.putExtra("usuarioId", usuario.getId());
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(this, "Perfil no coincide", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(this, "Documento de usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Error al obtener datos de Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });

                            } else {
                                Toast.makeText(this, "Verifica tu correo electrónico antes de ingresar", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
