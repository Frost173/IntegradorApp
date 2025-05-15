package com.cochera.miproyectointegrador.Login;

public interface LoginContract {
    interface View {
        void goToAdminInterface();
        void goToClienteInterface(int usuarioId);
        void showLoginError(String message);
    }

    interface Presenter {
        void login(String correo, String contraseña);
    }
}

