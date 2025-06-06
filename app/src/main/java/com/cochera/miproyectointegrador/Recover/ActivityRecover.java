package com.cochera.miproyectointegrador.Recover;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.R;


public class ActivityRecover extends AppCompatActivity implements RecoveryContract.View {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_email);

        dbHelper = new DBHelper(this);

        // Cargar el fragmento de email inicialmente
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new EmailFragment())
                .commit();
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public void showEmailError(String error) {

    }

    @Override
    public void showPasswordError(String error) {

    }

    @Override
    public void navigateToNewPasswordScreen(String email) {

    }

    @Override
    public void navigateToLogin() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showSuccessMessage(String message) {

    }

    @Override
    public void showErrorMessage(String message) {

    }
}