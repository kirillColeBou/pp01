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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void updateList(List<Product> newList) {
        productList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName() != null ? product.getName() : "Нет названия");
        holder.productNz.setText("НЗ: " + (product.getNz() != null ? product.getNz() : "не указан"));
        holder.productAlias.setText("Алиас: " + (product.getAlias() != null ? product.getAlias() : "не указан"));
        holder.productType.setText("Тип: " + (product.getTypeName() != null ? product.getTypeName() : "не указан"));

        if (product.getImg() != null && !product.getImg().isEmpty()) {
            byte[] decodedString = Base64.decode(product.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.productImage.setImageBitmap(decodedByte);
        } else {
            holder.productImage.setImageResource(R.drawable.box);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productNz, productAlias, productType;
        ImageView productImage;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productNz = itemView.findViewById(R.id.productNz);
            productAlias = itemView.findViewById(R.id.productAlias);
            productType = itemView.findViewById(R.id.productType);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}