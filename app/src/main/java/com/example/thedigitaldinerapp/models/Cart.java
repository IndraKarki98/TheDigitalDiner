package com.example.thedigitaldinerapp.models;

import java.io.Serializable;

public class Cart implements Serializable {

    private int cartId, foodId, price, quantity, perItemPrice;
    private String foodName, foodCategoryName, foodImage;

    public Cart(int cartId, int foodId, int price, int quantity, int perItemPrice, String foodName, String foodCategoryName, String foodImage) {
        this.cartId = cartId;
        this.foodId = foodId;
        this.price = price;
        this.quantity = quantity;
        this.perItemPrice = perItemPrice;
        this.foodName = foodName;
        this.foodCategoryName = foodCategoryName;
        this.foodImage = foodImage;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPerItemPrice() {
        return perItemPrice;
    }

    public void setPerItemPrice(int perItemPrice) {
        this.perItemPrice = perItemPrice;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCategoryName() {
        return foodCategoryName;
    }

    public void setFoodCategoryName(String foodCategoryName) {
        this.foodCategoryName = foodCategoryName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}