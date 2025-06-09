package com.cochera.miproyectointegrador;

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

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private Context context;
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
        Reserva reserva = reservaList.get(position); // 🔁 CORREGIDO

        // Mostrar placa
        holder.tvPlaca.setText("Placa: " + reserva.getPlaca());

        // Mostrar tipo de vehículo
        holder.tvTipoVehiculo.setText("Tipo: " + reserva.getTipoVehiculo());

        // Mostrar fecha y hora
        holder.tvFecha.setText("Fecha: " + reserva.getFecha());
        holder.tvHoraEntrada.setText("Entrada: " + reserva.getHoraEntrada());
        holder.tvHoraSalida.setText("Salida: " + reserva.getHoraSalida());

        // Mostrar ubicación (si existe en el layout)
        if (holder.tvUbicacion != null && reserva.getUbicacion() != null) {
            holder.tvUbicacion.setText("Ubicación: " + reserva.getUbicacion());
        }

        // Mostrar pago total
        holder.tvPagoTotal.setText("Pago: S/ " + reserva.getPago());

        // Mostrar nombre y apellido del usuario (si existe en el layout)
        if (holder.tvUsuario != null) {
            holder.tvUsuario.setText("Usuario: " + reserva.getNombreUsuario() + " " + reserva.getApellidoUsuario());
        }
    }

    @Override
    public int getItemCount() {
        return reservaList.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaca, tvTipoVehiculo, tvHoraEntrada, tvHoraSalida, tvFecha, tvPagoTotal,
                tvUbicacion, tvEstado, tvReservaDetalle, tvUsuario; //  CORREGIDO: tvUsuario agregado

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
            tvReservaDetalle = itemView.findViewById(R.id.tvReservaDetalle);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
        }
    }
}

