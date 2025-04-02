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

public class ProductActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private LinearLayout mainContent;
    private EditText searchEditText;
    private List<Product> originalProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        productsRecyclerView = findViewById(R.id.products_list);
        progressBar = findViewById(R.id.progressBar);
        mainContent = findViewById(R.id.mainContent);
        searchEditText = findViewById(R.id.search_edittext);

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setHasFixedSize(true);

        findViewById(R.id.back_button_layout).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        loadProducts();
        setupSearch();
    }

    private void filterProducts() {
        if (productAdapter == null) return;

        String searchQuery = searchEditText.getText().toString().toLowerCase().trim();
        List<Product> filteredList = new ArrayList<>();

        for (Product product : originalProductList) {
            boolean searchMatches = searchQuery.isEmpty() ||
                    (product.getName() != null && product.getName().toLowerCase().contains(searchQuery)) ||
                    (product.getNz() != null && product.getNz().toLowerCase().contains(searchQuery)) ||
                    (product.getAlias() != null && product.getAlias().toLowerCase().contains(searchQuery));

            if (searchMatches) filteredList.add(product);
        }
        productAdapter.updateList(filteredList);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }
        });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mainContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void loadProducts() {
        showProgress(true);
        ProductContext.getProducts(new ProductContext.ProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                runOnUiThread(() -> {
                    showProgress(false);
                    originalProductList = products;
                    productAdapter = new ProductAdapter(products);
                    productsRecyclerView.setAdapter(productAdapter);
                });
            }
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(ProductActivity.this, "Ошибка загрузки изделий", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}