package com.cochera.miproyectointegrador.Recover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cochera.miproyectointegrador.R;

public class NewPassword extends Fragment implements RecoveryContract.View {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private RecoveryPresenter presenter;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_password, container, false);

        etNewPassword = view.findViewById(R.id.et_new_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        presenter = new RecoveryPresenter(this, ((ActivityRecover)getActivity()).getDbHelper());

        // Obtener el email del bundle
        Bundle args = getArguments();
        if (args != null) {
            email = args.getString("email");
        }

        btnChangePassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            presenter.updatePassword(email, newPassword, confirmPassword);
        });

        return view;
    }

    @Override
    public void showPasswordError(String error) {
        etNewPassword.setError(error);
        etConfirmPassword.setError(error);
    }

    @Override
    public void navigateToLogin() {
        getActivity().finish();
    }

    // Otros métodos de la interfaz...
    @Override public void showEmailError(String error) {}
    @Override public void navigateToNewPasswordScreen(String email) {}
    @Override public void showProgress() { /* Mostrar progreso */ }
    @Override public void hideProgress() { /* Ocultar progreso */ }
    @Override public void showSuccessMessage(String message) { Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show(); }
    @Override public void showErrorMessage(String message) { Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show(); }
}