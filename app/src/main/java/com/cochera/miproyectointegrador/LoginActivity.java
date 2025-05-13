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

import com.cochera.miproyectointegrador.Register.RegisterActivity;


public class LoginActivity extends AppCompatActivity implements LoginContract.View{
    /*
=======


public class LoginActivity extends AppCompatActivity implements LoginContract.View{
>>>>>>> 70cf38bf16dac277ecf77023b818e34e2e8818c4
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
<<<<<<< HEAD
    */
    //modifcacion testin

    private EditText etCorreo, etContrasena;
    private Button btnLogin, btnRegistrate;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.editTextCorreolog);
        etContrasena = findViewById(R.id.editTextContrasenalog);
        btnLogin = findViewById(R.id.buttonLogin);
        btnRegistrate = findViewById(R.id.buttonRegistrolog);

        presenter = new LoginPresenter(this, this);

        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();
            presenter.login(correo, contrasena);
        });

        btnRegistrate.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void goToAdminInterface() {
        startActivity(new Intent(this, Tarifario.class));
        finish();
    }

    @Override
    public void goToClienteInterface() {
        startActivity(new Intent(this, InterfazCliente.class));
    }



    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}