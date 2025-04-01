package com.example.pp01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoPackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopack_activity);
        Intent intent = getIntent();
        Pack pack = (Pack) intent.getSerializableExtra("pack");
        LinearLayout backButtonLayout = findViewById(R.id.back_button_layout);
        EditText packName = findViewById(R.id.packName);
        EditText productName = findViewById(R.id.productName);
        EditText category = findViewById(R.id.category);
        EditText warehouse = findViewById(R.id.warehouse);
        EditText quantity = findViewById(R.id.quantity);
        EditText numeration = findViewById(R.id.numeration);
        ImageView qrCodeImage = findViewById(R.id.qrCodeImage);
        if (pack != null) {
            packName.setText(pack.getName());
            productName.setText(pack.getProductName() != null ? pack.getProductName() : "не указан");
            category.setText(pack.getCategoryName() != null ? pack.getCategoryName() : "не указана");
            warehouse.setText(pack.getWarehouseName() != null ? pack.getWarehouseName() : "не указан");
            quantity.setText(String.valueOf(pack.getQuantity()));
            if (pack.getNumerationMin() != 0 || pack.getNumerationMax() != 0) {
                numeration.setText(pack.getNumerationMin() + " - " + pack.getNumerationMax());
            } else {
                numeration.setText("не указана");
            }
            if (pack.getQrCode() != null && !pack.getQrCode().isEmpty()) {
                try {
                    String base64Image = pack.getQrCode().startsWith("data:image") ?
                            pack.getQrCode().substring(pack.getQrCode().indexOf(",") + 1) : pack.getQrCode();
                    byte[] qrCodeBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap qrBitmap = BitmapFactory.decodeByteArray(qrCodeBytes, 0, qrCodeBytes.length);
                    qrCodeImage.setImageBitmap(qrBitmap);
                } catch (Exception e) {
                    qrCodeImage.setImageResource(R.drawable.box);
                }
            } else {
                qrCodeImage.setImageResource(R.drawable.box);
            }
        }
        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}