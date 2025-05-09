package com.cochera.miproyectointegrador.Login;

public interface LoginContract {
    interface View {
        void showLoginSuccess(int clienteId, String nombre, String correo);
        void showLoginError(String message);
    }
    interface Presenter {
        void login(String correo, String contraseña);
    }
}

