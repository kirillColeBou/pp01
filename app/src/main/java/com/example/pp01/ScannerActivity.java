package com.example.pp01;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.journeyapps.barcodescanner.BarcodeView;

public class ScannerActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private BarcodeView barcodeView;
    private TextView resultText;
    private ImageView scannerLine;
    private ObjectAnimator scanLineAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);

        barcodeView = findViewById(R.id.barcode_view);
        resultText = findViewById(R.id.result_text);
        scannerLine = findViewById(R.id.scanner_line);
        Button scanButton = findViewById(R.id.scan_button);

        setupScanLineAnimation();

        scanButton.setOnClickListener(v -> scanQRCode());

        checkCameraPermission();
    }

    private void setupScanLineAnimation() {
        scanLineAnimator = ObjectAnimator.ofFloat(scannerLine, "translationY", -150f, 150f);
        scanLineAnimator.setDuration(2000);
        scanLineAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scanLineAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scanLineAnimator.start();
    }

    private void checkCameraPermission() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            resultText.setText("Камера не поддерживается");
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        try {
            barcodeView.resume();
            setupAutoScanner();
        } catch (Exception e) {
            resultText.setText("Ошибка камеры");
            Toast.makeText(this, "Не удалось запустить камеру", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAutoScanner() {
        barcodeView.decodeContinuous(result -> {
            if (result.getText() != null) {
                runOnUiThread(() -> processResult(result.getText()));
            }
        });
    }

    private void scanQRCode() {
        barcodeView.decodeSingle(result -> {
            if (result.getText() != null) {
                processResult(result.getText());
            } else {
                resultText.setText("Не удалось распознать");
            }
        });
    }

    private void processResult(String text) {
        if (text.startsWith("package_id:")) {
            String packageId = text.substring("package_id:".length());
            resultText.setText("ID: " + packageId);
            Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
        } else {
            resultText.setText("Неверный формат");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                resultText.setText("Нет разрешения");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeView != null && hasCameraPermission()) {
            barcodeView.resume();
            if (scanLineAnimator != null) scanLineAnimator.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeView != null) barcodeView.pause();
        if (scanLineAnimator != null) scanLineAnimator.cancel();
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }
}