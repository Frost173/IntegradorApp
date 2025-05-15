package com.cochera.miproyectointegrador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Reserva;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {

    List<Reserva> reservas;
    Context context;

    public ReservaAdapter(Context context, List<Reserva> reservas) {
        this.context = context;
        this.reservas = reservas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaca, tvHoraEntrada, tvHoraSalida, tvFecha, tvPagoHora, tvUbicacion;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
            tvHoraEntrada = itemView.findViewById(R.id.tvHoraEntrada);
            tvHoraSalida = itemView.findViewById(R.id.tvHoraSalida);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvPagoHora = itemView.findViewById(R.id.tvPagoHora);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
        }
    }

    @NonNull
    @Override
    public ReservaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaAdapter.ViewHolder holder, int position) {
        Reserva r = reservas.get(position);
        holder.tvPlaca.setText("Placa: " + r.placa);
        holder.tvHoraEntrada.setText("Hora Entrada: " + r.horaEntrada);
        holder.tvHoraSalida.setText("Hora Salida: " + r.horaSalida);
        holder.tvFecha.setText("Fecha: " + r.fecha);
        holder.tvPagoHora.setText("Pago/hora: S/" + r.pagoHora);
        holder.tvUbicacion.setText("Ubicación: " + r.ubicacion);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }
}