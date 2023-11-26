package com.example.taskapp;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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

        holder.btnEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar un diálogo de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("¿Estás seguro de que quieres eliminar esta tarea?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Si el usuario hace clic en "Sí", eliminar la tarea
                                eliminarTarea(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Si el usuario hace clic en "No", cerrar el diálogo
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTarea;
        TextView descripcionTarea;
        TextView horaTarea;
        TextView statusTarea;
        Button btnEliminarTarea;

        public TareaViewHolder(View itemView) {
            super(itemView);
            nombreTarea = itemView.findViewById(R.id.edtTituloTarea);
            descripcionTarea = itemView.findViewById(R.id.edtDescripcionTarea);
            horaTarea = itemView.findViewById(R.id.edtHoraTarea);
            statusTarea = itemView.findViewById(R.id.edtStatus);
            btnEliminarTarea = itemView.findViewById(R.id.btnEliminarTarea);
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
            case "Terminado":
                return "En espera";
            case "En espera":
                return "Activo";
            case "Activo":
                return "Terminado";
            default:
                return "En espera";
        }
    }

    private void cambiarEstadoTarea(int position, String nuevoEstado) {
        getSnapshots().getSnapshot(position).getReference().update("status", nuevoEstado);
    }

    void eliminarTarea(int position) {
        getSnapshots().getSnapshot(position).getReference().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Tarea eliminada exitosamente de Firebase
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al eliminar la tarea de Firebase
                    }
                });
    }
}
