package com.cochera.miproyectointegrador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int TIPO_ENVIADO = 1;
    private static final int TIPO_RECIBIDO = 2;

    private List<Mensaje> mensajes;
    private String uidUsuario;

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
        View view;
        if (viewType == TIPO_ENVIADO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje_enviado, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje_recibido, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        holder.tvTexto.setText(mensaje.getTexto());

        if (holder.tvUsuario != null) {
            holder.tvUsuario.setText(mensaje.getAutor());
        }

        if (holder.tvHora != null) {
            String horaFormateada = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(mensaje.getTimestamp()));
            holder.tvHora.setText(horaFormateada);
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsuario, tvTexto, tvHora;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTexto);
            tvUsuario = itemView.findViewById(R.id.tvUsuario); // Puede ser null si es mensaje enviado
            tvHora = itemView.findViewById(R.id.tvHora);       // Agregado para mostrar la hora
        }
    }
}
