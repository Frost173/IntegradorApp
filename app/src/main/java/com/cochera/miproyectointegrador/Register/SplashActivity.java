package com.cochera.miproyectointegrador.Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.cochera.miproyectointegrador.MainActivity;
import com.cochera.miproyectointegrador.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cambia MainActivity por la actividad principal de tu app
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000); // 3 segundos
    }
}
