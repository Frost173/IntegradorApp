package com.cochera.miproyectointegrador.Login;

import android.content.Context;

public class LoginPresenter implements LoginContract.Presenter {
    /*
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
    }*/

    //modificacion testing

    private LoginContract.View view;
    private LoginModel model;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.model = new LoginModel(context);
    }

    @Override
    public void login(String correo, String contraseña) {
        if (model.isAdmin(correo, contraseña)) {
            view.goToAdminInterface();
        } else {
            int clienteId = model.validateLogin(correo, contraseña);
            if (clienteId != -1) {
                view.goToClienteInterface(clienteId);
            } else {
                view.showLoginError("Correo o contraseña incorrectos.");
            }
        }
    }
}

