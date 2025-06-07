package com.cochera.miproyectointegrador.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase DBHelper para manejar la base de datos SQLite.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "tu_basedatos_2.db", null, 13);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Perfiles (" +
                "perfilid INTEGER PRIMARY KEY," +
                "nombreperfil TEXT NOT NULL);");

        db.execSQL("CREATE TABLE Usuarios (" +
                "usuarioid INTEGER PRIMARY KEY," +
                "perfilid INTEGER," +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "correo TEXT UNIQUE NOT NULL," +
                "contrasena TEXT NOT NULL," +
                "FOREIGN KEY (perfilid) REFERENCES Perfiles(perfilid));");

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
                "placa TEXT," +           // columna nueva versión 2
                "pago REAL DEFAULT 0," +  // columna nueva versión 3
                "ubicacion TEXT," +       // columna nueva versión 3
                "FOREIGN KEY (usuarioid) REFERENCES Usuarios(usuarioid)," +
                "FOREIGN KEY (espacioid) REFERENCES Espacios(espacioid)," +
                "FOREIGN KEY (vehiculoid) REFERENCES Vehiculos(vehiculoid));");


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

        // Insertar datos iniciales en Usuarios
        db.execSQL("INSERT INTO Usuarios (usuarioid, perfilid, nombre, apellido, correo, contrasena) " +
                "VALUES (1, 1, 'admin', 'admin', 'admin@gmail.com', '1234');");
        db.execSQL("INSERT INTO Usuarios (usuarioid, perfilid, nombre, apellido, correo, contrasena) " +
                "VALUES (2, 2, 'cliente', 'cliente', 'cliente@gmail.com', '1234');");
        // Tabla Estacionamientos
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (1, 'La Victoria', 'Estacion gamarra', 1);");
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (2, 'Los Olivos', 'Estadio Nacional', 1);");
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (3, 'San Borja', 'Estacion San Borja', 1);");
        db.execSQL("INSERT INTO Estacionamientos (estacionamientoid, nombre, direccion, propietarioid) " +
                "VALUES (4, 'Chaclacayo', 'Calle 456', 1);");

