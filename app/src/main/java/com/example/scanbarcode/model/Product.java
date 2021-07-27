package com.example.scanbarcode.model;

public class Product {
    String id;
    String name;
    int amount;
    String urlImg;
    String barCode;

    public Product() {
    }

    public Product(String id, String name, int amount, String urlImg, String barCode) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.urlImg = urlImg;
        this.barCode = barCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
