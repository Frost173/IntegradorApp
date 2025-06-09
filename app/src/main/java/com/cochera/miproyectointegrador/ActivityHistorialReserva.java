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

        // Inicializar RecyclerView y LayoutManager
        recyclerReservas = findViewById(R.id.recyclerReservas);
        recyclerReservas.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar botón Home y asignar listener para regresar
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> finish());

        // Inicializar base de datos y lista de reservas
        dbHelper = new DBHelper(this);
        reservaList = new ArrayList<>();

        // Configurar adapter con la lista vacía inicialmente
        reservaAdapter = new ReservaAdapter(this, reservaList);
        recyclerReservas.setAdapter(reservaAdapter);

        // Cargar reservas desde la base de datos
        cargarReservasDesdeBD();
    }

    private void cargarReservasDesdeBD() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta solo con columnas que existen en la tabla Reservas
        String query = "SELECT reservaid, usuarioid, espacioid, vehiculoid, fechareserva, horaentrada, horasalida, estado, placa, pago, ubicacion " +
                "FROM Reservas ORDER BY fechareserva DESC, horaentrada DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            reservaList.clear(); // Limpiar lista antes de agregar nuevos datos

            do {
                Reserva reserva = new Reserva();
                reserva.setReservaid(cursor.getInt(cursor.getColumnIndexOrThrow("reservaid")));
                reserva.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuarioid")));
                reserva.setEspacioId(cursor.getInt(cursor.getColumnIndexOrThrow("espacioid")));
                reserva.setVehiculoId(cursor.getInt(cursor.getColumnIndexOrThrow("vehiculoid")));
                reserva.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fechareserva")));
                reserva.setHoraEntrada(cursor.getString(cursor.getColumnIndexOrThrow("horaentrada")));
                reserva.setHoraSalida(cursor.getString(cursor.getColumnIndexOrThrow("horasalida")));
                reserva.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                reserva.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow("placa")));
                reserva.setPago(cursor.getDouble(cursor.getColumnIndexOrThrow("pago")));
                reserva.setUbicacion(cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")));

                reservaList.add(reserva);
            } while (cursor.moveToNext());

            // Notificar al adapter que los datos cambiaron para refrescar la vista
            reservaAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No hay reservas registradas.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
