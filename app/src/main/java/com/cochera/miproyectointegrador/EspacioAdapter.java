package com.cochera.miproyectointegrador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Espacio;

import java.util.List;

//public class EspacioAdapter {
//}
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
        holder.tvCodigo.setText("Código: " + espacio.getCodigo());
        holder.tvEstado.setText("Estado: " + espacio.getEstado());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TarifarioActivity.class); // Reemplaza con la clase que quieras abrir
            intent.putExtra("estacionamientoid", espacio.getEstacionamientoId()); // Si tienes el ID en tu modelo
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
            //tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}