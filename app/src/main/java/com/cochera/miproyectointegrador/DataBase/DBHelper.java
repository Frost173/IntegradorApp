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
        db.execSQL("CREATE TABLE Administrador (" +
                "AdminID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NomCargo TEXT NOT NULL," +
                "NomInterfaz TEXT NOT NULL," +
                "Correo TEXT NOT NULL UNIQUE," +
                "Contrasena TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Cliente (" +
                "ClienteID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nombre TEXT NOT NULL," +
                "Correo TEXT NOT NULL UNIQUE," +
                "Contrasena TEXT NOT NULL," +
                "Celular TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Vehiculos (" +
                "VehiculoID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Placa TEXT NOT NULL UNIQUE," +
                "Color TEXT NOT NULL," +
                "Tipo TEXT NOT NULL," +
                "ClienteID INTEGER NOT NULL," +
                "FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID))");

        db.execSQL("CREATE TABLE Estacionamiento (" +
                "EstacionamientoID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AdminID INTEGER NOT NULL," +
                "Nombre TEXT NOT NULL," +
                "Direccion TEXT NOT NULL," +
                "Distrito TEXT NOT NULL," +
                "FOREIGN KEY (AdminID) REFERENCES Administrador(AdminID))");

        db.execSQL("CREATE TABLE Espacios (" +
                "EspacioID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "EstacionamientoID INTEGER NOT NULL," +
                "Codigo TEXT NOT NULL," +
                "Ubicacion TEXT NOT NULL," +
                "Estado TEXT NOT NULL," +
                "FOREIGN KEY (EstacionamientoID) REFERENCES Estacionamiento(EstacionamientoID))");

        db.execSQL("CREATE TABLE Reservas (" +
                "ReservaID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FechaReservada TEXT NOT NULL," +
                "HoraEntrada TEXT NOT NULL," +
                "HoraSalida TEXT NOT NULL," +
                "Estado TEXT NOT NULL," +
                "CodigoTicket TEXT NOT NULL," +
                "VehiculoID INTEGER NOT NULL," +
                "ClienteID INTEGER NOT NULL," +
                "EspacioID INTEGER NOT NULL," +
                "FOREIGN KEY (VehiculoID) REFERENCES Vehiculos(VehiculoID)," +
                "FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID)," +
                "FOREIGN KEY (EspacioID) REFERENCES Espacios(EspacioID))");

        db.execSQL("CREATE TABLE Pagos (" +
                "PagosID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ReservaID INTEGER NOT NULL," +
                "Monto REAL NOT NULL," +
                "Estado TEXT NOT NULL," +
                "FechaPago TEXT NOT NULL," +
                "Comprobante TEXT," +
                "FOREIGN KEY (ReservaID) REFERENCES Reservas(ReservaID))");

        db.execSQL("CREATE TABLE Tarifas (" +
                "TarifaID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PrecioHora REAL NOT NULL," +
                "TipoVehiculo TEXT NOT NULL," +
                "ReservaID INTEGER," +
                "EstacionamientoID INTEGER NOT NULL," +
                "FOREIGN KEY (ReservaID) REFERENCES Reservas(ReservaID)," +
                "FOREIGN KEY (EstacionamientoID) REFERENCES Estacionamiento(EstacionamientoID))");

        db.execSQL("CREATE TABLE Chat (" +
                "ChatID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Textos TEXT NOT NULL," +
                "FechaEnvio TEXT NOT NULL," +
                "FechaInicio TEXT NOT NULL," +
                "AdminID INTEGER NOT NULL," +
                "ClienteID INTEGER NOT NULL," +
                "FOREIGN KEY (AdminID) REFERENCES Administrador(AdminID)," +
                "FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID))");

        // Inserción del admin por defecto
        ContentValues values = new ContentValues();
        values.put("NomCargo", "SuperAdmin");
        values.put("NomInterfaz", "Lucas Huallpa");
        values.put("Correo", "admin.59@gmail.com");
        values.put("Contrasena", "AdminController@159");  // Ideal: almacenar hash en vez de texto plano

        db.insert("Administrador", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si cambias la estructura de la base de datos, puedes borrar y recrear aquí
        db.execSQL("DROP TABLE IF EXISTS Chat");
        db.execSQL("DROP TABLE IF EXISTS Tarifas");
        db.execSQL("DROP TABLE IF EXISTS Pagos");
        db.execSQL("DROP TABLE IF EXISTS Reservas");
        db.execSQL("DROP TABLE IF EXISTS Espacios");
        db.execSQL("DROP TABLE IF EXISTS Estacionamiento");
        db.execSQL("DROP TABLE IF EXISTS Vehiculos");
        db.execSQL("DROP TABLE IF EXISTS Cliente");
        db.execSQL("DROP TABLE IF EXISTS Administrador");
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
        Cursor cursor = db.rawQuery("SELECT * FROM Administrador WHERE Correo=? AND Contrasena=?",
                new String[]{correo, contrasena});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Validar cliente
    public int validateLogin(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ClienteID FROM Cliente WHERE Correo=? AND Contrasena=?",
                new String[]{correo, contrasena});
        int clienteId = -1;
        if (c.moveToFirst()) {
            clienteId = c.getInt(c.getColumnIndexOrThrow("ClienteID"));
        }
        c.close();
        return clienteId;
    }


}
