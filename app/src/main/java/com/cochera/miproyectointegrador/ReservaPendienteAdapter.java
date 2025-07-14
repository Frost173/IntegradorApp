package com.cochera.miproyectointegrador; // <-- Asegúrate que el paquete sea correcto

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Reserva;
import com.cochera.miproyectointegrador.R;

import java.util.List;

public class ReservaPendienteAdapter extends RecyclerView.Adapter<ReservaPendienteAdapter.ViewHolder> {

    private final List<Reserva> reservas;
    private final DBHelper dbHelper;
    private final Context context;

    public ReservaPendienteAdapter(List<Reserva> reservas, DBHelper dbHelper, Context context) {
        this.reservas = reservas;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva_pendiente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        holder.txtReservaInfo.setText("Reserva de " + reserva.getPlaca());
        holder.txtFechaHora.setText(reserva.getFecha() + " " + reserva.getHoraEntrada() + " - " + reserva.getHoraSalida());

        holder.btnAceptar.setOnClickListener(v -> {
            dbHelper.actualizarEstadoReserva(reserva.getReservaid(), "Confirmada");

            // Protección contra IndexOutOfBounds
            if (position >= 0 && position < reservas.size()) {
                reservas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, reservas.size()); // Actualiza correctamente
            }

            Toast.makeText(context, "Reserva confirmada", Toast.LENGTH_SHORT).show();
        });

        holder.btnRechazar.setOnClickListener(v -> {
            dbHelper.actualizarEstadoReserva(reserva.getReservaid(), "Cancelada");

            if (position >= 0 && position < reservas.size()) {
                reservas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, reservas.size());
            }

            Toast.makeText(context, "Reserva rechazada", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtReservaInfo, txtFechaHora;
        Button btnAceptar, btnRechazar;

        ViewHolder(View itemView) {
            super(itemView);
            txtReservaInfo = itemView.findViewById(R.id.txtReservaInfo);
            txtFechaHora = itemView.findViewById(R.id.txtFechaHora);
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
        }
    }
}
