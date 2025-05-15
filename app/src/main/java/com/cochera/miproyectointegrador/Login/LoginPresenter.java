package com.cochera.miproyectointegrador.Login;

import android.content.Context;
import com.cochera.miproyectointegrador.DataBase.DBHelper;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private DBHelper dbHelper;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.dbHelper = new DBHelper(context);
    }

    @Override
    public void login(String correo, String contraseña) {
//        DBHelper.UsuarioData data = dbHelper.getUsuarioData(correo, contraseña);
//
//        if (data.perfilId == -1) {
//            // Credenciales incorrectas
//            view.showLoginError("Correo o contraseña incorrectos.");
//            return;
//        }
//
//        switch (data.perfilId) {
//            case 1: // Administrador
//                view.goToAdminInterface();
//                break;
//            case 2: // Cliente
//                view.goToClienteInterface(data.usuarioId);
//                break;
//            default:
//                view.showLoginError("Perfil de usuario no reconocido.");
//                break;
//        }
    }
}
