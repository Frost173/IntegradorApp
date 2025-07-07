package com.cochera.miproyectointegrador.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.LoginActivity;
import com.cochera.miproyectointegrador.R;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    private EditText etNombre, etApellido, etCorreo, etContrasena, etCelular;
    private Button btnRegistrar;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Enlazar elementos del layout
        etNombre = findViewById(R.id.editTextName);
        etApellido = findViewById(R.id.editTextApellido);
        etCorreo = findViewById(R.id.editTextCorreo);
        etContrasena = findViewById(R.id.editTextContrasena);
        etCelular = findViewById(R.id.editTextPhone);
        btnRegistrar = findViewById(R.id.buttonRegistrarse);

        presenter = new RegisterPresenter(this, this);

        btnRegistrar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();
            String celular = etCelular.getText().toString().trim();
            String perfil = "Cliente"; // Por defecto al registrarse

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || celular.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.registrarUsuario(nombre, apellido, correo, contrasena, celular, perfil);
        });

        // Botón para volver al login (opcional)
        Button btnIrLogin = findViewById(R.id.buttonInicioSesion);
        btnIrLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "✅ Registro exitoso. Revisa tu correo.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showRegisterError(String msg) {
        Toast.makeText(this, "❌ " + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
