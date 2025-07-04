package com.cochera.miproyectointegrador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cochera.miproyectointegrador.DataBase.Usuario;

import java.util.List;

public class AdapterChatUsuario extends RecyclerView.Adapter<AdapterChatUsuario.ViewHolder> {

    private final List<ChatUsuario> listaChats;
    private final Context context;

    public AdapterChatUsuario(Context context, List<ChatUsuario> listaChats) {
        this.context = context;
        this.listaChats = listaChats;
    }

    @NonNull
    @Override
    public AdapterChatUsuario.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChatUsuario.ViewHolder holder, int position) {
        ChatUsuario chatUsuario = listaChats.get(position);
        Usuario usuario = chatUsuario.getUsuario();

        holder.tvNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "Sin nombre");
        holder.tvUltimoMensaje.setText(chatUsuario.getUltimoMensaje());
        holder.tvHora.setText(chatUsuario.getHora());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("uidDestino", usuario.getUid());
            intent.putExtra("nombreDestino", usuario.getNombre());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaChats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvUltimoMensaje, tvHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvUltimoMensaje = itemView.findViewById(R.id.tvUltimoMensaje);
            tvHora = itemView.findViewById(R.id.tvHora);
        }
    }
}

