package com.example.pp01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthorizationActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_activity);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
    }

    public void onAuthorization(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = md5(password);
        UserContext.checkUserCredentials(username, hashedPassword, new UserContext.Callback() {
            @Override
            public void onSuccess(boolean userExists) {
                runOnUiThread(() -> {
                    if (userExists) {
                        startActivity(new Intent(AuthorizationActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AuthorizationActivity.this,
                                "Неверные учетные данные", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(AuthorizationActivity.this,
                                "Ошибка соединения", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String md5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}