package com.cochera.miproyectointegrador;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.cochera.miproyectointegrador.DataBase.Reserva;


import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Tarifa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity_reservas extends AppCompatActivity {

    private DBHelper dbHelper;
    private int idTarifaSeleccionada = -1;

    private TextView tvNombreEstacionamiento;
    private EditText etFecha, etHoraEntrada, etHoraSalida, etTarifa, etHoras, etPrecio;
    private Spinner spTipoVehiculo;
    private Button btnReservar;

    private int usuarioId, estacionamientoId, espacioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        dbHelper = new DBHelper(this);

        // Referencias UI
        tvNombreEstacionamiento = findViewById(R.id.tvNombreEstacionamiento);
        etFecha = findViewById(R.id.etFecha);
        etHoraEntrada = findViewById(R.id.etHora);
        etHoraSalida = findViewById(R.id.etHoraSalida);
        etTarifa = findViewById(R.id.etTarifa);
        etHoras = findViewById(R.id.etColor);
        etPrecio = findViewById(R.id.etPrecio);
        spTipoVehiculo = findViewById(R.id.spTipoVehiculo);
        btnReservar = findViewById(R.id.btnReservar);

        // Obtener datos del Intent
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        estacionamientoId = getIntent().getIntExtra("estacionamientoid", -1);
        espacioId = getIntent().getIntExtra("espacioid", -1);

        // Consultar y mostrar el nombre del estacionamiento desde la base de datos
        if (estacionamientoId != -1) {
            String nombreEstacionamiento = dbHelper.obtenerNombreEstacionamientoPorId(estacionamientoId);
            if (nombreEstacionamiento != null) {
                tvNombreEstacionamiento.setText(nombreEstacionamiento);
            } else {
                tvNombreEstacionamiento.setText("Estacionamiento");
            }
        } else {
            tvNombreEstacionamiento.setText("Estacionamiento");
        }

        // Configurar Spinner con tipos de vehículo y tarifas
        cargarTiposVehiculo();

        // Configurar DatePicker para fecha
        etFecha.setOnClickListener(v -> mostrarDatePicker());

        // Configurar TimePickers para horas
        etHoraEntrada.setOnClickListener(v -> mostrarTimePicker(etHoraEntrada));
        etHoraSalida.setOnClickListener(v -> mostrarTimePicker(etHoraSalida));

        // Botón reservar
        btnReservar.setOnClickListener(v -> guardarReserva());
    }

    private void cargarTiposVehiculo() {
        List<Tarifa> tarifas = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);

        if (tarifas == null || tarifas.isEmpty()) {
            Toast.makeText(this, "No hay tarifas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> tiposVehiculo = new ArrayList<>();
        for (Tarifa t : tarifas) {
            tiposVehiculo.add(t.getTipoVehiculo());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposVehiculo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoVehiculo.setAdapter(adapter);

        spTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long id) {
                Tarifa tarifaSeleccionada = tarifas.get(position);
                idTarifaSeleccionada = tarifaSeleccionada.getIdTarifa();
                etTarifa.setText(String.format("%.2f", tarifaSeleccionada.getPrecio()));
                calcularPrecio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                idTarifaSeleccionada = -1;
                etTarifa.setText("");
                etPrecio.setText("");
            }
        });
    }


    private void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String fecha = String.format("%02d/%02d/%02d", dayOfMonth, month + 1, year % 100);
                    etFecha.setText(fecha);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void mostrarTimePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    String hora = String.format("%02d:%02d", hourOfDay, minute);
                    editText.setText(hora);
                    calcularHorasYPrecio();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void calcularHorasYPrecio() {
        String horaEntrada = etHoraEntrada.getText().toString();
        String horaSalida = etHoraSalida.getText().toString();

        if (!horaEntrada.isEmpty() && !horaSalida.isEmpty()) {
            String[] entrada = horaEntrada.split(":");
            String[] salida = horaSalida.split(":");

            int minutosEntrada = Integer.parseInt(entrada[0]) * 60 + Integer.parseInt(entrada[1]);
            int minutosSalida = Integer.parseInt(salida[0]) * 60 + Integer.parseInt(salida[1]);

            int diff = minutosSalida - minutosEntrada;
            if (diff < 0) diff += 24 * 60;

            double horas = diff / 60.0;
            etHoras.setText(String.format("%.2f", horas));
            calcularPrecio();
        }
    }

    private void calcularPrecio() {
        String tarifaStr = etTarifa.getText().toString();
        String horasStr = etHoras.getText().toString();

        if (!tarifaStr.isEmpty() && !horasStr.isEmpty()) {
            double tarifa = Double.parseDouble(tarifaStr);
            double horas = Double.parseDouble(horasStr);
            double precio = tarifa * horas;
            etPrecio.setText(String.format("%.2f", precio));
        }
    }

    private void guardarReserva() {
        String fecha = etFecha.getText().toString();
        String horaEntrada = etHoraEntrada.getText().toString();
        String horaSalida = etHoraSalida.getText().toString();

        if (fecha.isEmpty() || horaEntrada.isEmpty() || horaSalida.isEmpty() || idTarifaSeleccionada == -1) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuarioid", usuarioId);
        values.put("espacioid", espacioId);
        values.put("vehiculoid", idTarifaSeleccionada);
        values.put("fechareserva", fecha);
        values.put("horaentrada", horaEntrada);
        values.put("horasalida", horaSalida);
        values.put("estado", "Pendiente");

        long id = db.insert("Reservas", null, values);
        if (id != -1) {
            Toast.makeText(this, "Reserva registrada correctamente", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar la reserva", Toast.LENGTH_LONG).show();
        }
        db.close();
    }
}
