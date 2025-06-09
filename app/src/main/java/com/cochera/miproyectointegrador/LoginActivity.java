package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.cochera.miproyectointegrador.Recover.ActivityRecover;
import com.cochera.miproyectointegrador.Register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etClave;
    private Button btnLogin, btnRegistro;
    private TextView tvRecover;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etCorreo = findViewById(R.id.editTextCorreolog);
        etClave = findViewById(R.id.editTextContrasenalog);
        btnLogin = findViewById(R.id.buttonLogin);
        btnRegistro = findViewById(R.id.buttonRegistrolog);
        tvRecover = findViewById(R.id.textRecover);
        dbHelper = new DBHelper(this);

        // Acción de recuperación de contraseña
        tvRecover.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRecover.class));
        });

        // Acción de registro
        btnRegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRegister.class));
        });

        // Acción de inicio de sesión
        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String clave = etClave.getText().toString().trim();

            // Validación de campos vacíos
            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Usuario usuario = dbHelper.verificarUsuario(correo, clave);

                if (usuario != null) {
                    int usuarioId = usuario.getId();
                    String perfil = usuario.getPerfil();
                    String nombre = usuario.getNombre();

                    // GUARDAR usuario_id EN SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("usuario_id", usuarioId);
                    editor.apply();

                    Toast.makeText(this, "Bienvenido, " + nombre, Toast.LENGTH_SHORT).show();

                    Intent intent;

                    // Redireccionar según perfil
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

                    // También se puede pasar el ID por intent si lo necesitas
                    intent.putExtra("usuarioId", usuarioId);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error al iniciar sesión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }
}

