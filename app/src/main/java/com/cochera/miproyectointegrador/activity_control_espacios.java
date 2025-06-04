package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Espacio;

import java.util.List;



public class activity_control_espacios extends AppCompatActivity {

    GridLayout gridLayout;
    DBHelper dbHelper;
    List<Espacio> listaEspacios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(activity_control_espacios.this);
        setContentView(R.layout.activity_control_espacios);

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Ajuste de padding para EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Obtener referencia al GridLayout
        gridLayout = findViewById(R.id.gridEspacios);
        dbHelper = new DBHelper(this);

        int estacionamientoId = getIntent().getIntExtra("estacionamientoid", -1);

        if (estacionamientoId != -1) {
            listaEspacios = dbHelper.obtenerEspaciosPorEstacionamiento(estacionamientoId);
            llenarGridLayout(listaEspacios,usuarioId);
        } else {
            Toast.makeText(this, "Error al recibir el ID del estacionamiento", Toast.LENGTH_SHORT).show();
        }
    }
    private void llenarGridLayout(List<Espacio> espacios, int usuarioId) {
        gridLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Espacio espacio : espacios) {
            // Inflar el layout del espacio
            View espacioView = inflater.inflate(R.layout.item_espacio, gridLayout, false);

            // Obtener el TextView y setear datos
            TextView tvCodigo = espacioView.findViewById(R.id.tvCodigo);
            tvCodigo.setText(espacio.getCodigo()); // Suponiendo que tienes getCodigo()

            // Cambiar color si está ocupado
//        if (espacio.isOcupado()) {
//            tvCodigo.setBackgroundColor(Color.RED);
//        } else {
//            tvCodigo.setBackgroundColor(Color.GREEN);
//        }

            // Agregar eventos si deseas
            espacioView.setOnClickListener(v -> {
                Toast.makeText(this, "Espacio " + espacio.getCodigo(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, Activity_reservas.class);
                intent.putExtra("espacioid", espacio.getEspacioId());
                intent.putExtra("estacionamientoid", espacio.getEstacionamientoId());
                intent.putExtra("codigo", espacio.getCodigo());
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            });

            // Agregar al GridLayout
            gridLayout.addView(espacioView);
        }
    }
}