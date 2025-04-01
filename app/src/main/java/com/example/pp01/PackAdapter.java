package com.example.pp01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PackAdapter extends RecyclerView.Adapter<PackAdapter.PackViewHolder> {
    private List<Pack> packList;

    public PackAdapter(List<Pack> packList) {
        this.packList = packList;
    }

    public void updateList(List<Pack> newList) {
        packList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public PackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new PackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackViewHolder holder, int position) {
        Pack pack = packList.get(position);
        holder.productName.setText(pack.getProductName() != null ? pack.getProductName() : "Нет названия");
        holder.productPackName.setText(pack.getName() != null ? pack.getName() : "");
        holder.productCategory.setText("Категория: " + (pack.getCategoryName() != null ? pack.getCategoryName() : "не указана"));
        holder.productWarehouse.setText("Склад: " + (pack.getWarehouseName() != null ? pack.getWarehouseName() : "не указан"));
        holder.productQuantity.setText("Количество: " + pack.getQuantity());
        if (pack.getNumerationMin() != 0 || pack.getNumerationMax() != 0) {
            holder.productNumeration.setText("Нумерация: " + pack.getNumerationMin() + " - " + pack.getNumerationMax());
            holder.productNumeration.setVisibility(View.VISIBLE);
        } else {
            holder.productNumeration.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, InfoPackActivity.class);
                intent.putExtra("pack", pack);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }

    static class PackViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPackName, productCategory,
                productWarehouse, productQuantity, productNumeration;
        ImageView qrCodeImage;

        PackViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPackName = itemView.findViewById(R.id.productPackName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productWarehouse = itemView.findViewById(R.id.productWarehouse);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productNumeration = itemView.findViewById(R.id.productNumeration);
            qrCodeImage = itemView.findViewById(R.id.qrCodeImage);
            qrCodeImage.setImageResource(R.drawable.box);
        }
    }
}