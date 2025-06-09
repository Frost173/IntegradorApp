package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class TarifarioActivity extends AppCompatActivity {

    Spinner spinnerTipoVehiculo;
    EditText editTextPrecio;
    Button btnReservar;

    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas); // Asegúrate de que sea el layout correcto

        spinnerTipoVehiculo = findViewById(R.id.spTipoVehiculo);
        editTextPrecio = findViewById(R.id.etTarifa);
        btnReservar = findViewById(R.id.btnReservar);

        dbHelper = new DBHelper(this);

        int estacionamientoId = getIntent().getIntExtra("estacionamientoId", -1);
        String[] tiposVehiculo = dbHelper.obtenerTiposVehiculoPorEstacionamiento(estacionamientoId).toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposVehiculo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoVehiculo.setAdapter(adapter);

        spinnerTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoSeleccionado = parent.getItemAtPosition(position).toString();
                double tarifa = tipoSeleccionado.equals("Carro") ? 10.0
                        : tipoSeleccionado.equals("Moto") ? 5.0
                        : 15.0;
                editTextPrecio.setText(String.valueOf(tarifa));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editTextPrecio.setText("");
            }
        });

        btnReservar.setOnClickListener(v -> {
            String tipo = spinnerTipoVehiculo.getSelectedItem().toString();
            double precio = Double.parseDouble(editTextPrecio.getText().toString());

            // Suponiendo que el estacionamientoId es 1 (ajusta si es necesario)
            boolean insertado = dbHelper.insertarTarifa(1, tipo, precio);
            if (insertado) {
                Toast.makeText(this, "Tarifa guardada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar tarifa", Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(TarifarioActivity.this, Activity_estacionamientos.class));
        });
    }
}

