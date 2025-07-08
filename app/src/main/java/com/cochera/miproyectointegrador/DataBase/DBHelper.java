package com.cochera.miproyectointegrador.DataBase;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Clase DBHelper para manejar la base de datos SQLite.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "tu_basedatos_2.db", null, 26);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Perfiles (" +
                "perfilid INTEGER PRIMARY KEY," +
                "nombreperfil TEXT NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS Usuarios (" +
                "usuarioid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uid TEXT, " +
                "perfil TEXT, " +
                "nombre TEXT NOT NULL, " +
                "apellido TEXT NOT NULL, " +
                "correo TEXT UNIQUE NOT NULL, " +
                "contrasena TEXT NOT NULL, " +
                "celular TEXT, " +
                "estado TEXT, " +
                "edad INTEGER);");



        db.execSQL("CREATE TABLE Estacionamientos (" +
                "estacionamientoid INTEGER PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "direccion TEXT NOT NULL," +
                "propietarioid INTEGER," +
                "FOREIGN KEY (propietarioid) REFERENCES Usuarios(usuarioid));");

        db.execSQL("CREATE TABLE Espacios (" +
                "espacioid INTEGER PRIMARY KEY," +
                "estacionamientoid INTEGER," +
                "codigo TEXT NOT NULL," +
                "estado TEXT CHECK(estado IN ('Disponible', 'Ocupado', 'Reservado')) NOT NULL," +
                "ubicacion TEXT," +
                "FOREIGN KEY (estacionamientoid) REFERENCES Estacionamientos(estacionamientoid));");

        db.execSQL("CREATE TABLE Vehiculos (" +
                "vehiculoid INTEGER PRIMARY KEY," +
                "usuarioid INTEGER," +
                "placa TEXT UNIQUE NOT NULL," +
                "tipo TEXT," +
                "color TEXT," +
                "FOREIGN KEY (usuarioid) REFERENCES Usuarios(usuarioid));");

        db.execSQL("CREATE TABLE Reservas (" +
                "reservaid INTEGER PRIMARY KEY," +
                "usuarioid INTEGER," +
                "espacioid INTEGER," +
                "vehiculoid INTEGER," +
                "fechareserva DATE," +
                "horaentrada TEXT," +
                "horasalida TEXT," +
                "estado TEXT CHECK(estado IN ('Pendiente', 'Confirmada', 'Cancelada', 'Finalizada')) NOT NULL," +
                "placa TEXT," +
                "pago REAL DEFAULT 0," +
                "ubicacion TEXT," +
                "estacionamientoid INTEGER," +
                "FOREIGN KEY (usuarioid) REFERENCES Usuarios(usuarioid)," +
                "FOREIGN KEY (espacioid) REFERENCES Espacios(espacioid)," +
                "FOREIGN KEY (vehiculoid) REFERENCES Vehiculos(vehiculoid)," +
                "FOREIGN KEY (estacionamientoid) REFERENCES Estacionamientos(estacionamientoid));"); // Opcional


        db.execSQL("CREATE TABLE Pagos (" +
                "pagoid INTEGER PRIMARY KEY," +
                "reservaid INTEGER," +
                "monto REAL NOT NULL," +
                "fechapago DATE NOT NULL," +
                "FOREIGN KEY (reservaid) REFERENCES Reservas(reservaid));");

        db.execSQL("CREATE TABLE Tarifas (" +
                "tarifaid INTEGER PRIMARY KEY," +
                "estacionamientoid INTEGER," +
                "tipovehiculo TEXT NOT NULL," +
                "precio REAL NOT NULL," +
                "FOREIGN KEY (estacionamientoid) REFERENCES Estacionamientos(estacionamientoid));");

        db.execSQL("CREATE TABLE HistorialAcceso (" +
                "historialid INTEGER PRIMARY KEY," +
                "reservaid INTEGER," +
                "fechahoraingreso TEXT," +
                "fechahorasalida TEXT," +
                "FOREIGN KEY (reservaid) REFERENCES Reservas(reservaid));");

        db.execSQL("CREATE TABLE Chats (" +
                "chatid INTEGER PRIMARY KEY," +
                "usuarioid INTEGER," +
                "mensaje TEXT NOT NULL," +
                "fechaenvio TEXT NOT NULL," +
                "FOREIGN KEY (usuarioid) REFERENCES Usuarios(usuarioid));");

        // Insertar datos iniciales en Perfiles
        db.execSQL("INSERT INTO Perfiles (perfilid, nombreperfil) VALUES (1, 'Administrador');");
        db.execSQL("INSERT INTO Perfiles (perfilid, nombreperfil) VALUES (2, 'Cliente');");

