package com.example.pp01;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String nz;
    private int id_type;
    private String alias;
    private String name;
    private String img;
    private String typeName;

    public Product(int id, String nz, int id_type, String alias, String name, String img) {
        this.id = id;
        this.nz = nz;
        this.id_type = id_type;
        this.alias = alias;
        this.name = name;
        this.img = img;
    }

    public int getId() { return id; }
    public String getNz() { return nz; }
    public int getIdType() { return id_type; }
    public String getAlias() { return alias; }
    public String getName() { return name; }
    public String getImg() { return img; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
}