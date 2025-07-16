package com.cochera.miproyectointegrador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private final Context context;
    private List<Reserva> reservaList;

    public ReservaAdapter(Context context, List<Reserva> reservaList) {
        this.context = context;
        this.reservaList = reservaList;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservaList.get(position);

        holder.tvPlaca.setText("Placa: " + safeText(reserva.getPlaca(), "N/A"));
        holder.tvTipoVehiculo.setText("Tipo: " + safeText(reserva.getTipoVehiculo(), "N/A"));
        holder.tvFecha.setText("Fecha: " + safeText(reserva.getFecha(), "N/A"));
        holder.tvHoraEntrada.setText("Entrada: " + safeText(reserva.getHoraEntrada(), "00:00"));
        holder.tvHoraSalida.setText("Salida: " + safeText(reserva.getHoraSalida(), "00:00"));
        holder.tvUbicacion.setText("Ubicación: " + safeText(reserva.getUbicacion(), "No definida"));
        holder.tvPagoTotal.setText(String.format("Pago: S/ %.2f", reserva.getPago()));
        holder.tvEstado.setText("Estado: " + safeText(reserva.getEstado(), "Pendiente"));

        // Ya se trajo concatenado desde SQL: nombre || ' ' || apellido AS nombreUsuario
        holder.tvUsuario.setText("Usuario: " + safeText(reserva.getNombreUsuario(), "No definido"));

        holder.tvEstacionamiento.setText("Estacionamiento: " + safeText(reserva.getNombreEstacionamiento(), "Desconocido"));
        holder.tvEspacio.setText("Espacio: " + safeText(reserva.getCodigoEspacio(), "N/A"));
    }

    @Override
    public int getItemCount() {
        return reservaList != null ? reservaList.size() : 0;
    }

    public void actualizarLista(List<Reserva> nuevasReservas) {
        this.reservaList = new ArrayList<>(nuevasReservas);
        notifyDataSetChanged();
    }

    static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaca, tvTipoVehiculo, tvHoraEntrada, tvHoraSalida, tvFecha, tvPagoTotal,
                tvUbicacion, tvEstado, tvUsuario, tvEstacionamiento, tvEspacio;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
            tvTipoVehiculo = itemView.findViewById(R.id.tvTipoVehiculo);
            tvHoraEntrada = itemView.findViewById(R.id.tvHoraEntrada);
            tvHoraSalida = itemView.findViewById(R.id.tvHoraSalida);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvPagoTotal = itemView.findViewById(R.id.tvPagoTotal);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvEstacionamiento = itemView.findViewById(R.id.tvEstacionamiento);
            tvEspacio = itemView.findViewById(R.id.tvEspacio);
        }
    }

    private String safeText(String text, String defaultText) {
        return (text != null && !text.trim().isEmpty()) ? text : defaultText;
    }
}
