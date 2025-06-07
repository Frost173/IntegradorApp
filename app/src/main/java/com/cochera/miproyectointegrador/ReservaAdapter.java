package com.cochera.miproyectointegrador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Reserva;
import com.cochera.miproyectointegrador.R;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {

    private List<Reserva> reservas;  // Lista mutable para actualizar
    private final Context context;

    public ReservaAdapter(Context context, List<Reserva> reservas) {
        this.context = context;
        this.reservas = reservas;
    }

    public void setListaReservas(List<Reserva> nuevasReservas) {
        this.reservas = nuevasReservas;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaca, tvHoraEntrada, tvHoraSalida, tvFecha, tvPagoHora, tvUbicacion, tvReservaDetalle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
            tvHoraEntrada = itemView.findViewById(R.id.tvHoraEntrada);
            tvHoraSalida = itemView.findViewById(R.id.tvHoraSalida);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvPagoHora = itemView.findViewById(R.id.tvPagoHora);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvReservaDetalle = itemView.findViewById(R.id.tvReservaDetalle);
        }
    }

    @NonNull
    @Override
    public ReservaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReservaAdapter.ViewHolder holder, int position) {
        if (reservas == null || reservas.isEmpty()) return;

        Reserva r = reservas.get(position);

        holder.tvPlaca.setText("Placa: " + safeString(r.getPlaca()));
        holder.tvHoraEntrada.setText("Hora Entrada: " + safeString(r.getHoraEntrada()));
        holder.tvHoraSalida.setText("Hora Salida: " + safeString(r.getHoraSalida()));
        holder.tvFecha.setText("Fecha: " + safeString(r.getFecha()));
        holder.tvPagoHora.setText("Pago/hora: S/" + r.getPagoHora());
        holder.tvUbicacion.setText("Ubicación: " + safeString(r.getUbicacion()));

        String detalle = "Reserva registrada correctamente para el vehículo con placa "
                + safeString(r.getPlaca()) + " en " + safeString(r.getUbicacion()) + ".";
        holder.tvReservaDetalle.setText(detalle);
        holder.tvReservaDetalle.setVisibility(View.VISIBLE);

        // Si quieres agregar listener de click en el ítem, descomenta y adapta:
        /*
        holder.itemView.setOnClickListener(v -> {
            // Código para manejar click, e.g. mostrar detalles o editar reserva
        });
        */
    }

    @Override
    public int getItemCount() {
        return (reservas != null) ? reservas.size() : 0;
    }

    // Método auxiliar para evitar nulls y mostrar string vacío
    private String safeString(String s) {
        return s != null ? s : "";
    }
}

