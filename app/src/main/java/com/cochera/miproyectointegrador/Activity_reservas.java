package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;
import com.cochera.miproyectointegrador.DataBase.Tarifa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class Activity_reservas extends AppCompatActivity {

    private RecyclerView recyclerReservas;

    private int idTarifa;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas); // Asegúrate de que el XML se llame así

        // Inicializar RecyclerView
        recyclerReservas = findViewById(R.id.recyclerReservas);
        int estacionamientoId = getIntent().getIntExtra("estacionamientoid", -1);
        //Toast.makeText(this, "Espacio " + estacionamientoId, Toast.LENGTH_SHORT).show();
        EditText etFecha = findViewById(R.id.etFecha);
        Spinner spTipoVehiculo = findViewById(R.id.spTipoVehiculo);

//        Map<String, Double> tarifasMap = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);
//        List<String> tiposVehiculo = new ArrayList<>(tarifasMap.keySet());
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposVehiculo);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spTipoVehiculo.setAdapter(adapter);

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);

        // Dentro de onCreate
        EditText etHoraEntrada = findViewById(R.id.etHora);
        EditText etHoraSalida = findViewById(R.id.etHoraSalida);
        EditText etTarifa = findViewById(R.id.etTarifa);
        EditText etHoras = findViewById(R.id.etColor); // El campo "Horas"
        Button btnReservar = findViewById(R.id.btnReservar);
        EditText etPrecio = findViewById(R.id.etPrecio);



        List<Tarifa> tarifasList = dbHelper.obtenerTarifasPorEstacionamiento(estacionamientoId);
        List<String> tiposVehiculo = new ArrayList<>();
        for (Tarifa tarifa : tarifasList) {
            tiposVehiculo.add(tarifa.getTipoVehiculo());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposVehiculo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoVehiculo.setAdapter(adapter);

// Variable para guardar el idTarifa seleccionado
        final int[] idTarifaSeleccionada = {-1};

        spTipoVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tarifa tarifaSeleccionada = tarifasList.get(position);

                etTarifa.setText(String.format("%.2f", tarifaSeleccionada.getPrecio()));
                calcularPrecio(etTarifa, etHoras, etPrecio);

                // Guarda el idTarifa en la variable
                idTarifaSeleccionada[0] = tarifaSeleccionada.getIdTarifa();
                idTarifa = idTarifaSeleccionada[0];
                Toast.makeText(Activity_reservas.this, "Tarifa Id, " + idTarifa, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada
            }
        });

        etHoraEntrada.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                String horaFormateada = String.format("%02d:%02d", selectedHour, selectedMinute);
                etHoraEntrada.setText(horaFormateada);

                // Calcular horas automáticamente
                calcularHoras(etHoraEntrada, etHoraSalida, etHoras);
                calcularPrecio(etTarifa, etHoras, etPrecio);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        etHoraSalida.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                String horaFormateada = String.format("%02d:%02d", selectedHour, selectedMinute);
                etHoraSalida.setText(horaFormateada);

                // Calcular horas automáticamente
                calcularHoras(etHoraEntrada, etHoraSalida, etHoras);
                calcularPrecio(etTarifa, etHoras, etPrecio);
            }, hour, minute, true);
            timePickerDialog.show();
        });

// Botón Reservar
        btnReservar.setOnClickListener(v -> {
            String fechaReserva = etFecha.getText().toString();
            String horaEntrada = etHoraEntrada.getText().toString();
            String horaSalida = etHoraSalida.getText().toString();

            if (fechaReserva.isEmpty() || horaEntrada.isEmpty() || horaSalida.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Valores por defecto
            //int usuarioId = getIntent().getIntExtra("usuarioId", -1); // 🚨 Cambia esto según la lógica de tu app
            int espacioId = getIntent().getIntExtra("espacioid", -1);
            //int vehiculoId = idTarifa; // 🚨 Cambia esto según la lógica de tu app
            String estado = "Pendiente";

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("usuarioid", usuarioId);
            values.put("espacioid", espacioId);
            values.put("vehiculoid", idTarifa);
            values.put("fechareserva", fechaReserva);
            values.put("horaentrada", horaEntrada);
            values.put("horasalida", horaSalida);
            values.put("estado", estado);

            long resultado = db.insert("Reservas", null, values);
            if (resultado != -1) {
                Toast.makeText(this, "Reserva registrada correctamente", Toast.LENGTH_SHORT).show();
                // Opcional: Limpia los campos o regresa a la actividad anterior
                finish(); // Regresar, por ejemplo
            } else {
                Toast.makeText(this, "Error al registrar la reserva", Toast.LENGTH_SHORT).show();
            }

            db.close();
        });

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Activity_reservas.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Formatear la fecha seleccionada
                            String fechaSeleccionada = String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear + 1, year1 % 100);
                            etFecha.setText(fechaSeleccionada);
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
        // Aquí podrías agregar un Adapter más adelante
        // recyclerReservas.setLayoutManager(new LinearLayoutManager(this));
        // recyclerReservas.setAdapter(new TuAdaptador(listaDeDatos));
    }

    private void calcularHoras(EditText etHoraEntrada, EditText etHoraSalida, EditText etHoras) {
        String horaEntradaStr = etHoraEntrada.getText().toString();
        String horaSalidaStr = etHoraSalida.getText().toString();

        if (!horaEntradaStr.isEmpty() && !horaSalidaStr.isEmpty()) {
            String[] horaEntradaParts = horaEntradaStr.split(":");
            String[] horaSalidaParts = horaSalidaStr.split(":");

            int entradaMinutos = Integer.parseInt(horaEntradaParts[0]) * 60 + Integer.parseInt(horaEntradaParts[1]);
            int salidaMinutos = Integer.parseInt(horaSalidaParts[0]) * 60 + Integer.parseInt(horaSalidaParts[1]);

            int diferenciaMinutos = salidaMinutos - entradaMinutos;

            if (diferenciaMinutos < 0) {
                // Asumimos que la salida es el día siguiente
                diferenciaMinutos += 24 * 60;
            }

            double horas = diferenciaMinutos / 60.0;
            etHoras.setText(String.format("%.2f", horas));
        } else {
            etHoras.setText(""); // Limpia si faltan datos
        }
    }

    private void calcularPrecio(EditText etTarifa, EditText etHoras, EditText etPrecio) {
        String tarifaStr = etTarifa.getText().toString();
        String horasStr = etHoras.getText().toString();

        if (!tarifaStr.isEmpty() && !horasStr.isEmpty()) {
            double tarifa = Double.parseDouble(tarifaStr);
            double horas = Double.parseDouble(horasStr);

            double precio = tarifa * horas;
            etPrecio.setText(String.format("%.2f", precio));
        } else {
            etPrecio.setText("");
        }
    }
}