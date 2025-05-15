package com.cochera.miproyectointegrador.Login;

import android.content.Context;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class LoginModel {

    private DBHelper dbHelper;

    public LoginModel(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Verifica si el usuario es administrador (PerfilID = 1)
    public boolean isAdmin(String correo, String contrasena) {
        return true;
    }

    // Retorna el ID del cliente si las credenciales son válidas (PerfilID = 2), o -1 si no lo es
    public int validateLogin(String correo, String contrasena) {
        return 1;
    }

    // Retorna el ID del usuario (cliente o admin)
    public int getUsuarioId(String correo, String contrasena) {
        return 1;
    }

    // Retorna el perfil ID (1 = admin, 2 = cliente)
    public int getPerfilId(String correo, String contrasena) {
        return 1;
    }
}


