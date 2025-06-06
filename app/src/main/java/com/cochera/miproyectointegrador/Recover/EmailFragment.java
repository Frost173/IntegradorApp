package com.cochera.miproyectointegrador.Recover;

import android.os.Bundle;

import com.cochera.miproyectointegrador.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmailFragment extends Fragment implements RecoveryContract.View {
    private EditText etEmail;
    private Button btnContinue;
    private RecoveryPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_email, container, false);

        etEmail = view.findViewById(R.id.et_email);
        btnContinue = view.findViewById(R.id.btn_continue);

        presenter = new RecoveryPresenter(this, ((ActivityRecover)getActivity()).getDbHelper());

        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            presenter.validateEmail(email);
        });

        return view;
    }

    @Override
    public void showEmailError(String error) {
        etEmail.setError(error);
    }

    @Override
    public void navigateToNewPasswordScreen(String email) {
        NewPassword fragment = new NewPassword();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Otros métodos de la interfaz...
    @Override public void showPasswordError(String error) {}
    @Override public void navigateToLogin() { getActivity().finish(); }
    @Override public void showProgress() { /* Mostrar progreso */ }
    @Override public void hideProgress() { /* Ocultar progreso */ }
    @Override public void showSuccessMessage(String message) { Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show(); }
    @Override public void showErrorMessage(String message) { Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show(); }
}