// Insertar datos iniciales en Usuarios con perfil como texto
        db.execSQL("INSERT INTO Usuarios (usuarioid, perfil, nombre, apellido, correo, contrasena) " +
                "VALUES (1, 'Administrador', 'admin', 'admin', 'admin@gmail.com', '1234');");
        db.execSQL("INSERT INTO Usuarios (usuarioid, perfil, nombre, apellido, correo, contrasena) " +
                "VALUES (2, 'Cliente', 'cliente', 'cliente', 'cliente@gmail.com', '1234');");

        // Tabla Estacionamientos
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (1, 'La Victoria', 'Estacion gamarra', 1);");
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (2, 'Los Olivos', 'Estadio Nacional', 1);");
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (3, 'San Borja', 'Estacion San Borja', 1);");
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (4, 'Chaclacayo', 'Calle 456', 1);");

// Tabla Espacios con ubicación
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (1, 1, 'A1', 'Disponible', '1A');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (2, 1, 'A2', 'Disponible', '1B');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (3, 1, 'A3', 'Disponible', '1C');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (4, 1, 'A4', 'Disponible', '1D');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (5, 1, 'A5', 'Disponible', '1E');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (6, 1, 'A6', 'Disponible', '1F');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (7, 1, 'A7', 'Disponible', '1G');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (8, 1, 'A8', 'Disponible', '1H');");

        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (9, 2, 'B1', 'Disponible', '2A');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (10, 2, 'B2', 'Disponible', '2B');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (11, 2, 'B3', 'Disponible', '2C');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (12, 2, 'B4', 'Disponible', '2D');");

        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (13, 3, 'C1', 'Disponible', '3A');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (14, 3, 'C2', 'Disponible', '3B');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (15, 3, 'C3', 'Disponible', '3C');");

        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (16, 4, 'D1', 'Disponible', '4A');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (17, 4, 'D2', 'Disponible', '4B');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado, ubicacion) " +
                "VALUES (18, 4, 'D3', 'Disponible', '4C');");


// Tabla Vehiculos
        // Tabla Vehiculos (cada uno con su vehiculoid único)
        db.execSQL("INSERT INTO Vehiculos (vehiculoid, usuarioid, placa, tipo, color) VALUES (1, 2, 'ABC123', 'Carro', 'Rojo');");     // Para La Victoria
        db.execSQL("INSERT INTO Vehiculos (vehiculoid, usuarioid, placa, tipo, color) VALUES (2, 2, 'XYZ789', 'Moto', 'Negro');");     // Para Los Olivos
        db.execSQL("INSERT INTO Vehiculos (vehiculoid, usuarioid, placa, tipo, color) VALUES (3, 2, 'CAM456', 'Camion', 'Azul');");    // Para San Borja
        db.execSQL("INSERT INTO Vehiculos (vehiculoid, usuarioid, placa, tipo, color) VALUES (4, 2, 'CAR999', 'Carro', 'Blanco');");   // Para Chaclacayo


