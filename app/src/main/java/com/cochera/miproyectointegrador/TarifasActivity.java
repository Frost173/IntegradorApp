package com.cochera.miproyectointegrador;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class TarifasActivity extends AppCompatActivity {

    private Spinner spinnerTipoVehiculo;
    private EditText editTarifa;
    private Button btnGuardar;
    private TextView textResumen;

    private DBHelper dbHelper;

    private final String[] tiposVehiculo = {"Carro", "Moto", "Camión"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifas);

        // Inicialización de vistas
        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        editTarifa = findViewById(R.id.editTarifa);
        btnGuardar = findViewById(R.id.btnGuardar);
        textResumen = findViewById(R.id.textResumen);

        dbHelper = new DBHelper(this);

        // Configuración del Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposVehiculo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoVehiculo.setAdapter(adapter);

        // Evento al seleccionar tipo de vehículo
        spinnerTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipo = tiposVehiculo[position];
                double tarifa = dbHelper.obtenerTarifaPorTipo(tipo);
                editTarifa.setText(String.valueOf(tarifa));
                textResumen.setText(""); // Limpia resumen anterior
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });

        // Evento botón guardar
        btnGuardar.setOnClickListener(v -> guardarTarifa());
    }

    private void guardarTarifa() {
        String tipo = spinnerTipoVehiculo.getSelectedItem().toString();
        String tarifaStr = editTarifa.getText().toString().trim();

        if (TextUtils.isEmpty(tarifaStr)) {
            Toast.makeText(this, "Completa el campo de tarifa", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double tarifa = Double.parseDouble(tarifaStr);
            if (tarifa < 0) {
                Toast.makeText(this, "La tarifa debe ser un valor positivo", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.actualizarTarifa(tipo, tarifa);
            Toast.makeText(this, "Tarifa actualizada correctamente", Toast.LENGTH_SHORT).show();

            // Mostrar resumen actualizado
            String resumen = "✅ Tarifa actualizada:\n" + tipo + " → S/ " + tarifa;
            textResumen.setText(resumen);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese una tarifa válida", Toast.LENGTH_SHORT).show();
        }
    }
}
