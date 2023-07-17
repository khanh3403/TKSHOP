package com.example.tkshop.Model;

import java.io.Serializable;

public class Products implements Serializable {

    private String id;
    private String name;
    private int inventory;
    private int price;
    private String image;
    private String id_Category;

    public Products() {

    }

    public Products(String name, int inventory, int price, String image, String id_Category) {
        this.name = name;
        this.inventory = inventory;
        this.price = price;
        this.image = image;
        this.id_Category = id_Category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId_Category() {
        return id_Category;
    }

    public void setId_Category(String id_Category) {
        this.id_Category = id_Category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
