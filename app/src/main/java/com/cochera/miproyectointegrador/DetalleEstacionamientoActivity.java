package com.cochera.miproyectointegrador;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Tarifa; // Si tienes la clase en otro paquete, ajusta el import

import java.util.List;

public class DetalleEstacionamientoActivity extends AppCompatActivity {
    private TextView tvTipoVehiculo, tvTarifa, tvEstado;
    private DBHelper dbHelper;
    private int estacionamientoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estacionamiento);

        tvTipoVehiculo = findViewById(R.id.tvTipoVehiculo);
        tvTarifa = findViewById(R.id.tvTarifa);
        tvEstado = findViewById(R.id.tvEstado);
        dbHelper = new DBHelper(this);

        estacionamientoId = getIntent().getIntExtra("estacionamientoId", -1);

        if (estacionamientoId != -1) {
            // Suponiendo que tienes un método como este:
            List<Tarifa> tarifas = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);
            if (!tarifas.isEmpty()) {
                Tarifa tarifa = tarifas.get(0); // ejemplo: toma el primero
                tvTipoVehiculo.setText("Tipo: " + tarifa.getTipoVehiculo());
                tvTarifa.setText("Tarifa: S/ " + tarifa.getPrecio());
                tvEstado.setText("Estado: Disponible"); // cambia esto si tienes lógica de estado
            }
        }
    }
}
