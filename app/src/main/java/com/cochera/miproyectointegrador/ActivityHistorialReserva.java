package com.cochera.miproyectointegrador;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistorialReserva extends AppCompatActivity {

    private RecyclerView recyclerReservas;
    private ReservaAdapter reservaAdapter;
    private List<Reserva> reservaList;
    private DBHelper dbHelper;
    private ImageButton btnHome; // Botón para regresar

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_reserva);

        // Inicializar RecyclerView
        recyclerReservas = findViewById(R.id.recyclerReservas);
        recyclerReservas.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar botón Home y asignar listener para regresar
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> finish());

        // Inicializar base de datos y lista
        dbHelper = new DBHelper(this);
        reservaList = new ArrayList<>();

        try {
            cargarReservasDesdeBD();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar reservas: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Configurar adapter
        reservaAdapter = new ReservaAdapter(this, reservaList);
        recyclerReservas.setAdapter(reservaAdapter);
    }

    private void cargarReservasDesdeBD() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT reservaid, usuarioId, espacioId, vehiculoId, " +
                "nombreUsuario, apellidoUsuario, placa, tipoVehiculo, fecha, " +
                "horaEntrada, horaSalida, pagoHora, pago, ubicacion, estado " +
                "FROM reservas ORDER BY fecha DESC, horaEntrada DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Reserva reserva = new Reserva();
                reserva.setReservaid(cursor.getInt(cursor.getColumnIndexOrThrow("reservaid")));
                reserva.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuarioId")));
                reserva.setEspacioId(cursor.getInt(cursor.getColumnIndexOrThrow("espacioId")));
                reserva.setVehiculoId(cursor.getInt(cursor.getColumnIndexOrThrow("vehiculoId")));
                reserva.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow("nombreUsuario")));
                reserva.setApellidoUsuario(cursor.getString(cursor.getColumnIndexOrThrow("apellidoUsuario")));
                reserva.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow("placa")));
                reserva.setTipoVehiculo(cursor.getString(cursor.getColumnIndexOrThrow("tipoVehiculo")));
                reserva.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha")));
                reserva.setHoraEntrada(cursor.getString(cursor.getColumnIndexOrThrow("horaEntrada")));
                reserva.setHoraSalida(cursor.getString(cursor.getColumnIndexOrThrow("horaSalida")));
                reserva.setPagoHora(cursor.getDouble(cursor.getColumnIndexOrThrow("pagoHora")));
                reserva.setPago(cursor.getDouble(cursor.getColumnIndexOrThrow("pago")));
                reserva.setUbicacion(cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")));
                reserva.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));

                reservaList.add(reserva);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No hay reservas registradas.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }
}