// Tabla Reservas
        // Reservas con cada vehiculoid correspondiente y fechas bien formateadas
        db.execSQL("INSERT INTO Reservas (reservaid, usuarioid, espacioid, vehiculoid, fechareserva, horaentrada, horasalida, estado) " +
                "VALUES (1, 2, 2, 1, '20/07/2025', '08:00', '10:00', 'Confirmada');"); // La Victoria - Carro

        db.execSQL("INSERT INTO Reservas (reservaid, usuarioid, espacioid, vehiculoid, fechareserva, horaentrada, horasalida, estado) " +
                "VALUES (2, 2, 9, 2, '20/07/2025', '09:00', '11:00', 'Confirmada');"); // Los Olivos - Moto

        db.execSQL("INSERT INTO Reservas (reservaid, usuarioid, espacioid, vehiculoid, fechareserva, horaentrada, horasalida, estado) " +
                "VALUES (3, 2, 13, 3, '20/07/2025', '10:00', '12:00', 'Confirmada');"); // San Borja - Camion

        db.execSQL("INSERT INTO Reservas (reservaid, usuarioid, espacioid, vehiculoid, fechareserva, horaentrada, horasalida, estado) " +
                "VALUES (4, 2, 16, 4, '20/07/2025', '07:00', '09:00', 'Confirmada');"); // Chaclacayo - Carro


// Tabla Pagos
        db.execSQL("INSERT INTO Pagos (pagoid, reservaid, monto, fechapago) " +
                "VALUES (1, 1, 15.00, '2025-05-17');");

