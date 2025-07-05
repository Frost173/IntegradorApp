package com.cochera.miproyectointegrador.Register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.R;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    private EditText etNombre, etApellido, etCorreo, etContraseña, etCelular;
    private Button btnRegistrar;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Vincula todos los campos
        etNombre = findViewById(R.id.editTextName);
        etApellido = findViewById(R.id.editTextApellido);  // este campo ya está en tu XML
        etCorreo = findViewById(R.id.editTextCorreo);
        etContraseña = findViewById(R.id.editTextContrasena);
        etCelular = findViewById(R.id.editTextPhone);
        btnRegistrar = findViewById(R.id.buttonRegistrarse);

        presenter = new RegisterPresenter(this, this);

        btnRegistrar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String contraseña = etContraseña.getText().toString().trim();
            String celular = etCelular.getText().toString().trim();
            int perfilid = 2; // cliente por defecto

            // Llamamos al Presenter
            presenter.registrarUsuario(nombre, apellido, correo, contraseña, celular, perfilid);
        });
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "✅ Registro exitoso. Verifica tu correo", Toast.LENGTH_LONG).show();
        finish(); // Regresar al login
    }

    @Override
    public void showRegisterError(String msg) {
        Toast.makeText(this, "❌ Error: " + msg, Toast.LENGTH_LONG).show();
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
