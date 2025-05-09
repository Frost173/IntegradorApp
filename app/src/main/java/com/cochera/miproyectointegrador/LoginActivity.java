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

import com.cochera.miproyectointegrador.Login.LoginContract;
import com.cochera.miproyectointegrador.Login.LoginPresenter;


public class LoginActivity extends AppCompatActivity implements LoginContract.View{
    private EditText etCorreo, etContraseña;
    private Button btnLogin;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.editTextCorreolog);
        etContraseña = findViewById(R.id.editTextContrasenalog);
        btnLogin = findViewById(R.id.buttonLogin);

        presenter = new LoginPresenter(this, this);

        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String contraseña = etContraseña.getText().toString().trim();
            presenter.login(correo, contraseña);
        });
        Button btnRegistrate = findViewById(R.id.buttonRegistrolog); // Asegúrate de tener este botón en tu layout

        btnRegistrate.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ActivityRegister.class);
            startActivity(intent);
        });


    }

    @Override
    public void showLoginSuccess(int clienteId, String nombre, String correo) {
        Toast.makeText(this, "¡Bienvenido " + nombre + "!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, InterfazCliente.class);
        intent.putExtra("clienteId", clienteId);
        intent.putExtra("nombre", nombre);
        intent.putExtra("correo", correo);
        startActivity(intent);
        finish();
    }


    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}