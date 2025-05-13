package com.cochera.miproyectointegrador.Login;

public interface LoginContract {
    interface View {
        void goToAdminInterface();
        void goToClienteInterface();
        void showLoginError(String message);
    }
    interface Presenter {
        void login(String correo, String contrasena);
    }
}

