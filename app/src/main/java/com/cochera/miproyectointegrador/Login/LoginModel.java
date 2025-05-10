package com.cochera.miproyectointegrador.Login;

<<<<<<< HEAD
import android.content.Context;/*
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;*/
=======
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
>>>>>>> 70cf38bf16dac277ecf77023b818e34e2e8818c4

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class LoginModel {
<<<<<<< HEAD

    /*
    private DBHelper dbHelper;


=======
    private DBHelper dbHelper;

>>>>>>> 70cf38bf16dac277ecf77023b818e34e2e8818c4
    public LoginModel(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Devuelve el ID del cliente si es correcto, o -1 si no existe
    public int validateLogin(String correo, String contraseña) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT ClienteID, Nombre FROM Cliente WHERE Correo=? AND Contraseña=?",
                new String[]{correo, contraseña}
        );
        if (c.moveToFirst()) {
            int clienteId = c.getInt(c.getColumnIndexOrThrow("ClienteID"));
            c.close();
            return clienteId;
        }
        c.close();
        return -1;
    }

    public String getNombreCliente(int clienteId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT Nombre FROM Cliente WHERE ClienteID=?",
                new String[]{String.valueOf(clienteId)}
        );
        String nombre = "";
        if (c.moveToFirst()) {
            nombre = c.getString(c.getColumnIndexOrThrow("Nombre"));
        }
        c.close();
        return nombre;
    }

    public String getCorreoCliente(int clienteId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT Correo FROM Cliente WHERE ClienteID=?",
                new String[]{String.valueOf(clienteId)}
        );
        String correo = "";
        if (c.moveToFirst()) {
            correo = c.getString(c.getColumnIndexOrThrow("Correo"));
        }
        c.close();
        return correo;
    }
<<<<<<< HEAD
    */

    //modificaciones testing

    private DBHelper dbHelper;

    public LoginModel(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean isAdmin(String correo, String contrasena) {
        return dbHelper.isAdmin(correo, contrasena);
    }

    public int validateLogin(String correo, String contrasena) {
        return dbHelper.validateLogin(correo, contrasena);
    }
=======
>>>>>>> 70cf38bf16dac277ecf77023b818e34e2e8818c4
}