// Tabla Espacios
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (1, 1, 'A1', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (2, 1, 'A2', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (3, 1, 'A3', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (4, 1, 'A4', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (5, 1, 'A5', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (6, 1, 'A6', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (7, 1, 'A7', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (8, 1, 'A8', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (9, 2, 'B1', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (10, 2, 'B2', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (11, 2, 'B3', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (12, 2, 'B4', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (13, 3, 'C1', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (14, 3, 'C2', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (15, 3, 'C3', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (16, 4, 'D1', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (17, 4, 'D2', 'Disponible');");
        db.execSQL("INSERT INTO Espacios (espacioid, estacionamientoid, codigo, estado) " +
                "VALUES (18, 4, 'D3', 'Disponible');");

// Tabla Vehiculos
        db.execSQL("INSERT INTO Vehiculos (vehiculoid, usuarioid, placa, tipo, color) " +
                "VALUES (1, 2, 'ABC123', 'Carro', 'Rojo');");
        db.execSQL("INSERT INTO Vehiculos (vehiculoid, usuarioid, placa, tipo, color) " +
                "VALUES (2, 2, 'XYZ789', 'Moto', 'Negro');");

// Tabla Reservas
        db.execSQL("INSERT INTO Reservas (reservaid, usuarioid, espacioid, vehiculoid, fechareserva, horaentrada, horasalida, estado) " +
                "VALUES (1, 2, 2, 1, '2025-05-17', '08:00', '10:00', 'Confirmada');");

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


    public void mostrarUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios", null);
        Log.d("USUARIO",  ", Correo:  ");
        while (cursor.moveToNext()) {
            String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
            String clave = cursor.getString(cursor.getColumnIndexOrThrow("clave"));
            Log.d("USUARIO",  ", Correo: " + correo + ", Clave: " + clave);
        }
        cursor.close();
    }
    public boolean tieneReservasEnFecha(String fecha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM reservas WHERE fechareserva = ?";
        Cursor cursor = db.rawQuery(query, new String[]{fecha});
        boolean tieneReserva = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                tieneReserva = count > 0;
            }
            cursor.close();
        }

        return tieneReserva;
    }



    // Ejemplo: Obtenemos tarifas para un estacionamiento específico
//    public List<String> obtenerTarifasPorEstacionamiento( int estacionamientoId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<String> listaTarifas = new ArrayList<>();
//        String sql = "SELECT tipovehiculo || ' - $' || precio as descripcion FROM Tarifas WHERE estacionamientoid = ?";
//        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(estacionamientoId)});
//
//        if (cursor.moveToFirst()) {
//            do {
//                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
//                listaTarifas.add(descripcion);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return listaTarifas;
//    }
//    public Map<String, Double> obtenerTarifasPorEstacionamiento(int estacionamientoId) {
//        Map<String, Double> tarifasMap = new HashMap<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String sql = "SELECT tipovehiculo, precio FROM Tarifas WHERE estacionamientoid = ?";
//        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(estacionamientoId)});
//
//        if (cursor.moveToFirst()) {
//            do {
//                String tipoVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("tipovehiculo"));
//                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
//
//                tarifasMap.put(tipoVehiculo, precio);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//        return tarifasMap;
//    }

    public List<Tarifa> obtenerTarifasPorEstacionamiento(int estacionamientoId) {
        List<Tarifa> tarifasList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT tarifaid, tipovehiculo, precio FROM Tarifas WHERE estacionamientoid = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(estacionamientoId)});

        if (cursor.moveToFirst()) {
            do {
                int idTarifa = cursor.getInt(cursor.getColumnIndexOrThrow("tarifaid"));
                String tipoVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("tipovehiculo"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));

                tarifasList.add(new Tarifa(idTarifa, tipoVehiculo, precio));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tarifasList;
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
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Reservas WHERE fechareserva = ?", new String[]{fecha});

        if (cursor.moveToFirst()) {
            int idxPlaca = cursor.getColumnIndex("placa");
            int idxHoraEntrada = cursor.getColumnIndex("horaentrada");
            int idxHoraSalida = cursor.getColumnIndex("horasalida");
            int idxFecha = cursor.getColumnIndex("fechareserva");
            int idxPago = cursor.getColumnIndex("pago");
            int idxUbicacion = cursor.getColumnIndex("ubicacion");

            do {
                Reserva r = new Reserva();

                if (idxPlaca != -1 && !cursor.isNull(idxPlaca))
                    r.setPlaca(cursor.getString(idxPlaca));
                else
                    r.setPlaca("N/A");

                if (idxHoraEntrada != -1 && !cursor.isNull(idxHoraEntrada))
                    r.setHoraEntrada(cursor.getString(idxHoraEntrada));
                else
                    r.setHoraEntrada("00:00");

                if (idxHoraSalida != -1 && !cursor.isNull(idxHoraSalida))
                    r.setHoraSalida(cursor.getString(idxHoraSalida));
                else
                    r.setHoraSalida("00:00");

                if (idxFecha != -1 && !cursor.isNull(idxFecha))
                    r.setFecha(cursor.getString(idxFecha));
                else
                    r.setFecha("00/00/0000");

                if (idxPago != -1 && !cursor.isNull(idxPago))
                    r.setPagoHora(cursor.getDouble(idxPago));
                else
                    r.setPagoHora(0.0);

                if (idxUbicacion != -1 && !cursor.isNull(idxUbicacion))
                    r.setUbicacion(cursor.getString(idxUbicacion));
                else
                    r.setUbicacion("No definido");

                lista.add(r);
            } while (cursor.moveToNext());
        } else {
            Log.e("DBHelper", "No se encontraron reservas para la fecha: " + fecha);
        }

        cursor.close();
        return lista;
    }






    public String obtenerNombreEstacionamientoPorId(int estacionamientoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String nombre = null;
        Cursor cursor = db.rawQuery("SELECT nombre FROM Estacionamientos WHERE estacionamientoid = ?",
                new String[]{String.valueOf(estacionamientoId)});
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return nombre;
    }

    public Usuario  verificarUsuario(String correo, String contrasena) {

//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE correo = ? AND contrasena = ?", new String[]{correoss,contrasena});
//        boolean existe = cursor.getCount() > 0;
//        cursor.close();
//        return existe;
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



    public boolean actualizarContrasena(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contrasena", newPassword);

        int filasActualizadas = db.update("usuarios", values, "email = ?", new String[]{email});
        db.close();

        return filasActualizadas > 0;
    }


    // Método opcional para agregar un usuario de prueba
    public void insertarUsuario( String nombre, String apellido, String correo, String contraseña, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("correo", correo);
        values.put("apellido", apellido);
        values.put("contrasena", contraseña);
        values.put("perfilid", id);
        db.insert("Usuarios", null, values);
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
}


