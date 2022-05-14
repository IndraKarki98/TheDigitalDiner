package com.example.thedigitaldinerapp.models;

import java.io.Serializable;

public class Food implements Serializable {

    private int id, price, foodCategory;
    private String foodName, ingredients, image;

    public Food(int id, int price, int foodCategory, String foodName, String ingredients, String image) {
        this.id = id;
        this.price = price;
        this.foodCategory = foodCategory;
        this.foodName = foodName;
        this.ingredients = ingredients;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(int foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
