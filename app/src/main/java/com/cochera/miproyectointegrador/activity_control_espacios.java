package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Espacio;

import java.util.List;

public class activity_control_espacios extends AppCompatActivity {

    private GridLayout gridLayout;
    private DBHelper dbHelper;
    private List<Espacio> listaEspacios;
    private Button btnContinuar;

    private Espacio espacioSeleccionado = null;
    private int usuarioId;
    private int estacionamientoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activa EdgeToEdge si usas esa librería (asegúrate de tenerla)
        // EdgeToEdge.enable(activity_control_espacios.this); // Comenta si da error

        setContentView(R.layout.activity_control_espacios);

        // Obtener datos del Intent
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        estacionamientoId = getIntent().getIntExtra("estacionamientoid", -1);

        // Ajustar padding para sistema de barras (status, navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a vistas
        gridLayout = findViewById(R.id.gridEspacios);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setEnabled(false); // Deshabilitado al inicio

        dbHelper = new DBHelper(this);

        if (estacionamientoId != -1) {
            listaEspacios = dbHelper.obtenerEspaciosPorEstacionamiento(estacionamientoId);
            llenarGridLayout(listaEspacios);
        } else {
            Toast.makeText(this, "Error al recibir el ID del estacionamiento", Toast.LENGTH_SHORT).show();
        }

        // Evento botón continuar
        btnContinuar.setOnClickListener(v -> {
            if (espacioSeleccionado != null) {
                Intent intent = new Intent(activity_control_espacios.this, Activity_reservas.class);
                intent.putExtra("espacioid", espacioSeleccionado.getEspacioId());
                intent.putExtra("estacionamientoid", espacioSeleccionado.getEstacionamientoId());
                intent.putExtra("codigo", espacioSeleccionado.getCodigo());
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            } else {
                Toast.makeText(activity_control_espacios.this, "Selecciona un espacio primero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarGridLayout(List<Espacio> espacios) {
        gridLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Espacio espacio : espacios) {
            View espacioView = inflater.inflate(R.layout.item_espacio, gridLayout, false);

            TextView tvCodigo = espacioView.findViewById(R.id.tvCodigo);
            tvCodigo.setText(espacio.getCodigo());

            // Opcional: cambiar color según estado
            /*
            if (espacio.isOcupado()) {
                tvCodigo.setBackgroundColor(Color.RED);
            } else {
                tvCodigo.setBackgroundColor(Color.GREEN);
            }
            */

            espacioView.setOnClickListener(v -> {
                espacioSeleccionado = espacio;
                btnContinuar.setEnabled(true);
                Toast.makeText(activity_control_espacios.this,
                        "Seleccionado: " + espacio.getCodigo(), Toast.LENGTH_SHORT).show();

                // Resaltar el espacio seleccionado
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    View child = gridLayout.getChildAt(i);
                    child.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                espacioView.setBackgroundColor(getResources().getColor(R.color.purple_200)); // Cambia por color deseado
            });

            gridLayout.addView(espacioView);
        }
    }
}
