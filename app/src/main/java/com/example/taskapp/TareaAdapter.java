package com.example.taskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {
    private List<Tarea> listaDeTareas;

    public TareaAdapter(List<Tarea> listaDeTareas) {
        this.listaDeTareas = listaDeTareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_card_layout, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaDeTareas.get(position);
        holder.nombreTarea.setText(tarea.getTitulo());
        holder.descripcionTarea.setText(tarea.getDescripcion());
        holder.horaTarea.setText(tarea.getHora());
        holder.statusTarea.setText(tarea.getStatus());

        holder.statusTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Tarea tarea = listaDeTareas.get(adapterPosition);

                    String estadoActual = tarea.getStatus();
                    String nuevoEstado = obtenerSiguienteEstado(estadoActual);
                    tarea.setStatus(nuevoEstado);

                    holder.statusTarea.setText(nuevoEstado);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeTareas.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTarea;
        TextView descripcionTarea;
        TextView horaTarea;
        TextView statusTarea;

        public TareaViewHolder(View itemView) {
            super(itemView);
            nombreTarea = itemView.findViewById(R.id.edtTituloTarea);
            descripcionTarea = itemView.findViewById(R.id.edtDescripcionTarea);
            horaTarea = itemView.findViewById(R.id.edtHoraTarea);
            statusTarea = itemView.findViewById(R.id.edtStatus);
        }
    }

    private String obtenerSiguienteEstado(String estadoActual) {
        if ("En espera".equals(estadoActual)) {
            return "En Curso";
        } else if ("En Curso".equals(estadoActual)) {
            return "Completado";
        } else {
            return "En espera";
        }
    }
}