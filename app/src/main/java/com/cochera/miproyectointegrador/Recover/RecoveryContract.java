package com.cochera.miproyectointegrador.Recover;

public interface RecoveryContract {
    interface View {
        void showEmailError(String error);
        void showPasswordError(String error);
        void navigateToNewPasswordScreen(String email);
        void navigateToLogin();
        void showProgress();
        void hideProgress();
        void showSuccessMessage(String message);
        void showErrorMessage(String message);
    }

    interface Presenter {
        void validateEmail(String email);
        void updatePassword(String email, String newPassword, String confirmPassword);
    }

}
