package com.example.pp01;

import java.io.Serializable;

public class Pack implements Serializable {
    private int id;
    private String name;
    private int id_category;
    private int count;
    private int id_storehouse;
    private String QR;
    private int numeration_min;
    private int numeration_max;
    private int id_product;
    private String id_converted;
    private String categoryName;
    private String warehouseName;
    private String productName;


    public Pack(int id, String name, int id_category, int count, int id_storehouse, String QR, int numeration_min, int numeration_max, int id_product, String id_converted) {
        this.id = id;
        this.name = name;
        this.id_category = id_category;
        this.count = count;
        this.id_storehouse = id_storehouse;
        this.QR = QR;
        this.numeration_min = numeration_min;
        this.numeration_max = numeration_max;
        this.id_product = id_product;
        this.id_converted = id_converted;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return id_category; }
    public int getQuantity() { return count; }
    public int getWarehouseId() { return id_storehouse; }
    public String getQrCode() { return QR; }
    public int getNumerationMin() { return numeration_min; }
    public int getNumerationMax() { return numeration_max; }
    public int getProductId() { return id_product; }
    public String getIdConverted() { return id_converted; }
    public String getCategoryName() { return categoryName; }
    public String getWarehouseName() { return warehouseName; }
    public String getProductName() { return productName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public void setProductName(String productName) { this.productName = productName; }
}