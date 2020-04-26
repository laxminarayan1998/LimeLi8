package com.limelite.limeli8;

public class SlideModel {

    private String url;
    private String product;
    private String subProduct;

    public String getUrl() {
        return url;
    }

    public SlideModel() {
    }

    public SlideModel(String url) {
        this.url = url;
    }

    public String getProduct() {
        return product;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public SlideModel(String url, String product, String subProduct) {
        this.url = url;
        this.product = product;
        this.subProduct = subProduct;
    }
}
