package com.example.taskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TareaAdapter extends FirestoreRecyclerAdapter<Tarea, TareaAdapter.TareaViewHolder> {

    public TareaAdapter(@NonNull FirestoreRecyclerOptions<Tarea> options) {
        super(options);
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_card_layout, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TareaViewHolder holder, int position, @NonNull Tarea tarea) {
        holder.bind(tarea);
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

        public void bind(Tarea tarea) {
            nombreTarea.setText(tarea.getTitulo());
            descripcionTarea.setText(tarea.getDescripcion());
            horaTarea.setText(tarea.getHora());
            statusTarea.setText(tarea.getStatus());

            itemView.setOnClickListener(view -> {
                String nuevoEstado = obtenerNuevoEstado(tarea.getStatus());

                cambiarEstadoTarea(getAdapterPosition(), nuevoEstado);
            });
        }
    }

    private String obtenerNuevoEstado(String estadoActual) {
        switch (estadoActual) {
            case "Activo":
                return "En espera";
            case "En espera":
                return "Terminado";
            case "Terminado":
                return "Activo";
            default:
                return "Activo";
        }
    }

    private void cambiarEstadoTarea(int position, String nuevoEstado) {
        getSnapshots().getSnapshot(position).getReference().update("status", nuevoEstado);
    }
}
