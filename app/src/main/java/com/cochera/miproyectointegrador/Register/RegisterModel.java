package com.cochera.miproyectointegrador.Register;

import android.content.Context;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class RegisterModel {
        private DBHelper dbHelper;
        public RegisterModel(Context context) {
            dbHelper = new DBHelper(context);
        }
        public boolean register(String nombre, String correo, String contrasena,String celular) {
            return dbHelper.registerClient(nombre, correo, contrasena, celular);
        }

}
