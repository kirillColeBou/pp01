package com.example.pp01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StorehouseAdapter extends RecyclerView.Adapter<StorehouseAdapter.StorehouseViewHolder> {
    private List<Storehouse> storehouseList;

    public StorehouseAdapter(List<Storehouse> storehouseList) {
        this.storehouseList = storehouseList;
    }

    public void updateList(List<Storehouse> newList) {
        storehouseList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public StorehouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storehouse, parent, false);
        return new StorehouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StorehouseViewHolder holder, int position) {
        Storehouse storehouse = storehouseList.get(position);
        holder.storehouseName.setText(storehouse.getName() != null ? storehouse.getName() : "Нет названия");
        holder.storehouseImage.setImageResource(R.drawable.storehouse);
    }

    @Override
    public int getItemCount() {
        return storehouseList.size();
    }

    static class StorehouseViewHolder extends RecyclerView.ViewHolder {
        TextView storehouseName;
        ImageView storehouseImage;

        StorehouseViewHolder(@NonNull View itemView) {
            super(itemView);
            storehouseName = itemView.findViewById(R.id.storehouseName);
            storehouseImage = itemView.findViewById(R.id.storehouseImage);
        }
    }
}