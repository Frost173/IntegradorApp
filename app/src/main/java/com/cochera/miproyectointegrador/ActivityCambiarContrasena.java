package com.cochera.miproyectointegrador;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class ActivityCambiarContrasena extends AppCompatActivity {

    EditText etContrasenaActual, etNuevaContrasena, etConfirmarContrasena;
    Button btnGuardar;
    ImageButton btnBack;
    DBHelper dbHelper;
    String usuarioActual; // Usuario actual de sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        dbHelper = new DBHelper(this);
        etContrasenaActual = findViewById(R.id.etContrasenaActual);
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnBack = findViewById(R.id.btnBack);

        // Recuperar usuario logueado de SharedPreferences
        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);
        usuarioActual = preferences.getString("usuario", "");

        // Botón para regresar atrás
        btnBack.setOnClickListener(v -> finish());

        // Desactivar botón guardar inicialmente
        btnGuardar.setEnabled(false);

        // TextWatcher para activar/desactivar botón según campos llenos
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String actual = etContrasenaActual.getText().toString().trim();
                String nueva = etNuevaContrasena.getText().toString().trim();
                String confirmar = etConfirmarContrasena.getText().toString().trim();
                btnGuardar.setEnabled(!actual.isEmpty() && !nueva.isEmpty() && !confirmar.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        etContrasenaActual.addTextChangedListener(textWatcher);
        etNuevaContrasena.addTextChangedListener(textWatcher);
        etConfirmarContrasena.addTextChangedListener(textWatcher);

        btnGuardar.setOnClickListener(v -> {
            String actual = etContrasenaActual.getText().toString();
            String nueva = etNuevaContrasena.getText().toString();
            String confirmar = etConfirmarContrasena.getText().toString();

            if (!dbHelper.validarUsuario(usuarioActual, actual)) {
                Toast.makeText(this, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!nueva.equals(confirmar)) {
                Toast.makeText(this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean actualizado = dbHelper.actualizarContrasena(usuarioActual, nueva);
            if (actualizado) {
                Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

