package com.example.pp01;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StorehouseActivity extends AppCompatActivity {
    private RecyclerView storehousesRecyclerView;
    private StorehouseAdapter storehouseAdapter;
    private ProgressBar progressBar;
    private LinearLayout mainContent;
    private EditText searchEditText;
    private List<Storehouse> originalStorehouseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storehouse_activity);

        storehousesRecyclerView = findViewById(R.id.storehouse_list);
        progressBar = findViewById(R.id.progressBar);
        mainContent = findViewById(R.id.mainContent);
        searchEditText = findViewById(R.id.search_edittext);

        storehousesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storehousesRecyclerView.setHasFixedSize(true);

        findViewById(R.id.back_button_layout).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        loadStorehouses();
        setupSearch();
    }

    private void filterStorehouses() {
        if (storehouseAdapter == null) return;

        String searchQuery = searchEditText.getText().toString().toLowerCase().trim();
        List<Storehouse> filteredList = new ArrayList<>();

        for (Storehouse storehouse : originalStorehouseList) {
            boolean searchMatches = searchQuery.isEmpty() ||
                    (storehouse.getName() != null && storehouse.getName().toLowerCase().contains(searchQuery));

            if (searchMatches) filteredList.add(storehouse);
        }
        storehouseAdapter.updateList(filteredList);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStorehouses();
            }
        });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mainContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void loadStorehouses() {
        showProgress(true);
        StorehouseContext.getStorehouses(new StorehouseContext.StorehousesCallback() {
            @Override
            public void onSuccess(List<Storehouse> storehouses) {
                runOnUiThread(() -> {
                    showProgress(false);
                    originalStorehouseList = storehouses;
                    storehouseAdapter = new StorehouseAdapter(storehouses);
                    storehousesRecyclerView.setAdapter(storehouseAdapter);
                });
            }
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(StorehouseActivity.this, "Ошибка загрузки цехов", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}