package com.example.pp01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StorehouseAdapter extends RecyclerView.Adapter<StorehouseAdapter.StorehouseViewHolder> {
    public List<Storehouse> storehouseList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public StorehouseAdapter(List<Storehouse> storehouseList, OnItemClickListener listener) {
        this.storehouseList = storehouseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StorehouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storehouse, parent, false);
        return new StorehouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StorehouseViewHolder holder, int position) {
        Storehouse storehouse = storehouseList.get(position);
        holder.storehouseName.setText(storehouse.getName());

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storehouseList.size();
    }

    static class StorehouseViewHolder extends RecyclerView.ViewHolder {
        TextView storehouseName;
        LinearLayout editButton, deleteButton;

        StorehouseViewHolder(@NonNull View itemView) {
            super(itemView);
            storehouseName = itemView.findViewById(R.id.storehouseName);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}