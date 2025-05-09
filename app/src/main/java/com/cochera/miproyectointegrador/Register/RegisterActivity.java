package com.cochera.miproyectointegrador.Register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.R;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {
    private EditText etNombre, etCorreo, etContraseña, etCelular;
    private Button btnRegistrar;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.editTextName);
        etCorreo = findViewById(R.id.editTextCorreo);
        etContraseña = findViewById(R.id.editTextContrasena);
        etCelular = findViewById(R.id.editTextPhone);
        btnRegistrar = findViewById(R.id.buttonRegistrarse);

        presenter = new RegisterPresenter(this, this);

        btnRegistrar.setOnClickListener(v -> presenter.register(
                etNombre.getText().toString(),
                etCorreo.getText().toString(),
                etContraseña.getText().toString(),
                etCelular.getText().toString()
        ));
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        finish(); // O ir a login
    }

    @Override
    public void showRegisterError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

