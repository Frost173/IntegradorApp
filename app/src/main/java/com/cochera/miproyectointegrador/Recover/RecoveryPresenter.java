package com.cochera.miproyectointegrador.Recover;

import android.content.Context;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Usuario;

public class RecoveryPresenter implements RecoveryContract.Presenter {
    private RecoveryContract.View view;
    private DBHelper dbHelper;

    public RecoveryPresenter(RecoveryContract.View view, DBHelper dbHelper) {
        this.view = view;
        this.dbHelper = dbHelper;
    }

    @Override
    public void validateEmail(String email) {
        if (email.isEmpty()) {
            view.showEmailError("El correo electrónico no puede estar vacío");
            return;
        }

        // Verificar si el correo existe
        Usuario usuario = dbHelper.verificarUsuario(email, "");
        if (usuario == null) {
            view.showEmailError("Correo electrónico no registrado");
            return;
        }

        view.navigateToNewPasswordScreen(email);
    }

    @Override
    public void updatePassword(String email, String newPassword, String confirmPassword) {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            view.showPasswordError("Las contraseñas no pueden estar vacías");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            view.showPasswordError("Las contraseñas no coinciden");
            return;
        }

        view.showProgress();

        // Actualizar contraseña en la base de datos
        boolean success = dbHelper.actualizarContrasena(email, newPassword);

        view.hideProgress();

        if (success) {
            view.showSuccessMessage("Contraseña actualizada correctamente");
            view.navigateToLogin();
        } else {
            view.showErrorMessage("Error al actualizar la contraseña");
        }
    }

}


