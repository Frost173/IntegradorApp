package com.cochera.miproyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class TarifarioActivity extends AppCompatActivity {
//
//    Spinner spinnerTipoVehiculo;
//    EditText precioEditText, estadoEditText;
//    DBHelper dbHelper;
//    int estacionamientoId;
//    List<String> tiposVehiculo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tarifario);
//
//        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
////        precioEditText = findViewById(R.id.editTextText2);
////        estadoEditText = findViewById(R.id.editTextText3);
//
//        dbHelper = new DBHelper(this);
//        estacionamientoId = 1;
//
//        // Obtener tipos únicos de vehículo desde la BD
//        tiposVehiculo = dbHelper.obtenerTiposVehiculoPorEstacionamiento(estacionamientoId);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposVehiculo);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTipoVehiculo.setAdapter(adapter);
//
//        spinnerTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String tipoSeleccionado = tiposVehiculo.get(position);
//                double precio = dbHelper.obtenerPrecioPorTipoVehiculo(estacionamientoId, tipoSeleccionado);
//
////                if (precio > 0) {
////                    precioEditText.setText(String.valueOf(precio));
////                    estadoEditText.setText("Activo");
////                } else {
////                    precioEditText.setText("0");
////                    estadoEditText.setText("No disponible");
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Nada
//            }
//        });
//    }
//}

public class TarifarioActivity extends AppCompatActivity {

    Spinner spinnerTipoVehiculo;
    EditText editTextPrecio;

    // Mapa de precios por tipo de vehículo
    Map<String, Double> preciosVehiculos;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifario); // Asegúrate de que este sea el nombre correcto

        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        editTextPrecio = findViewById(R.id.editTextPrecio);
        Button btnReservar = findViewById(R.id.btnReservar);
        dbHelper = new DBHelper(this);

        // Simula tus tipos de vehículo y precios
        String[] tiposVehiculo = {"Carro", "Moto", "Camión"};
        preciosVehiculos = new HashMap<>();
        preciosVehiculos.put("Carro", 10.0);
        preciosVehiculos.put("Moto", 5.0);
        preciosVehiculos.put("Camión", 15.0);

        // Adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposVehiculo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoVehiculo.setAdapter(adapter);


        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertarReserva(2, 2,1, "14/05/2025","14:00","16:00","Pendiente");
                Intent intent = new Intent(TarifarioActivity.this, Activity_estacionamientos.class);
                startActivity(intent);
            }
        });

        // Muestra el precio cuando se selecciona un tipo
        spinnerTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoSeleccionado = parent.getItemAtPosition(position).toString();
                Double precio = preciosVehiculos.get(tipoSeleccionado);
                if (precio != null) {
                    editTextPrecio.setText(String.valueOf(precio));
                } else {
                    editTextPrecio.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editTextPrecio.setText("");
            }
        });
    }
}