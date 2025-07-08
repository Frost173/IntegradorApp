package com.cochera.miproyectointegrador;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class PagoReservaActivity extends AppCompatActivity {

    private TextView txtMonto, txtNumero;
    private Button btnCopiar;
    private ImageView imagenQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_reserva);

        txtMonto = findViewById(R.id.txtMonto);
        txtNumero = findViewById(R.id.txtNumero);
        btnCopiar = findViewById(R.id.btnCopiar);
        imagenQR = findViewById(R.id.imagenQR);

        // Obtener monto si proviene de reserva
        double monto = getIntent().getDoubleExtra("monto", -1);

        if (monto >= 0) {
            txtMonto.setVisibility(View.VISIBLE);
            txtMonto.setText("Monto a pagar: S/ " + String.format("%.2f", monto));
        } else {
            txtMonto.setVisibility(View.GONE); // Si entraste desde el menú principal, sin monto
        }

        // Botón para copiar número al portapapeles
        btnCopiar.setOnClickListener(v -> {
            String numero = txtNumero.getText().toString().trim();
            if (!numero.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Número Yape", numero);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "📋 Número copiado al portapapeles", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Número vacío", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
