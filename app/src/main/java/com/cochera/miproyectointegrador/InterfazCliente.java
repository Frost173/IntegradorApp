package com.cochera.miproyectointegrador;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InterfazCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_cliente);

        int clienteId = getIntent().getIntExtra("clienteId", -1);
        String nombre = getIntent().getStringExtra("nombre");
        String correo = getIntent().getStringExtra("correo");


    }

}