// Tabla Tarifas
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (1, 1, 'Carro', 7.5);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (2, 1, 'Moto', 3.0);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (3, 1, 'Camion', 10.0);");

        // Tarifas para Estacionamiento 2 (Los Olivos)
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (4, 2, 'Carro', 7.0);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (5, 2, 'Moto', 2.5);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (6, 2, 'Camion', 9.5);");

        // Tarifas para Estacionamiento 3 (San Borja)
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (7, 3, 'Carro', 8.0);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (8, 3, 'Moto', 3.5);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (9, 3, 'Camion', 11.0);");

        // Tarifas para Estacionamiento 4 (Chaclacayo)
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (10, 4, 'Carro', 6.5);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (11, 4, 'Moto', 2.0);");
        db.execSQL("INSERT INTO Tarifas (tarifaid, estacionamientoid, tipovehiculo, precio) VALUES (12, 4, 'Camion', 9.0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Agrega columna 'placa'
            db.execSQL("ALTER TABLE Reservas ADD COLUMN placa TEXT");
        }

        if (oldVersion < 3) {
            // Agrega columna 'pago' y 'ubicacion' si aún no existen
            try {
                db.execSQL("ALTER TABLE Reservas ADD COLUMN pago REAL DEFAULT 0");
            } catch (Exception e) {
                // La columna ya existe, ignora el error
            }
            try {
                db.execSQL("ALTER TABLE Reservas ADD COLUMN ubicacion TEXT DEFAULT 'No definido'");
            } catch (Exception e) {
                // La columna ya existe, ignora el error
            }
        }

        if (oldVersion < 4) {
            // Cambios grandes: eliminar y recrear tablas
            db.execSQL("DROP TABLE IF EXISTS Chats");
            db.execSQL("DROP TABLE IF EXISTS HistorialAcceso");
            db.execSQL("DROP TABLE IF EXISTS Tarifas");
            db.execSQL("DROP TABLE IF EXISTS Pagos");
            db.execSQL("DROP TABLE IF EXISTS Reservas");
            db.execSQL("DROP TABLE IF EXISTS Vehiculos");
            db.execSQL("DROP TABLE IF EXISTS Espacios");
            db.execSQL("DROP TABLE IF EXISTS Estacionamientos");
            db.execSQL("DROP TABLE IF EXISTS Usuarios");
            db.execSQL("DROP TABLE IF EXISTS Perfiles");
            onCreate(db);
        }
    }

    public Usuario obtenerUsuarioPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Usuarios",
                new String[]{"usuarioid", "uid", "nombre", "apellido", "correo", "perfil", "estado", "edad"},
                "usuarioid = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("usuarioid")));
            usuario.setUid(cursor.getString(cursor.getColumnIndexOrThrow("uid")));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow("apellido")));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow("correo")));
            usuario.setPerfil(cursor.getString(cursor.getColumnIndexOrThrow("perfil")));
            usuario.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
            usuario.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow("edad")));
            cursor.close();
            return usuario;
        }

        if (cursor != null) cursor.close();
        return null;
    }

    public Usuario obtenerUsuarioPorUid(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Usuarios", null, "uid = ?", new String[]{uid}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow("usuarioid")));
            usuario.setUid(cursor.getString(cursor.getColumnIndexOrThrow("uid")));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow("apellido")));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow("correo")));
            usuario.setPerfil(cursor.getString(cursor.getColumnIndexOrThrow("perfil")));
            usuario.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
            usuario.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow("edad")));
            cursor.close();
            return usuario;
        }

        if (cursor != null) cursor.close();
        return null;
    }



    public boolean insertarUsuarioCompleto(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid", usuario.getUid());
        values.put("perfil", usuario.getPerfil());
        values.put("nombre", usuario.getNombre());
        values.put("apellido", usuario.getApellido());
        values.put("correo", usuario.getCorreo());
        values.put("contrasena", usuario.getContrasena()); // si no tienes, pon ""
        values.put("celular", usuario.getCelular());       // si no tienes, pon ""
        values.put("estado", usuario.getEstado());
        values.put("edad", usuario.getEdad());

        long result = db.insert("Usuarios", null, values);
        db.close();

        if (result == -1) {
            Log.e("DBHelper", "Error al insertar usuario con UID");
            return false;
        } else {
            Log.d("DBHelper", "Usuario insertado con UID: " + usuario.getUid());
            return true;
        }
    }


    public void mostrarUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios", null);
        Log.d("USUARIO",  ", Correo:  ");
        while (cursor.moveToNext()) {
            String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
            String clave = cursor.getString(cursor.getColumnIndexOrThrow("contrasena")); // ✅
            Log.d("USUARIO",  ", Correo: " + correo + ", Clave: " + clave);
        }
        cursor.close();
    }
    public boolean tieneReservasEnFecha(String fecha) {
        boolean hayReservas = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String query = "SELECT COUNT(*) FROM Reservas WHERE fechareserva = ?";
            cursor = db.rawQuery(query, new String[]{fecha});
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                hayReservas = count > 0;
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error al verificar reservas en fecha: " + fecha, e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return hayReservas;
    }


    public List<Estacionamiento> obtenerEstacionamientos() {
        List<Estacionamiento> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Estacionamientos", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("estacionamientoid"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"));
                int propietarioId = cursor.getInt(cursor.getColumnIndexOrThrow("propietarioid"));

                lista.add(new Estacionamiento(id, nombre, direccion, propietarioId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }
    public List<Espacio> obtenerEspaciosPorEstacionamiento(int estacionamientoId) {
        List<Espacio> espacios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Espacios WHERE estacionamientoid = ?",
                new String[]{String.valueOf(estacionamientoId)});

        if (cursor.moveToFirst()) {
            do {
                Espacio espacio = new Espacio();
                espacio.setEspacioId(cursor.getInt(cursor.getColumnIndexOrThrow("espacioid")));
                espacio.setEstacionamientoId(cursor.getInt(cursor.getColumnIndexOrThrow("estacionamientoid")));
                espacio.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow("codigo")));
                espacio.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                espacios.add(espacio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return espacios;
    }
    public List<Tarifa> obtenerTarifasPorEstacionamiento(int estacionamientoId) {
        List<Tarifa> listaTarifas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT tarifaid, estacionamientoid, tipovehiculo, precio FROM Tarifas WHERE estacionamientoid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(estacionamientoId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int estId = cursor.getInt(1);
                String tipoVehiculo = cursor.getString(2);
                double precio = cursor.getDouble(3);
                listaTarifas.add(new Tarifa(id, estId, tipoVehiculo, precio));

                Log.d("DEBUG", "Tarifa encontrada - ID: " + id + ", Tipo: " + tipoVehiculo + ", Precio: " + precio);
            } while (cursor.moveToNext());
        } else {
            Log.d("DEBUG", "No se encontraron tarifas para estacionamientoId: " + estacionamientoId);
        }

        cursor.close();
        db.close();

        return listaTarifas;
    }
    public boolean insertarTarifa(int estacionamientoId, String tipoVehiculo, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estacionamientoid", estacionamientoId);
        values.put("tipovehiculo", tipoVehiculo);
        values.put("precio", precio);

        long resultado = db.insert("Tarifas", null, values);
        return resultado != -1;
    }

    public String obtenerNombreEstacionamientoPorId(int estacionamientoId) {
        SQLiteDatabase db = getReadableDatabase();
        String nombre = null;

        Cursor cursor = db.rawQuery(
                "SELECT nombre FROM Estacionamientos WHERE estacionamientoid = ?",
                new String[]{String.valueOf(estacionamientoId)}
        );
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return nombre;
    }


    public List<String> obtenerTiposVehiculoPorEstacionamiento(int estacionamientoId) {
        List<String> tipos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT tipovehiculo FROM Tarifas WHERE estacionamientoid = ?",
                new String[]{String.valueOf(estacionamientoId)});

        if (cursor.moveToFirst()) {
            do {
                tipos.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tipos;
    }
    public double obtenerPrecioPorTipoVehiculo(int estacionamientoId, String tipoVehiculo) {
        SQLiteDatabase db = this.getReadableDatabase();
        double precio = -1;

        Cursor cursor = db.rawQuery("SELECT precio FROM Tarifas WHERE estacionamientoid = ? AND tipovehiculo = ?",
                new String[]{String.valueOf(estacionamientoId), tipoVehiculo});

        if (cursor.moveToFirst()) {
            precio = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return precio;
    }

    public List<Reserva> obtenerReservasPorFecha(String fecha) {
        List<Reserva> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String query = "SELECT r.*, u.nombre, u.apellido, v.tipo, e.codigo, e.ubicacion, est.nombre AS nombre_estacionamiento " +
                    "FROM Reservas r " +
                    "LEFT JOIN Usuarios u ON r.usuarioid = u.usuarioid " +
                    "LEFT JOIN Vehiculos v ON r.vehiculoid = v.vehiculoid " +
                    "LEFT JOIN Espacios e ON r.espacioid = e.espacioid " +
                    "LEFT JOIN Estacionamientos est ON e.estacionamientoid = est.estacionamientoid " +  // ⬅️ aquí va el JOIN correcto
                    "WHERE r.fechareserva = ?";

            cursor = db.rawQuery(query, new String[]{fecha});

            if (cursor.moveToFirst()) {
                do {
                    Reserva r = new Reserva();
                    r.setReservaid(cursor.getInt(cursor.getColumnIndexOrThrow("reservaid")));
                    r.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuarioid")));
                    r.setEspacioId(cursor.getInt(cursor.getColumnIndexOrThrow("espacioid")));
                    r.setVehiculoId(cursor.getInt(cursor.getColumnIndexOrThrow("vehiculoid")));
                    r.setPlaca(cursor.getString(cursor.getColumnIndexOrThrow("placa")));
                    r.setHoraEntrada(cursor.getString(cursor.getColumnIndexOrThrow("horaentrada")));
                    r.setHoraSalida(cursor.getString(cursor.getColumnIndexOrThrow("horasalida")));
                    r.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fechareserva")));
                    r.setPago(cursor.getDouble(cursor.getColumnIndexOrThrow("pago")));
                    r.setUbicacion(cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")));
                    r.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));

                    // Datos del JOIN
                    r.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                    r.setApellidoUsuario(cursor.getString(cursor.getColumnIndexOrThrow("apellido")));
                    r.setTipoVehiculo(cursor.getString(cursor.getColumnIndexOrThrow("tipo")));
                    r.setCodigoEspacio(cursor.getString(cursor.getColumnIndexOrThrow("codigo")));
                    r.setNombreEstacionamiento(cursor.getString(cursor.getColumnIndexOrThrow("nombre_estacionamiento")));

                    lista.add(r);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error al obtener reservas con JOIN", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return lista;
    }

    public boolean insertarUsuario(String nombre, String apellido, String correo, String celular, String perfil, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid", uid);
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("correo", correo);
        values.put("celular", celular);

        // Obtener el ID del perfil desde la tabla Perfiles
        int perfilId = obtenerPerfilId(perfil); // Este método lo explico abajo
        values.put("perfilid", perfilId);

        // Puedes agregar valores por defecto si quieres:
        values.put("estado", "activo");
        values.put("edad", 0);

        long resultado = db.insert("Usuarios", null, values);
        db.close();

        return resultado != -1;
    }
    private int obtenerPerfilId(String nombrePerfil) {
        SQLiteDatabase db = this.getReadableDatabase();
        int perfilId = -1;

        Cursor cursor = db.rawQuery("SELECT perfilid FROM Perfiles WHERE nombreperfil = ?", new String[]{nombrePerfil});

        if (cursor.moveToFirst()) {
            perfilId = cursor.getInt(0);
        }

        cursor.close();
        return perfilId;
    }

    public boolean validarUsuario(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM Usuarios WHERE correo = ? AND contrasena = ?",
                new String[]{correo, contrasena}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }



    public Usuario verificarUsuario(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = null;

        String query = "SELECT u.usuarioid, u.nombre, u.apellido, u.correo, p.nombreperfil " +
                "FROM Usuarios u " +
                "INNER JOIN Perfiles p ON u.perfilid = p.perfilid " +
                "WHERE u.correo = ? AND u.contrasena = ?";

        Cursor cursor = db.rawQuery(query, new String[]{correo, contrasena});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String apellido = cursor.getString(2);
            String correoObtenido = cursor.getString(3);
            String perfil = cursor.getString(4);

            usuario = new Usuario(id, nombre, apellido, correoObtenido, perfil);
        }

        cursor.close();
        db.close();
        return usuario;
    }


    public boolean insertarReserva(Reserva r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("usuarioid", r.getUsuarioId());
        values.put("espacioid", r.getEspacioId());
        values.put("vehiculoid", r.getVehiculoId());
        values.put("fechareserva", r.getFecha());
        values.put("horaentrada", r.getHoraEntrada());
        values.put("horasalida", r.getHoraSalida());
        values.put("estado", r.getEstado());
        values.put("placa", r.getPlaca());

        // Aquí agregas estos 2 campos que faltan
        values.put("pago", r.getPagoHora());
        values.put("ubicacion", r.getUbicacion());

        long resultado = db.insert("Reservas", null, values);
        return resultado != -1;
    }



    public boolean actualizarContrasena(String correo, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contrasena", newPassword);

        // Cambiado "email" por "correo" para que coincida con el esquema de la tabla
        int filasActualizadas = db.update("Usuarios", values, "correo = ?", new String[]{correo});
        db.close();

        return filasActualizadas > 0;
    }
    public double obtenerTarifaPorTipo(String tipoVehiculo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT precio FROM Tarifas WHERE tipovehiculo = ?", new String[]{tipoVehiculo});
        if (cursor.moveToFirst()) {
            double tarifa = cursor.getDouble(0);
            cursor.close();
            return tarifa;
        }
        cursor.close();
        return 0.0;
    }

    public void actualizarTarifa(String tipoVehiculo, double nuevaTarifa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("precio", nuevaTarifa);
        db.update("Tarifas", values, "tipovehiculo = ?", new String[]{tipoVehiculo});
    }

    public Cursor obtenerDatosUsuario(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Cambiado "id" por "usuarioid"
        String query = "SELECT nombre, correo FROM Usuarios WHERE usuarioid = ?";
        return db.rawQuery(query, new String[]{String.valueOf(usuarioId)});
    }

    // Método opcional para agregar un usuario de prueba
    public boolean insertarUsuario(String nombre, String apellido, String correo, String celular, String perfil, String uid, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid", uid);
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("correo", correo);
        values.put("celular", celular);
        values.put("contrasena", contrasena);
        values.put("estado", "activo");
        values.put("edad", 0);
        values.put("perfil", perfil);  // 👈 guardar como texto (no como ID)

        long resultado = db.insert("Usuarios", null, values);

        if (resultado == -1) {
            Log.e("DBHelper", "❌ Error al insertar usuario en SQLite");
        } else {
            Log.d("DBHelper", "✅ Usuario insertado con éxito, ID: " + resultado + ", UID: " + uid);
        }

        db.close();
        return resultado != -1;
    }




    public void insertarReserva( int usuarioid, int espacioid, int vehiculoid, String fechareserva, String horaentrada,String horasalida,String estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuarioid", usuarioid);
        values.put("espacioid", espacioid);
        values.put("vehiculoid", vehiculoid);
        values.put("fechareserva", fechareserva);
        values.put("horaentrada", horaentrada);
        values.put("horasalida", horasalida);
        values.put("estado", estado);
        db.insert("Reservas", null, values);
    }

    public void insertarVehiculo( int usuarioid, String placa, String tipo,String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuarioid", usuarioid);
        values.put("placa", placa);
        values.put("tipo", tipo);
        values.put("color", color);
        db.insert("Vehiculos", null, values);
    }


    public void insertarPerfil( String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombreperfil", nombre);
        db.insert("Perfiles", null, values);
    }

    public void insertarEstacionamiento( String nombre, String direccion, int propietarioid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("direccion", direccion);
        values.put("propietarioid", propietarioid);
        db.insert("Estacionamientos", null, values);
    }

    public void insertarEspacio( int estacionamientoid, String codigo, String estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estacionamientoid", estacionamientoid);
        values.put("codigo", codigo);
        values.put("estado", estado);
        db.insert("Espacios", null, values);
    }

    public void insertarTarifas( int estacionamientoid, String tipovehiculo, String precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estacionamientoid", estacionamientoid);
        values.put("tipovehiculo", tipovehiculo);
        values.put("precio", precio);
        db.insert("Tarifas", null, values);
    }
    public long insertarUsuarioYRetornarID(String nombre, String apellido, String correo,
                                           String contrasena, String celular, int perfilid, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("correo", correo);
        values.put("contrasena", contrasena);  //
        values.put("celular", celular);
        values.put("perfil", (perfilid == 1) ? "Administrador" : "Cliente");
        values.put("uid", uid);

        return db.insert("Usuarios", null, values);
    }

    // reservas duplicadas

    public boolean existeReservaEnEspacio(int espacioId, int estacionamientoId, String fecha, String nuevaEntrada, String nuevaSalida) {
        SQLiteDatabase db = this.getReadableDatabase();

        int nuevaEntradaMin = convertirHoraAMinutos(nuevaEntrada);
        int nuevaSalidaMin = convertirHoraAMinutos(nuevaSalida);

        Cursor cursor = db.rawQuery(
                "SELECT r.horaentrada, r.horasalida " +
                        "FROM Reservas r " +
                        "JOIN Espacios e ON r.espacioid = e.espacioid " +
                        "WHERE r.espacioid = ? AND e.estacionamientoid = ? AND r.fechareserva = ?",
                new String[]{
                        String.valueOf(espacioId),
                        String.valueOf(estacionamientoId),
                        fecha
                }
        );

        boolean conflicto = false;

        while (cursor.moveToNext()) {
            String entradaExistente = cursor.getString(0);
            String salidaExistente = cursor.getString(1);

            int entradaExistenteMin = convertirHoraAMinutos(entradaExistente);
            int salidaExistenteMin = convertirHoraAMinutos(salidaExistente);

            // Verifica traslape de horarios
            if (!(nuevaSalidaMin <= entradaExistenteMin || nuevaEntradaMin >= salidaExistenteMin)) {
                conflicto = true;
                break;
            }
        }

        cursor.close();
        db.close();
        return conflicto;
    }


    public int convertirHoraAMinutos(String hora) {
        try {
            if (hora == null || hora.trim().isEmpty()) return -1;

            String[] partes = hora.trim().split(" ");
            if (partes.length != 2) return -1;

            String[] hm = partes[0].split(":");
            if (hm.length != 2) return -1;

            int h = Integer.parseInt(hm[0]);
            int m = Integer.parseInt(hm[1]);

            String ampm = partes[1].toUpperCase(Locale.getDefault());

            if (ampm.equals("PM") && h != 12) h += 12;
            if (ampm.equals("AM") && h == 12) h = 0;

            return h * 60 + m;
        } catch (Exception e) {
            return -1;
        }
    }

}

