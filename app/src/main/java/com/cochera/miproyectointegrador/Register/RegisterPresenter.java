package com.cochera.miproyectointegrador.Register;

import android.content.Context;
import com.cochera.miproyectointegrador.Register.RegisterContract;
import com.cochera.miproyectointegrador.Register.RegisterModel;
import androidx.leanback.widget.Presenter;

public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View view;
    private RegisterModel model;
    public RegisterPresenter(RegisterContract.View view, Context context) {
        this.view = view;
        this.model = new RegisterModel(context);
    }
    @Override
    public void registerUser(String nombre, String correo, String contraseña, String celular) {

    }
}

