package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.cochera.miproyectointegrador.Register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etClave;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.editTextCorreolog);
        etClave = findViewById(R.id.editTextContrasenalog);
        btnLogin = findViewById(R.id.buttonLogin);
        dbHelper = new DBHelper(this);

        //dbHelper.insertarUsuario("cliente", "cliente","cliente@gmail.com", "1234",2);
        //dbHelper.insertarEstacionamiento("Surco Local","Surco",1);
        //dbHelper.insertarEspacio(1,"LV005","Disponible");
        //dbHelper.insertarEspacio(1,"LV006","Disponible");
        //dbHelper.insertarEspacio(1,"LV007","Disponible");
        //dbHelper.insertarTarifas(1,"Convertible","13.00");
        //dbHelper.insertarTarifas(1,"Station Wagon","10.00");
        //dbHelper.insertarTarifas(1,"Camioneta","15.00");
        //dbHelper.insertarPerfil("Cliente");
        // OPCIONAL: insertar un usuario de prueba
        // dbHelper.insertarUsuario("Juan", "juan@correo.com", "1234");
        //dbHelper.insertarVehiculo(2, "NSHU76", "Camioneta","Verde");
        //dbHelper.insertarReserva(2, 1,1, "14/05/2025","14:00","16:00","Pendiente");
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

                    if (usuario != null) {
                        Toast.makeText(LoginActivity.this, "Bienvenido, " + usuario.getNombre(), Toast.LENGTH_SHORT).show();

                        // Redirección según perfil
                        switch (usuario.getPerfil()) {
                            case "Administrador":
                                startActivity(new Intent(LoginActivity.this, ActivityAdminint.class));
                                break;
                            case "Cliente":
                                startActivity(new Intent(LoginActivity.this, Activity_estacionamientos.class));
                                break;

                            default:
                                Toast.makeText(LoginActivity.this, "Perfil no reconocido", Toast.LENGTH_SHORT).show();
                        }

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
