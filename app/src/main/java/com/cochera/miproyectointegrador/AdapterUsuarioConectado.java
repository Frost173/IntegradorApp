package com.cochera.miproyectointegrador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Usuario;
import com.cochera.miproyectointegrador.R;

import java.util.List;

public class AdapterUsuarioConectado extends RecyclerView.Adapter<AdapterUsuarioConectado.UsuarioViewHolder> {

    private Context context;
    private List<Usuario> listaUsuarios;

    public AdapterUsuarioConectado(Context context, List<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_conectado, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.nombreTextView.setText(usuario.getNombre());

        // Muestra estado con el círculo verde o gris
        if ("conectado".equalsIgnoreCase(usuario.getEstado())) {
            holder.estadoView.setBackgroundResource(R.drawable.circle_green);  // conectado
        } else {
            holder.estadoView.setBackgroundResource(R.drawable.circle_gray);   // desconectado
        }
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView nombreTextView;
        View estadoView;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tvNombreUsuario);
            estadoView = itemView.findViewById(R.id.viewEstado);
        }
    }
}
