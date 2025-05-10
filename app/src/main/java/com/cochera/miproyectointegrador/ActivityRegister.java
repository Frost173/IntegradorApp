package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cochera.miproyectointegrador.Register.RegisterContract;
import com.cochera.miproyectointegrador.Register.RegisterPresenter;

public class ActivityRegister extends AppCompatActivity implements RegisterContract.View  {


    private EditText etNombre, etCorreo, etContrasena, etCelular;

    private Button btnRegistrar;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.editTextName);
        etCorreo = findViewById(R.id.editTextCorreo);
        etContrasena = findViewById(R.id.editTextContrasena);
        etCelular = findViewById(R.id.editTextPhone);
        btnRegistrar = findViewById(R.id.buttonRegistrarse);

        presenter = new RegisterPresenter(this, this);

        btnRegistrar.setOnClickListener(v -> presenter.register(
                etNombre.getText().toString(),
                etCorreo.getText().toString(),
                etContrasena.getText().toString(),
                etCelular.getText().toString()
        ));

        Button btnIrLogin = findViewById(R.id.buttonInicioSesion); // Asegúrate de tener este botón en tu layout

        btnIrLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityRegister.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class); // Redirige al login
        startActivity(intent);
        finish(); // Opcional: evita volver al registro con el botón atrás
    }


    @Override
    public void showRegisterError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}