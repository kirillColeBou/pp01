package com.example.pp01;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoPackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopack_activity);
        TextView testTextView = findViewById(R.id.test);
        String qrContent = getIntent().getStringExtra("QR_CONTENT");
        if (qrContent != null) {
            testTextView.setText(qrContent);
        }
    }
}