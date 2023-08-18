package com.example.fooddelivery.model;

public class Order {
    private int totalQuantityAllItem;
    private int totalOrderAllPrice;
    private String paymentOption;
    private String currentDate;
    private String currentTime;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(int totalQuantityAllItem, int totalOrderAllPrice, String paymentOption, String currentDate, String currentTime) {
        this.totalQuantityAllItem = totalQuantityAllItem;
        this.totalOrderAllPrice = totalOrderAllPrice;
        this.paymentOption = paymentOption;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public int getTotalQuantityAllItem() {
        return totalQuantityAllItem;
    }

    public int getTotalOrderAllPrice() {
        return totalOrderAllPrice;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }
}
