package com.example.pp01;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StorehouseActivity extends AppCompatActivity {
    private RecyclerView storehousesRecyclerView;
    private StorehouseAdapter storehouseAdapter;
    private LinearLayout addButton;
    private LinearLayout backButton;
    private EditText searchEditText;
    private List<Storehouse> storehouseList = new ArrayList<>();
    private List<Storehouse> filteredStorehouseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storehouse_activity);
        storehousesRecyclerView = findViewById(R.id.storehouse_list);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.back_button_layout);
        searchEditText = findViewById(R.id.search_edittext);
        storehousesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storehouseAdapter = new StorehouseAdapter(filteredStorehouseList, new StorehouseAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Storehouse storehouse = filteredStorehouseList.get(position);
                int originalPosition = storehouseList.indexOf(storehouse);
                showAddEditDialog(storehouse, originalPosition);
            }

            @Override
            public void onDeleteClick(int position) {
                Storehouse storehouse = filteredStorehouseList.get(position);
                int originalPosition = storehouseList.indexOf(storehouse);
                showDeleteDialog(originalPosition);
            }
        });
        storehousesRecyclerView.setAdapter(storehouseAdapter);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(StorehouseActivity.this, MainActivity.class));
            finish();
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStorehouses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        addButton.setOnClickListener(v -> showAddEditDialog(null, -1));
        loadStorehouses();
    }

    private void filterStorehouses(String query) {
        filteredStorehouseList.clear();
        if (query.isEmpty()) {
            filteredStorehouseList.addAll(storehouseList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Storehouse storehouse : storehouseList) {
                if (storehouse.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredStorehouseList.add(storehouse);
                }
            }
        }
        storehouseAdapter.notifyDataSetChanged();
    }

    private void loadStorehouses() {
        StorehouseContext.getStorehouses(new StorehouseContext.StorehousesCallback() {
            @Override
            public void onSuccess(List<Storehouse> storehouses) {
                runOnUiThread(() -> {
                    storehouseList.clear();
                    storehouseList.addAll(storehouses);
                    filterStorehouses(searchEditText.getText().toString());
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(StorehouseActivity.this, error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showAddEditDialog(Storehouse storehouse, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_storehouse, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.nameInput);
        if (storehouse != null) {
            nameInput.setText(storehouse.getName());
        }

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                if (storehouse == null) {
                    createStorehouse(name);
                } else {
                    updateStorehouse(storehouse.getId(), name, position);
                }
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.create().show();
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Удаление")
                .setMessage("Вы уверены, что хотите удалить этот цех?")
                .setPositiveButton("Удалить", (dialog, which) ->
                        deleteStorehouse(storehouseList.get(position).getId(), position))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void createStorehouse(String name) {
        StorehouseContext.createStorehouse(name, new StorehouseContext.StorehouseOperationCallback() {
            @Override
            public void onSuccess(Storehouse storehouse) {
                runOnUiThread(() -> {
                    storehouseList.add(storehouse);
                    filterStorehouses(searchEditText.getText().toString());
                    Toast.makeText(StorehouseActivity.this, "Цех добавлен", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(StorehouseActivity.this, error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateStorehouse(int id, String newName, int position) {
        StorehouseContext.updateStorehouse(id, newName, new StorehouseContext.StorehouseOperationCallback() {
            @Override
            public void onSuccess(Storehouse updatedStorehouse) {
                runOnUiThread(() -> {
                    storehouseList.set(position, updatedStorehouse);
                    filterStorehouses(searchEditText.getText().toString());
                    Toast.makeText(StorehouseActivity.this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(StorehouseActivity.this, error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void deleteStorehouse(int id, int position) {
        StorehouseContext.deleteStorehouse(id, new StorehouseContext.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    storehouseList.remove(position);
                    filterStorehouses(searchEditText.getText().toString());
                    Toast.makeText(StorehouseActivity.this, "Цех удален", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(StorehouseActivity.this, error, Toast.LENGTH_SHORT).show());
            }
        });
    }
}