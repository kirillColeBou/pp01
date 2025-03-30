package com.example.pp01;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AuthorizationActivity extends AppCompatActivity {
    EditText etUsername;
    EditText etPassword;
    UserContext userContext = new UserContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.authorization_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
    }

    public void onAuthorization(View view){
        String username_str = etUsername.getText().toString();
        String password_str = etPassword.getText().toString();
        UserContext.checkUserCredentials(username_str, password_str, null, new UserContext.Callback() {
            @Override
            public void onSuccess(boolean userExists) {
                runOnUiThread(() -> {
                    if (userExists) {
                        setContentView(R.layout.main_activity);
                    } else {
                        Toast.makeText(AuthorizationActivity.this,
                                "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                       Log.d("Error", error));
            }
        });
    }


}