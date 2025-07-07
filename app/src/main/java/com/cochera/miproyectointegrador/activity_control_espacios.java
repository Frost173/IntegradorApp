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

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Espacio;

import java.util.List;

public class activity_control_espacios extends AppCompatActivity {

    private GridLayout gridLayout;
    private DBHelper dbHelper;
    private List<Espacio> listaEspacios;
    private Button btnContinuar;

    private Espacio espacioSeleccionado = null;
    private int usuarioId, estacionamientoId;
    private String fecha, horaEntrada, horaSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_espacios);

        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        estacionamientoId = getIntent().getIntExtra("estacionamientoId", -1);
        fecha = getIntent().getStringExtra("fecha");
        horaEntrada = getIntent().getStringExtra("horaEntrada");
        horaSalida = getIntent().getStringExtra("horaSalida");

        gridLayout = findViewById(R.id.gridEspacios);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setEnabled(false);

        dbHelper = new DBHelper(this);

        if (estacionamientoId != -1) {
            listaEspacios = dbHelper.obtenerEspaciosPorEstacionamiento(estacionamientoId);
            llenarGridLayout(listaEspacios);
        } else {
            Toast.makeText(this, "Error: estacionamientoId inválido", Toast.LENGTH_SHORT).show();
        }

        btnContinuar.setOnClickListener(v -> {
            if (espacioSeleccionado != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("espacioid", espacioSeleccionado.getEspacioId());
                resultIntent.putExtra("codigo", espacioSeleccionado.getCodigo());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Selecciona un espacio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarGridLayout(List<Espacio> espacios) {
        gridLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Espacio espacio : espacios) {
            View espacioView = inflater.inflate(R.layout.item_espacio, gridLayout, false);
            TextView tvCodigo = espacioView.findViewById(R.id.tvCodigo);
            TextView tvEstado = espacioView.findViewById(R.id.tvEstado);

            tvCodigo.setText(espacio.getUbicacion());

            boolean ocupado = dbHelper.existeReservaEnEspacio(
                    espacio.getEspacioId(),
                    estacionamientoId,
                    fecha,
                    horaEntrada,
                    horaSalida
            );

            if (ocupado) {
                espacioView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                tvEstado.setText("Ocupado");
                espacioView.setEnabled(false);
            } else {
                tvEstado.setText("Disponible");

                espacioView.setOnClickListener(v -> {
                    espacioSeleccionado = espacio;
                    btnContinuar.setEnabled(true);

                    Toast.makeText(this, "Seleccionado: " + espacio.getUbicacion(), Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        View child = gridLayout.getChildAt(i);
                        child.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    espacioView.setBackgroundColor(getResources().getColor(R.color.purple_200));
                });
            }

            gridLayout.addView(espacioView);
        }
    }
}
