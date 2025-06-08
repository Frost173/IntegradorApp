package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.Register.RegisterContract;
import com.cochera.miproyectointegrador.Register.RegisterPresenter;

public class ActivityRegister extends AppCompatActivity implements RegisterContract.View {

    private EditText etNombre, etApellido, etCorreo, etContrasena, etCelular;
    private Button btnRegistrar;
    private RegisterPresenter presenter;

    public static final int ROL_CONDUCTOR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || celular.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreo.setError("Correo inválido");
                etCorreo.requestFocus();
                return;
            }

            if (contrasena.length() < 6) {
                etContrasena.setError("La contraseña debe tener al menos 6 caracteres");
                etContrasena.requestFocus();
                return;
            }

            if (!celular.matches("\\d{9}")) {
                etCelular.setError("El número de celular debe tener 9 dígitos");
                etCelular.requestFocus();
                return;
            }

            presenter.registrarUsuario(nombre, apellido, correo, contrasena, celular, ROL_CONDUCTOR);
        });

        Button btnIrLogin = findViewById(R.id.buttonInicioSesion);
        btnIrLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityRegister.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showRegisterError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String message) { }
    @Override
    public void showError(String message) { }
}

