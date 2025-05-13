package com.cochera.miproyectointegrador.Login;

import android.content.Context;

import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class LoginPresenter implements LoginContract.Presenter {

    /*
=======
>>>>>>> 70cf38bf16dac277ecf77023b818e34e2e8818c4
    private LoginContract.View view;
    private LoginModel model;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.model = new LoginModel(context);
    }

    @Override
    public void login(String correo, String contraseña) {
        int clienteId = model.validateLogin(correo, contraseña);
        if (clienteId != -1) {
            String nombre = model.getNombreCliente(clienteId);
            String correoReal = model.getCorreoCliente(clienteId);
            view.showLoginSuccess(clienteId, nombre, correoReal);
        } else {
            view.showLoginError("Correo o contraseña incorrectos.");
        }
<<<<<<< HEAD
    }*/

    //modificacion testing

    private LoginContract.View view;
    private DBHelper dbHelper;


    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.dbHelper = new DBHelper(context);
    }

    @Override
    public void login(String correo, String contrasena) {
        if (dbHelper.isAdmin(correo, contrasena)) {
            view.goToAdminInterface(); // Redirige a Tarifario
        } else if (dbHelper.isCliente(correo, contrasena)) {
            view.goToClienteInterface();
        } else {
            view.showLoginError("Credenciales incorrectas");
        }
    }
}

