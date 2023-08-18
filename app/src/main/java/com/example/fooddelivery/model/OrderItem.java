package com.example.fooddelivery.model;

public class OrderItem {
    private String foodName;
    private int quantity;
    private int price;

    public OrderItem(String foodName, int quantity, int price) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
