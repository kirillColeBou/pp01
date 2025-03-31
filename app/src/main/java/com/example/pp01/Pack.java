package com.example.pp01;

public class Pack {
    private String id;
    private String name;
    private int id_category;
    private int count;
    private int id_storehouse;
    private String QR;
    private String categoryName;
    private String warehouseName;

    public Pack(String id, String name, int id_category, int count, int id_storehouse, String QR) {
        this.id = id;
        this.name = name;
        this.id_category = id_category;
        this.count = count;
        this.id_storehouse = id_storehouse;
        this.QR = QR;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return id_category; }
    public int getQuantity() { return count; }
    public int getWarehouseId() { return id_storehouse; }
    public String getQrCode() { return QR; }
    public String getCategoryName() { return categoryName; }
    public String getWarehouseName() { return warehouseName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
}