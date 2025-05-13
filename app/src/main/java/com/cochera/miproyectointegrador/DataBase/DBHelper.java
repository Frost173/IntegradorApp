package com.cochera.miproyectointegrador.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    private static final String DATABASE_NAME = "parkingapp.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios (" +
                "UsuarioID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL," +
                "Apellido TEXT NOT NULL," +
                "Correo TEXT NOT NULL UNIQUE," +
                "Contrasena TEXT NOT NULL);");

        db.execSQL("CREATE TABLE Perfiles (" +
                "PerfilesID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NomPerfil TEXT NOT NULL," +
                "UsuarioID INTEGER NOT NULL," +
                "FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID));");

        db.execSQL("CREATE TABLE Chats (" +
                "ChatID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Mensaje TEXT NOT NULL," +
                "FechaEnvio TEXT NOT NULL," +
                "UsuarioID INTEGER NOT NULL," +
                "FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID));");

        db.execSQL("CREATE TABLE Vehiculos (" +
                "VehiculosID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Tipo TEXT NOT NULL," +
                "Color TEXT NOT NULL," +
                "Placa TEXT NOT NULL UNIQUE," +
                "UsuarioID INTEGER NOT NULL," +
                "FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID));");

        db.execSQL("CREATE TABLE Estacionamiento (" +
                "EstacionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Ubicacion TEXT NOT NULL," +
                "UsuarioID INTEGER NOT NULL," +
                "FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID));");

        db.execSQL("CREATE TABLE Tarifas (" +
                "TarifaID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TipoVehiculo TEXT NOT NULL," +
                "Precio REAL NOT NULL," +
                "EstacionID INTEGER NOT NULL," +
                "FOREIGN KEY (EstacionID) REFERENCES Estacionamiento(EstacionID));");

        db.execSQL("CREATE TABLE Espacios (" +
                "EspacioID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Codigo TEXT NOT NULL UNIQUE," +
                "Estado TEXT NOT NULL," +
                "EstacionID INTEGER NOT NULL," +
                "FOREIGN KEY (EstacionID) REFERENCES Estacionamiento(EstacionID));");

        db.execSQL("CREATE TABLE Reservas (" +
                "ReservalID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FechaEntrada TEXT NOT NULL," +
                "HoraEntrada TEXT NOT NULL," +
                "HoraSalida TEXT," +
                "Estado TEXT NOT NULL," +
                "UsuarioID INTEGER NOT NULL," +
                "EspacioID INTEGER NOT NULL," +
                "VehiculosID INTEGER NOT NULL," +
                "FOREIGN KEY (UsuarioID) REFERENCES Usuarios(UsuarioID)," +
                "FOREIGN KEY (EspacioID) REFERENCES Espacios(EspacioID)," +
                "FOREIGN KEY (VehiculosID) REFERENCES Vehiculos(VehiculosID));");

        db.execSQL("CREATE TABLE Pagos (" +
                "PagosID TEXT PRIMARY KEY," +
                "Monto REAL NOT NULL," +
                "FechaPago TEXT NOT NULL," +
                "ReservalID INTEGER NOT NULL," +
                "FOREIGN KEY (ReservalID) REFERENCES Reservas(ReservalID));");

        db.execSQL("CREATE TABLE HistorialAcceso (" +
                "HistorialID TEXT PRIMARY KEY," +
                "FechaIngreso TEXT NOT NULL," +
                "FechaSalida TEXT," +
                "ReservalID INTEGER NOT NULL," +
                "FOREIGN KEY (ReservalID) REFERENCES Reservas(ReservalID));");

        // Insertar admin por defecto
        ContentValues adminUser = new ContentValues();
        adminUser.put("Nombre", "Admin");
        adminUser.put("Apellido", "Controller");
        adminUser.put("Correo", "admin.59@gmail.com");
        adminUser.put("Contrasena", "AdminController@159");
        long adminId = db.insert("Usuarios", null, adminUser);

        ContentValues adminProfile = new ContentValues();
        adminProfile.put("NomPerfil", "Admin");
        adminProfile.put("UsuarioID", adminId);
        db.insert("Perfiles", null, adminProfile);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si cambias la estructura de la base de datos, puedes borrar y recrear aquí
        db.execSQL("DROP TABLE IF EXISTS HistorialAcceso");
        db.execSQL("DROP TABLE IF EXISTS Pagos");
        db.execSQL("DROP TABLE IF EXISTS Reservas");
        db.execSQL("DROP TABLE IF EXISTS Espacios");
        db.execSQL("DROP TABLE IF EXISTS Tarifas");
        db.execSQL("DROP TABLE IF EXISTS Estacionamiento");
        db.execSQL("DROP TABLE IF EXISTS Vehiculos");
        db.execSQL("DROP TABLE IF EXISTS Chats");
        db.execSQL("DROP TABLE IF EXISTS Perfiles");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }


    public boolean registerClient(String nombre, String correo, String contrasena, String celular) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", nombre);
        values.put("Correo", correo);
        values.put("Contrasena", contrasena);
        values.put("Celular", celular);
        long result = db.insert("Cliente", null, values);
        return result != -1;
    }


    //Cuenta de mi administrador


    // Verificar si email existe
    public boolean emailExists(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Cliente WHERE Correo=?", new String[]{correo});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    // Cambiar contraseña
    public boolean updatePassword(String correo, String nuevaContrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Contraseña", nuevaContrasena);
        int rows = db.update("Cliente", cv, "Correo=?", new String[]{correo});
        return rows > 0;
    }
    //modifcacion testing
    // Validar administrador
    public boolean isAdmin(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Usuarios u " +
                        "INNER JOIN Perfiles p ON u.UsuarioID = p.UsuarioID " +
                        "WHERE u.Correo = ? AND u.Contrasena = ? AND p.NomPerfil = 'Admin'",
                new String[]{correo, contrasena}
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean isCliente(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Usuarios u " +
                        "INNER JOIN Perfiles p ON u.UsuarioID = p.UsuarioID " +
                        "WHERE u.Correo = ? AND u.Contrasena = ? AND p.NomPerfil = 'Cliente'",
                new String[]{correo, contrasena}
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean registerCliente(String nombre, String apellido, String correo,
                                   String contrasena, String dni, String celular) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", nombre);
        values.put("Apellido", apellido);
        values.put("Correo", correo);
        values.put("Contrasena", contrasena);

        try {
            db.beginTransaction();
            long userId = db.insertOrThrow("Usuarios", null, values);

            if(userId == -1) return false;

            ContentValues perfil = new ContentValues();
            perfil.put("NomPerfil", "Cliente");
            perfil.put("UsuarioID", userId);
            db.insertOrThrow("Perfiles", null, perfil);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Validar cliente
    public int validateLogin(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ClienteID FROM Cliente WHERE Correo=? AND Contrasena=?",
                new String[]{correo, contrasena});
        int usuarioId = -1;
        if (c.moveToFirst()) {
            usuarioId = c.getInt(c.getColumnIndexOrThrow("UsuarioID"));
        }
        c.close();
        return usuarioId;
    }



}
