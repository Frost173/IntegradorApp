package com.cochera.miproyectointegrador.Register;

import android.content.Context;

import androidx.leanback.widget.Presenter;

public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View view;
    private RegisterModel model;
    public RegisterPresenter(RegisterContract.View view, Context context) {
        this.view = view;
        this.model = new RegisterModel(context);
    }
    @Override
    public void register(String nombre, String correo, String contrasena, String celular) {
        if (model.register(nombre, correo, contrasena, celular)) {
            view.showRegisterSuccess();
        } else {
            view.showRegisterError("No se pudo registrar. ¿Correo o DNI ya registrados?");
        }
    }


}

