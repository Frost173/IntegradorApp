package com.cochera.miproyectointegrador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Estacionamiento;

import java.util.ArrayList;
import java.util.List;

public class EstacionamientoAdapter extends RecyclerView.Adapter<EstacionamientoAdapter.ViewHolder> {

    private List<Estacionamiento> lista;           // Lista actual (filtrada)
    private List<Estacionamiento> listaOriginal;   // Lista completa original
    private Context context;
    private int usuarioId;

    public EstacionamientoAdapter(Context context, List<Estacionamiento> lista, int usuarioId) {
        this.context = context;
        this.lista = new ArrayList<>(lista);
        this.listaOriginal = new ArrayList<>(lista);
        this.usuarioId = usuarioId;
    }

    @NonNull
    @Override
    public EstacionamientoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_estacionamiento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstacionamientoAdapter.ViewHolder holder, int position) {
        Estacionamiento estacionamiento = lista.get(position);
        holder.tvNombre.setText(estacionamiento.getNombre());
        holder.tvDireccion.setText(estacionamiento.getDireccion());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), activity_control_espacios.class);
            intent.putExtra("estacionamientoId", estacionamiento.getEstacionamientoId());
            intent.putExtra("usuarioId", usuarioId);
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Método para filtrar lista en tiempo real
    public void filtrarLista(String texto) {
        texto = texto.toLowerCase();
        List<Estacionamiento> listaFiltrada = new ArrayList<>();
        for (Estacionamiento e : listaOriginal) {
            if (e.getNombre().toLowerCase().contains(texto)) {
                listaFiltrada.add(e);
            }
        }
        lista = listaFiltrada;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreEstacionamiento);
            tvDireccion = itemView.findViewById(R.id.espaciosLibres);

        }
    }
}
