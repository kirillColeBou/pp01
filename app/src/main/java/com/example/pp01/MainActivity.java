package com.example.pp01;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout categoriesContainer;
    private RecyclerView packRecyclerView;
    private PackAdapter packAdapter;
    private ProgressBar progressBar;
    private LinearLayout mainContent;
    private EditText searchEditText;
    private List<Pack> originalPackList = new ArrayList<>();
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        categoriesContainer = findViewById(R.id.categories_container);
        packRecyclerView = findViewById(R.id.products_list);
        progressBar = findViewById(R.id.progressBar);
        mainContent = findViewById(R.id.mainContent);
        searchEditText = findViewById(R.id.search_edittext);

        packRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        packRecyclerView.setHasFixedSize(true);

        findViewById(R.id.menu_button).setOnClickListener(v -> showPopupMenu(v));
        loadCategories();
        loadPacks();
        setupSearch();
    }

    private void filterPacks() {
        if (packAdapter == null) return;

        String searchQuery = searchEditText.getText().toString().toLowerCase().trim();
        List<Pack> filteredList = new ArrayList<>();

        for (Pack pack : originalPackList) {
            boolean categoryMatches = selectedCategoryId == -1 || pack.getCategoryId() == selectedCategoryId;
            boolean searchMatches = searchQuery.isEmpty() || pack.getName().toLowerCase().contains(searchQuery);
            if (categoryMatches && searchMatches) filteredList.add(pack);
        }
        packAdapter.updateList(filteredList);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPacks();
            }
        });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mainContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void loadPacks() {
        showProgress(true);
        PackContext.getPack(new PackContext.PacksCallback() {
            @Override
            public void onSuccess(List<Pack> packs) {
                runOnUiThread(() -> {
                    showProgress(false);
                    originalPackList = packs;
                    packAdapter = new PackAdapter(packs);
                    packRecyclerView.setAdapter(packAdapter);
                });
            }
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showProgress(false);
                    Toast.makeText(MainActivity.this, "Ошибка загрузки товаров", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.popup_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_scanner) {
                startActivity(new Intent(this, ScannerActivity.class));
            } else if (item.getItemId() == R.id.menu_warehouse) {
                startActivity(new Intent(this, StorehouseActivity.class));
                finish();
            } else if (item.getItemId() == R.id.menu_product) {
                startActivity(new Intent(this, ProductActivity.class));
                finish();
            } else if (item.getItemId() == R.id.menu_logout) {
                startActivity(new Intent(this, AuthorizationActivity.class));
                finish();
            }
            return true;
        });
        popup.show();
    }

    private void loadCategories() {
        CategoryContext.getCategories(new CategoryContext.CategoriesCallback() {
            @Override
            public void onSuccess(JSONArray categories) {
                runOnUiThread(() -> {
                    try {
                        categoriesContainer.removeAllViews();
                        addCategoryButton("Все", -1);

                        for (int i = 0; i < categories.length(); i++) {
                            String categoryName = categories.getJSONObject(i).getString("name");
                            int categoryId = categories.getJSONObject(i).getInt("id");
                            addCategoryButton(categoryName, categoryId);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Ошибка обработки категорий", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ошибка загрузки категорий", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addCategoryButton(String categoryName, int categoryId) {
        Button button = new Button(this);
        button.setTag(categoryId);
        button.setText(categoryName);
        button.setBackgroundResource(categoryId == -1 ? R.drawable.background_category_selected : R.drawable.background_category);
        button.setTextColor(ContextCompat.getColor(this, categoryId == -1 ? R.color.white : R.color.black));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginRight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15,
                getResources().getDisplayMetrics()
        );
        params.setMargins(0, 0, marginRight, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        button.setPadding(padding, padding/2, padding, padding/2);
        button.setLayoutParams(params);
        button.setOnClickListener(v -> {
            for (int i = 0; i < categoriesContainer.getChildCount(); i++) {
                View child = categoriesContainer.getChildAt(i);
                if (child instanceof Button) {
                    child.setBackgroundResource(R.drawable.background_category);
                    ((Button) child).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                }
            }
            v.setBackgroundResource(R.drawable.background_category_selected);
            ((Button) v).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            selectedCategoryId = (int) v.getTag();
            filterPacks();
        });
        categoriesContainer.addView(button);
    }
}