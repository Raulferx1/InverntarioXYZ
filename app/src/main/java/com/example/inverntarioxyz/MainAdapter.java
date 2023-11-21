package com.example.inverntarioxyz;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
        holder.nombre.setText(model.getNombre());
        holder.marca.setText(model.getMarca());
        holder.descripcion.setText(model.getDescripcion());

        Glide.with(holder.img.getContext())
                .load(model.getImgURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(holder.img.getContext());
                final View view = LayoutInflater.from(holder.img.getContext()).inflate(R.layout.ventana_emergente, null);

                builder.setView(view);
                final AlertDialog alertDialog = builder.create();

                final EditText nombreEditText = view.findViewById(R.id.nombreText);
                final EditText marcaEditText = view.findViewById(R.id.marcaText);
                final EditText descripcionText = view.findViewById(R.id.descripcionText);
                final EditText imageURLText = view.findViewById(R.id.img1Text);

                Button actualizarBtn = view.findViewById(R.id.btn_actualizar);

                nombreEditText.setText(model.getNombre());
                marcaEditText.setText(model.getMarca());
                descripcionText.setText(model.getDescripcion());
                imageURLText.setText(model.getImgURL());

                alertDialog.show();

                actualizarBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nuevoNombre = nombreEditText.getText().toString();
                        String nuevoMarca = marcaEditText.getText().toString();
                        String nuevaDescripcion = descripcionText.getText().toString();
                        String nuevaImageURL = imageURLText.getText().toString();

                        if (!nuevoNombre.equals(model.getNombre()) || !nuevoMarca.equals(model.getMarca())
                                || !nuevaDescripcion.equals(model.getDescripcion()) || !nuevaImageURL.equals(model.getImgURL())) {

                            Map<String, Object> map = new HashMap<>();
                            map.put("Nombre", nuevoNombre);
                            map.put("Marca", nuevoMarca);
                            map.put("Descripcion", nuevaDescripcion);
                            map.put("imgURL", nuevaImageURL);

                            FirebaseDatabase.getInstance().getReference().child("Programación Android")
                                    .child(getRef(position).getKey()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.nombre.getContext(), "Actualización Correcta", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.nombre.getContext(), "Error en la actualización", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    });
                        } else {
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nombre.getContext());
                builder.setTitle("¿Estás seguro de eliminarlo?");
                builder.setMessage("Eliminado");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Programación Android")
                                .child(getRef(position).getKey()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nombre.getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();

                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.nombre.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.nombre.getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView nombre, marca, descripcion;
        Button editar, eliminar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            nombre = itemView.findViewById(R.id.nombreText);
            marca = itemView.findViewById(R.id.marcaText);
            descripcion = itemView.findViewById(R.id.descripcionText);

            editar = itemView.findViewById(R.id.btn_edit);
            eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}