package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.cochera.miproyectointegrador.Login.LoginContract;
import com.cochera.miproyectointegrador.Login.LoginPresenter;

import com.cochera.miproyectointegrador.Recover.ActivityRecover;
import com.cochera.miproyectointegrador.Register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etClave;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    //codigo agregado para recuperar contraseña--borrar de ser neeccesario con las demas clases
        TextView textRecover = findViewById(R.id.textRecover);
        textRecover.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityRecover.class));
        });

        etCorreo = findViewById(R.id.editTextCorreolog);
        etClave = findViewById(R.id.editTextContrasenalog);
        btnLogin = findViewById(R.id.buttonLogin);
        dbHelper = new DBHelper(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String correo = etCorreo.getText().toString().trim();
                    String clave = etClave.getText().toString().trim();
//
//                    if (dbHelper.verificarUsuario(correo, clave)) {
//                        Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
//
//                        // Redirigir a ActivityAdminint
//                        Intent intent = new Intent(LoginActivity.this, ActivityAdminint.class);
//                        startActivity(intent);
//                        finish(); // opcional
//
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Correo o clave incorrectos", Toast.LENGTH_SHORT).show();
//                    }
                    Usuario usuario = dbHelper.verificarUsuario(correo, clave);
                    Intent intent = null;
                    if (usuario != null) {
                        int usuarioId = usuario.getId();
                        Toast.makeText(LoginActivity.this, "Bienvenido, " + usuario.getNombre(), Toast.LENGTH_SHORT).show();

                        // Redirección según perfil
                        switch (usuario.getPerfil()) {
                            case "Administrador":
                                intent = new Intent(LoginActivity.this, ActivityAdminint.class);
                                break;
                            case "Cliente":
                                intent = new Intent(LoginActivity.this, Activity_estacionamientos.class);
                                break;

                            default:
                                Toast.makeText(LoginActivity.this, "Perfil no reconocido", Toast.LENGTH_SHORT).show();
                        }

// 🚀 PASAMOS el usuarioId como extra
                        intent.putExtra("usuarioId", usuarioId);
                        startActivity(intent);

                        finish(); // Cierra LoginActivity

                    } else {
                        Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}