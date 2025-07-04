package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.cochera.miproyectointegrador.Recover.ActivityRecover;
import com.cochera.miproyectointegrador.Register.RegisterPresenter;
import com.cochera.miproyectointegrador.Register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        tvRecover.setOnClickListener(v -> startActivity(new Intent(this, ActivityRecover.class)));
        btnRegistro.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

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
                                Usuario usuario = dbHelper.verificarUsuario(correo, clave);
                                if (usuario != null) {
                                    int usuarioId = usuario.getId();
                                    String perfil = usuario.getPerfil();
                                    String nombre = usuario.getNombre();

                                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("usuario_id", usuarioId);
                                    editor.apply();

                                    Toast.makeText(this, "Bienvenido, " + nombre, Toast.LENGTH_SHORT).show();

                                    Intent intent;
                                    switch (perfil) {
                                        case "Administrador":
                                            intent = new Intent(this, ActivityAdminint.class);
                                            break;
                                        case "Cliente":
                                            intent = new Intent(this, Activity_estacionamientos.class);
                                            break;
                                        default:
                                            Toast.makeText(this, "Perfil no reconocido", Toast.LENGTH_SHORT).show();
                                            return;
                                    }

                                    intent.putExtra("usuarioId", usuarioId);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(this, "Verifica tu correo electrónico", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Correo o clave incorrectos (Firebase)", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
