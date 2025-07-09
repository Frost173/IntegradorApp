package com.cochera.miproyectointegrador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int TIPO_ENVIADO = 1;
    private static final int TIPO_RECIBIDO = 2;

    private final List<Mensaje> mensajes;
    private final String uidUsuario;

    public MessageAdapter(List<Mensaje> mensajes, String uidUsuario) {
        this.mensajes = mensajes;
        this.uidUsuario = uidUsuario;
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);
        return mensaje.getEmisor().equals(uidUsuario) ? TIPO_ENVIADO : TIPO_RECIBIDO;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == TIPO_ENVIADO ? R.layout.item_mensaje_enviado : R.layout.item_mensaje_recibido,
                parent,
                false
        );
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        // Mostrar texto (si existe)
        if (mensaje.getTexto() != null && !mensaje.getTexto().isEmpty()) {
            holder.tvTexto.setVisibility(View.VISIBLE);
            holder.tvTexto.setText(mensaje.getTexto());
        } else {
            holder.tvTexto.setVisibility(View.GONE);
        }

        // Mostrar imagen (si existe)
        if (mensaje.getUrlImagen() != null && !mensaje.getUrlImagen().isEmpty()) {
            holder.imgMensaje.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(mensaje.getUrlImagen())
                    .into(holder.imgMensaje);
        } else {
            holder.imgMensaje.setVisibility(View.GONE);
        }

        // Mostrar hora
        String hora = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date(mensaje.getTimestamp()));
        holder.tvHora.setText(hora);

        // Mostrar autor si existe (solo para mensajes recibidos)
        if (holder.tvUsuario != null) {
            holder.tvUsuario.setText(mensaje.getAutor());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsuario, tvTexto, tvHora;
        ImageView imgMensaje;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTexto);
            tvHora = itemView.findViewById(R.id.tvHora);
            imgMensaje = itemView.findViewById(R.id.imgMensaje);

            // Solo existe en mensajes recibidos
            try {
                tvUsuario = itemView.findViewById(R.id.tvUsuario);
            } catch (Exception e) {
                tvUsuario = null;
            }
        }
    }
}
