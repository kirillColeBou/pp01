package com.example.pp01;

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
        holder.productName.setText(pack.getName());
        holder.productCategory.setText("Категория: " + pack.getCategoryName());
        holder.productWarehouse.setText("Склад: " + pack.getWarehouseName());
        holder.productQuantity.setText("Количество: " + pack.getQuantity());

        if (pack.getQrCode() != null && !pack.getQrCode().isEmpty()) {
            try {
                String base64Image = pack.getQrCode().startsWith("data:image") ?
                        pack.getQrCode().substring(pack.getQrCode().indexOf(",") + 1) : pack.getQrCode();
                byte[] qrCodeBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap qrBitmap = BitmapFactory.decodeByteArray(qrCodeBytes, 0, qrCodeBytes.length);
                holder.qrCodeImage.setImageBitmap(qrBitmap);
                holder.qrCodeImage.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                holder.qrCodeImage.setVisibility(View.GONE);
            }
        } else {
            holder.qrCodeImage.setVisibility(View.GONE);
        }
    }

    @Override public int getItemCount() { return packList.size(); }

    static class PackViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productCategory, productWarehouse, productQuantity;
        ImageView qrCodeImage;

        PackViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productWarehouse = itemView.findViewById(R.id.productWarehouse);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            qrCodeImage = itemView.findViewById(R.id.qrCodeImage);
        }
    }
}