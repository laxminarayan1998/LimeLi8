package com.limelite.limeli8;

public class SubProductDetailsPrice {

    private String option, price;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public SubProductDetailsPrice(String option, String price) {
        this.option = option;
        this.price = price;
    }

    public SubProductDetailsPrice() {
    }
}
