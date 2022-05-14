package com.example.thedigitaldinerapp.models;

import java.io.Serializable;

public class FoodCategory implements Serializable {

    private int id;
    private String category_name, image;

    public FoodCategory(int id, String category_name, String image) {
        this.id = id;
        this.category_name = category_name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return getCategory_name();
    }
}
