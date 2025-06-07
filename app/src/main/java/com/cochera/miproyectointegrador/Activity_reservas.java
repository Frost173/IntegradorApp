package com.cochera.miproyectointegrador;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Tarifa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Activity_reservas extends AppCompatActivity {

    private DBHelper dbHelper;

    private TextView tvNombreEstacionamiento;
    private EditText etFecha, etHoraEntrada, etHoraSalida, etTarifa, etHoras, etPrecio, etPlaca;
    private Spinner spTipoVehiculo;
    private Button btnReservar;

    private int usuarioId, estacionamientoId, espacioId;
    private List<Tarifa> tarifas;
    private Tarifa tarifaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        dbHelper = new DBHelper(this);

        // Referencias UI
        tvNombreEstacionamiento = findViewById(R.id.tvNombreEstacionamiento);
        etFecha = findViewById(R.id.etFecha);
        etHoraEntrada = findViewById(R.id.etHoraEntrada);
        etHoraSalida = findViewById(R.id.etHoraSalida);
        etTarifa = findViewById(R.id.etTarifa);
        etHoras = findViewById(R.id.etHorasEstimadas);
        etPrecio = findViewById(R.id.etPrecio);
        etPlaca = findViewById(R.id.etPlaca);
        spTipoVehiculo = findViewById(R.id.spTipoVehiculo);
        btnReservar = findViewById(R.id.btnReservar);

        // Obtener datos del Intent
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        estacionamientoId = getIntent().getIntExtra("estacionamientoid", -1);
        espacioId = getIntent().getIntExtra("espacioid", -1);

        if (estacionamientoId != -1) {
            String nombre = dbHelper.obtenerNombreEstacionamientoPorId(estacionamientoId);
            tvNombreEstacionamiento.setText(nombre != null ? nombre : "Estacionamiento");
        }

        cargarTiposVehiculo();

        // Listeners para fecha y hora
        etFecha.setOnClickListener(v -> mostrarDatePicker());
        etHoraEntrada.setOnClickListener(v -> mostrarTimePicker(etHoraEntrada));
        etHoraSalida.setOnClickListener(v -> mostrarTimePicker(etHoraSalida));

        btnReservar.setOnClickListener(v -> guardarReserva());
    }

    private void cargarTiposVehiculo() {
        tarifas = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tarifaSeleccionada = tarifas.get(position);
                etTarifa.setText(String.format(Locale.getDefault(), "%.2f", tarifaSeleccionada.getPrecio()));
                calcularHorasYPrecio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tarifaSeleccionada = null;
                etTarifa.setText("");
                etPrecio.setText("");
            }
        });
    }

    private void mostrarDatePicker() {
        Calendar hoy = Calendar.getInstance();
        Calendar max = (Calendar) hoy.clone();
        max.add(Calendar.DAY_OF_YEAR, 7);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            etFecha.setText(fecha);
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(hoy.getTimeInMillis());
        dialog.getDatePicker().setMaxDate(max.getTimeInMillis());
        dialog.show();
    }

    private void mostrarTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = (calendar.get(Calendar.MINUTE) / 15) * 15;

        // Usar formato 12h para AM/PM
        TimePickerDialog dialog = new TimePickerDialog(this, (view, h, m) -> {
            m = (m / 15) * 15;
            String periodo = h < 12 ? "AM" : "PM";
            int hora12 = h % 12 == 0 ? 12 : h % 12;
            String textoHora = String.format(Locale.getDefault(), "%02d:%02d %s", hora12, m, periodo);
            editText.setText(textoHora);
            calcularHorasYPrecio();
        }, hora, minuto, false);

        dialog.show();
    }

    private int convertirHoraAMinutos(String hora) {
        try {
            String[] partes = hora.split(" ");
            String[] hm = partes[0].split(":");
            int h = Integer.parseInt(hm[0]);
            int m = Integer.parseInt(hm[1]);
            if (partes[1].equalsIgnoreCase("PM") && h < 12) h += 12;
            if (partes[1].equalsIgnoreCase("AM") && h == 12) h = 0;
            return h * 60 + m;
        } catch (Exception e) {
            return -1;
        }
    }

    private void calcularHorasYPrecio() {
        String entrada = etHoraEntrada.getText().toString().trim();
        String salida = etHoraSalida.getText().toString().trim();

        int minEntrada = convertirHoraAMinutos(entrada);
        int minSalida = convertirHoraAMinutos(salida);

        if (minEntrada == -1 || minSalida == -1) return;

        int diferencia = minSalida - minEntrada;
        if (diferencia <= 0) diferencia += 24 * 60;

        double horas = diferencia / 60.0;
        etHoras.setText(String.format(Locale.getDefault(), "%.2f", horas));
        calcularPrecio();
    }

    private void calcularPrecio() {
        if (tarifaSeleccionada == null) return;

        try {
            double tarifa = tarifaSeleccionada.getPrecio();
            double horas = Double.parseDouble(etHoras.getText().toString());
            double precio = tarifa * horas;
            etPrecio.setText(String.format(Locale.getDefault(), "%.2f", precio));
        } catch (Exception e) {
            etPrecio.setText("");
        }
    }

    private void guardarReserva() {
        String fecha = etFecha.getText().toString().trim();
        String entrada = etHoraEntrada.getText().toString().trim();
        String salida = etHoraSalida.getText().toString().trim();
        String placa = etPlaca.getText().toString().trim().toUpperCase(Locale.getDefault());

        if (fecha.isEmpty() || entrada.isEmpty() || salida.isEmpty() || tarifaSeleccionada == null || placa.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de placa
        boolean esMoto = tarifaSeleccionada.getTipoVehiculo().equalsIgnoreCase("Moto");
        String regex = esMoto ? "[A-Z]{3}-\\d{4}" : "[A-Z]{3}-\\d{3}";
        if (!placa.matches(regex)) {
            String ejemplo = esMoto ? "ABC-1234" : "ABC-123";
            Toast.makeText(this, "Formato de placa inválido. Ejemplo: " + ejemplo, Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("usuarioid", usuarioId);
        valores.put("espacioid", espacioId);
        valores.put("vehiculoid", tarifaSeleccionada.getIdTarifa());
        valores.put("fechareserva", fecha);
        valores.put("horaentrada", entrada);
        valores.put("horasalida", salida);
        valores.put("estado", "Pendiente");
        valores.put("placa", placa);

        long id = db.insert("Reservas", null, valores);
        db.close();

        if (id != -1) {
            Toast.makeText(this, "Reserva guardada", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Activity_estacionamientos.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_LONG).show();
        }
    }
}
