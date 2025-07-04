package com.cochera.miproyectointegrador;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


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

        inicializarComponentes();
        recibirDatosIntent();
        cargarTiposVehiculo();

        // Listeners
        etFecha.setOnClickListener(v -> mostrarDatePicker());
        etHoraEntrada.setOnClickListener(v -> mostrarTimePicker(etHoraEntrada));
        etHoraSalida.setOnClickListener(v -> mostrarTimePicker(etHoraSalida));
        btnReservar.setOnClickListener(v -> guardarReserva());
    }

    private void inicializarComponentes() {
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
    }

    private void recibirDatosIntent() {
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        estacionamientoId = getIntent().getIntExtra("estacionamientoId", -1);
        Toast.makeText(this, String.valueOf(estacionamientoId), Toast.LENGTH_SHORT).show();
        espacioId = getIntent().getIntExtra("espacioid", -1);

        if (estacionamientoId != -1) {
            String nombre = dbHelper.obtenerNombreEstacionamientoPorId(estacionamientoId);
            tvNombreEstacionamiento.setText(nombre != null ? nombre : "Estacionamiento");
        }
    }

    private void cargarTiposVehiculo() {
        tarifas = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);

        if (tarifas == null || tarifas.isEmpty()) {
            Toast.makeText(this, "No hay tarifas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> tipos = new ArrayList<>();
        for (Tarifa t : tarifas) {
            tipos.add(t.getTipoVehiculo());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
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

        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y);
            etFecha.setText(fecha);
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(hoy.getTimeInMillis());
        dialog.getDatePicker().setMaxDate(max.getTimeInMillis());
        dialog.show();
    }

    private void mostrarTimePicker(EditText campoHora) {
        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = (cal.get(Calendar.MINUTE) / 15) * 15;

        new TimePickerDialog(this, (view, h, m) -> {
            m = (m / 15) * 15;
            String ampm = h < 12 ? "AM" : "PM";
            int hora12 = h % 12 == 0 ? 12 : h % 12;
            String horaFormateada = String.format(Locale.getDefault(), "%02d:%02d %s", hora12, m, ampm);
            campoHora.setText(horaFormateada);
            calcularHorasYPrecio();
        }, hora, minuto, false).show();
    }

    private int convertirHoraAMinutos(String hora) {
        try {
            String[] partes = hora.split(" ");
            String[] hm = partes[0].split(":");
            int h = Integer.parseInt(hm[0]);
            int m = Integer.parseInt(hm[1]);

            if (partes[1].equalsIgnoreCase("PM") && h != 12) h += 12;
            if (partes[1].equalsIgnoreCase("AM") && h == 12) h = 0;

            return h * 60 + m;
        } catch (Exception e) {
            return -1;
        }
    }

    private void calcularHorasYPrecio() {
        int minEntrada = convertirHoraAMinutos(etHoraEntrada.getText().toString().trim());
        int minSalida = convertirHoraAMinutos(etHoraSalida.getText().toString().trim());

        if (minEntrada == -1 || minSalida == -1) return;

        int diferencia = minSalida - minEntrada;
        if (diferencia <= 0) diferencia += 24 * 60;

        double horas = diferencia / 60.0;
        etHoras.setText(String.format(Locale.getDefault(), "%.2f", horas));
        calcularPrecio(horas);
    }

    private void calcularPrecio(double horas) {
        if (tarifaSeleccionada == null) return;
        double precio = tarifaSeleccionada.getPrecio() * horas;
        etPrecio.setText(String.format(Locale.getDefault(), "%.2f", precio));
    }

    private void guardarReserva() {
        String fecha = etFecha.getText().toString().trim();
        String entrada = etHoraEntrada.getText().toString().trim();
        String salida = etHoraSalida.getText().toString().trim();
        String placa = etPlaca.getText().toString().trim().toUpperCase(Locale.getDefault());

        if (fecha.isEmpty() || entrada.isEmpty() || salida.isEmpty() || placa.isEmpty() || tarifaSeleccionada == null) {
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

        // Insertar en la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("usuarioid", usuarioId);
        valores.put("espacioid", espacioId);
        valores.put("vehiculoid", tarifaSeleccionada.getTarifaid());
        valores.put("fechareserva", fecha);
        valores.put("horaentrada", entrada);
        valores.put("horasalida", salida);
        valores.put("pago", etPrecio.getText().toString().trim());
        valores.put("estado", "Pendiente");
        valores.put("placa", placa);

        long id = db.insert("Reservas", null, valores);
        db.close();

        if (id != -1) {
            Toast.makeText(this, "Reserva guardada correctamente", Toast.LENGTH_LONG).show();

            // ✅ Subir a Firebase
            subirReservaAFirebase(id, usuarioId, espacioId, tarifaSeleccionada.getTarifaid(), fecha, entrada, salida, etPrecio.getText().toString().trim(), "Pendiente", placa);
            Intent intent = new Intent(this, Activity_estacionamientos.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_LONG).show();
        }
    }
    private void subirReservaAFirebase(long idReserva, int usuarioId, int espacioId, int vehiculoId, String fecha, String entrada, String salida, String pago, String estado, String placa) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> reservaMap = new HashMap<>();
        reservaMap.put("usuarioid", usuarioId);
        reservaMap.put("espacioid", espacioId);
        reservaMap.put("vehiculoid", vehiculoId);
        reservaMap.put("fechareserva", fecha);
        reservaMap.put("horaentrada", entrada);
        reservaMap.put("horasalida", salida);
        reservaMap.put("pago", pago);
        reservaMap.put("estado", estado);
        reservaMap.put("placa", placa);

        // Convertimos el ID numérico en String para Firestore
        String idFirestore = String.valueOf(idReserva);

        firestore.collection("reservas").document(idFirestore)
                .set(reservaMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Reserva también subida a Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al subir a Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}
