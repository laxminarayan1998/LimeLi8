package com.limelite.limeli8;

public class MyBookingsModel {

    String date, deliveryPrice, grandTotalPrice, gstPrice, gstValue, offerPrice, orderId, orderStatus, price, priceWithQuantity, productSubTitleValue, productUrl, totalPrice, productName;

    public MyBookingsModel() {
    }

    public MyBookingsModel(String date, String deliveryPrice, String grandTotalPrice, String gstPrice, String gstValue, String offerPrice, String orderId, String orderStatus, String price, String priceWithQuantity, String productSubTitleValue, String productUrl, String totalPrice, String productName) {
        this.date = date;
        this.deliveryPrice = deliveryPrice;
        this.grandTotalPrice = grandTotalPrice;
        this.gstPrice = gstPrice;
        this.gstValue = gstValue;
        this.offerPrice = offerPrice;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.price = price;
        this.priceWithQuantity = priceWithQuantity;
        this.productSubTitleValue = productSubTitleValue;
        this.productUrl = productUrl;
        this.totalPrice = totalPrice;
        this.productName = productName;
    }

    public String getDate() {
        return date;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getGrandTotalPrice() {
        return grandTotalPrice;
    }

    public String getGstPrice() {
        return gstPrice;
    }

    public String getGstValue() {
        return gstValue;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceWithQuantity() {
        return priceWithQuantity;
    }

    public String getProductSubTitleValue() {
        return productSubTitleValue;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getProductName() {
        return productName;
    }
}
