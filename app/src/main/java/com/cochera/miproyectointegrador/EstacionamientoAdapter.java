package com.cochera.miproyectointegrador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.DBHelper;
import com.cochera.miproyectointegrador.DataBase.Estacionamiento;

import java.util.List;

public class EstacionamientoAdapter extends RecyclerView.Adapter<EstacionamientoAdapter.ViewHolder> {
    private List<Estacionamiento> lista;
    private Context context;
    DBHelper dbHelper;
    public EstacionamientoAdapter(Context context, List<Estacionamiento> lista) {
        this.context = context;
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        public TextView direccionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreEstacionamiento);
            direccionTextView = itemView.findViewById(R.id.direccionEstacionamiento);

        }

    }

    @Override
    public EstacionamientoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_estacionamiento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EstacionamientoAdapter.ViewHolder holder, int position) {
        Estacionamiento estacionamiento = lista.get(position);
        holder.nombreTextView.setText(estacionamiento.getNombre());
        holder.direccionTextView.setText(estacionamiento.getDireccion());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, activity_control_espacios.class);
            intent.putExtra("estacionamientoid", estacionamiento.getEstacionamientoId());
            //dbHelper.insertarReserva(2, 1,1, "14/05/2025","14:00","16:00","Pendiente");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
