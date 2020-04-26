package com.limelite.limeli8;

public class ProductModel {

    private String name, category, section, imageUrl;

    public ProductModel() {
    }

    public ProductModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductModel(String name, String category, String section, String imageUrl) {
        this.name = name;
        this.category = category;
        this.section = section;
        this.imageUrl = imageUrl;
    }
}
