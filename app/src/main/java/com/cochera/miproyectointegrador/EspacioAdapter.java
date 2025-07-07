package com.cochera.miproyectointegrador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Espacio;

import java.util.List;

public class EspacioAdapter extends RecyclerView.Adapter<EspacioAdapter.ViewHolder> {
    private Context context;
    private List<Espacio> listaEspacios;

    public EspacioAdapter(Context context, List<Espacio> listaEspacios) {
        this.context = context;
        this.listaEspacios = listaEspacios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_espacio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Espacio espacio = listaEspacios.get(position);

        holder.tvCodigo.setText(espacio.getUbicacion()); // A1, B2, etc.
        holder.tvEstado.setText(espacio.getEstado());

        // Cambiar color de fondo del badge según estado
        int colorEstado;
        switch (espacio.getEstado()) {
            case "Disponible":
                colorEstado = context.getResources().getColor(R.color.estado_disponible);
                break;
            case "Reservado":
                colorEstado = context.getResources().getColor(R.color.estado_reservado);
                break;
            case "Ocupado":
                colorEstado = context.getResources().getColor(R.color.estado_ocupado);
                break;
            default:
                colorEstado = context.getResources().getColor(android.R.color.darker_gray);
                break;
        }

        // Aplica el color al fondo del badge (TextView tvEstado)
        holder.tvEstado.getBackground().setTint(colorEstado);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Estacionamiento: " + espacio.getEstacionamientoId(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, TarifarioActivity.class);
            intent.putExtra("estacionamientoid", espacio.getEstacionamientoId());
            intent.putExtra("espacioid", espacio.getEspacioId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return listaEspacios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }

}
