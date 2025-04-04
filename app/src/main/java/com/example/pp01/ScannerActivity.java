package com.example.pp01;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.journeyapps.barcodescanner.BarcodeView;
import java.util.List;

public class ScannerActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    private BarcodeView barcodeView;
    private TextView resultText;
    private ImageView scannerLine;
    private ObjectAnimator scanLineAnimator;
    private boolean isProcessingResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);

        barcodeView = findViewById(R.id.barcode_view);
        resultText = findViewById(R.id.result_text);
        scannerLine = findViewById(R.id.scanner_line);
        Button scanButton = findViewById(R.id.scan_button);
        ImageView backButton = findViewById(R.id.back_button);

        setupScanLineAnimation();

        scanButton.setOnClickListener(v -> scanQRCode());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ScannerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

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
        barcodeView.decodeSingle(result -> {
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
        if (isProcessingResult) return;
        if (text != null && !text.trim().isEmpty()) {
            isProcessingResult = true;
            String packageId = text.trim();
            findPackInSupabase(packageId);
        } else {
            resultText.setText("Пустой QR-код");
            Toast.makeText(this, "Не удалось распознать QR-код", Toast.LENGTH_SHORT).show();
        }
    }

    private void findPackInSupabase(String packId) {
        PackContext.findPackById(packId, new PackContext.PacksCallback() {
            @Override
            public void onSuccess(List<Pack> packs) {
                if (packs != null && !packs.isEmpty()) {
                    Pack foundPack = packs.get(0);
                    Intent intent = new Intent(ScannerActivity.this, InfoPackActivity.class);
                    intent.putExtra("pack", foundPack);
                    startActivity(intent);
                    finish();
                } else {
                    runOnUiThread(() -> {
                        resultText.setText("Упаковка не найдена");
                        Toast.makeText(ScannerActivity.this,
                                "Упаковка с ID " + packId + " не найдена",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    resultText.setText("Ошибка поиска");
                    Toast.makeText(ScannerActivity.this,
                            "Ошибка при поиске упаковки: " + error,
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
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
        isProcessingResult = false;
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