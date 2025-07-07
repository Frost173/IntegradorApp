package com.cochera.miproyectointegrador;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Tarifa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class Activity_reservas extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView tvNombreEstacionamiento;
    private EditText etFecha, etHoraEntrada, etHoraSalida, etTarifa, etHoras, etPrecio, etPlaca;
    private Spinner spTipoVehiculo;
    private Button btnReservar, btnSeleccionarEspacio;

    private int usuarioId, estacionamientoId, espacioId;
    private String codigoEspacio;
    private List<Tarifa> tarifas;
    private Tarifa tarifaSeleccionada;

    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private String uidFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        dbHelper = new DBHelper(this);
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            uidFirebase = firebaseUser.getUid();
            cargarNombreUsuarioDesdeFirebase();
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            finish();
        }

        inicializarComponentes();
        recibirDatosIntent();
        cargarTiposVehiculo();

        etFecha.setOnClickListener(v -> mostrarDatePicker());
        etHoraEntrada.setOnClickListener(v -> mostrarTimePicker(etHoraEntrada));
        etHoraSalida.setOnClickListener(v -> mostrarTimePicker(etHoraSalida));
        btnReservar.setOnClickListener(v -> guardarReserva());

        btnSeleccionarEspacio.setOnClickListener(v -> {
            String fecha = etFecha.getText().toString().trim();
            String entrada = etHoraEntrada.getText().toString().trim();
            String salida = etHoraSalida.getText().toString().trim();
            String placa = etPlaca.getText().toString().trim().toUpperCase(Locale.getDefault());

            if (fecha.isEmpty() || entrada.isEmpty() || salida.isEmpty() || placa.isEmpty() || tarifaSeleccionada == null) {
                Toast.makeText(this, "Completa todos los campos antes de seleccionar un espacio", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(Activity_reservas.this, activity_control_espacios.class);
            intent.putExtra("usuarioId", usuarioId);
            intent.putExtra("estacionamientoId", estacionamientoId);
            intent.putExtra("fecha", fecha);
            intent.putExtra("horaEntrada", entrada);
            intent.putExtra("horaSalida", salida);
            intent.putExtra("placa", placa);
            intent.putExtra("tipoVehiculo", spTipoVehiculo.getSelectedItemPosition());
            startActivityForResult(intent, 1);
        });
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
        btnSeleccionarEspacio = findViewById(R.id.btnSeleccionarEspacio);
    }

    private void recibirDatosIntent() {
        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        estacionamientoId = getIntent().getIntExtra("estacionamientoId", -1);

        if (estacionamientoId != -1) {
            String nombre = dbHelper.obtenerNombreEstacionamientoPorId(estacionamientoId);
            tvNombreEstacionamiento.setText(nombre != null ? nombre : "Estacionamiento");
        }
    }

    private void cargarNombreUsuarioDesdeFirebase() {
        firestore.collection("usuarios").document(uidFirebase)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        tvNombreEstacionamiento.setText("Bienvenido, " + nombre);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error cargando nombre: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void cargarTiposVehiculo() {
        tarifas = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);

        if (tarifas == null || tarifas.isEmpty()) {
            Toast.makeText(this, "No hay tarifas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> tipos = new ArrayList<>();
        for (Tarifa t : tarifas) tipos.add(t.getTipoVehiculo());

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

        if (fecha.isEmpty() || entrada.isEmpty() || salida.isEmpty() || placa.isEmpty() || tarifaSeleccionada == null || espacioId == 0) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean existe = dbHelper.existeReservaEnEspacio(espacioId, estacionamientoId, fecha, entrada, salida);
        if (existe) {
            Toast.makeText(this, "❌ Ya existe una reserva para ese espacio en ese horario", Toast.LENGTH_LONG).show();
            return;
        }

        String ubicacion = estacionamientoId + codigoEspacio;

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
        valores.put("ubicacion", ubicacion);

        long id = db.insert("Reservas", null, valores);
        db.close();

        if (id != -1) {
            Toast.makeText(this, "✅ Reserva guardada correctamente", Toast.LENGTH_LONG).show();
            subirReservaAFirebase(id, usuarioId, espacioId, tarifaSeleccionada.getTarifaid(), fecha, entrada, salida, etPrecio.getText().toString().trim(), "Pendiente", placa, ubicacion);

            Intent intent = new Intent(this, Activity_estacionamientos.class);
            intent.putExtra("usuarioId", usuarioId);
            intent.putExtra("reservaExitosa", true);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "❌ Error al guardar la reserva", Toast.LENGTH_LONG).show();
        }
    }

    private void subirReservaAFirebase(long idReserva, int usuarioId, int espacioId, int vehiculoId, String fecha, String entrada, String salida, String pago, String estado, String placa, String ubicacion) {
        Map<String, Object> reservaMap = new HashMap<>();
        reservaMap.put("uid", uidFirebase);
        reservaMap.put("usuarioid", usuarioId);
        reservaMap.put("espacioid", espacioId);
        reservaMap.put("vehiculoid", vehiculoId);
        reservaMap.put("fechareserva", fecha);
        reservaMap.put("horaentrada", entrada);
        reservaMap.put("horasalida", salida);
        reservaMap.put("pago", pago);
        reservaMap.put("estado", estado);
        reservaMap.put("placa", placa);
        reservaMap.put("ubicacion", ubicacion);

        firestore.collection("reservas")
                .document(String.valueOf(idReserva))
                .set(reservaMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Reserva subida a Firebase", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al subir a Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            espacioId = data.getIntExtra("espacioid", 0);
            codigoEspacio = data.getStringExtra("codigo");
            Toast.makeText(this, "Espacio seleccionado: " + codigoEspacio, Toast.LENGTH_SHORT).show();
        }
    }
}
