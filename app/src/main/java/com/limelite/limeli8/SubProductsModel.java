package com.limelite.limeli8;

public class SubProductsModel {

    private String imageUrl, name, offerOnAbove, offerPrice, pieces, flatOff, strikePrice;

    public SubProductsModel() {
    }

    public SubProductsModel(String imageUrl, String name, String offerOnAbove, String offerPrice, String pieces, String flatOff, String strikePrice) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.offerOnAbove = offerOnAbove;
        this.offerPrice = offerPrice;
        this.pieces = pieces;
        this.flatOff = flatOff;
        this.strikePrice = strikePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferOnAbove() {
        return offerOnAbove;
    }

    public void setOfferOnAbove(String offerOnAbove) {
        this.offerOnAbove = offerOnAbove;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getFlatOff() {
        return flatOff;
    }

    public void setFlatOff(String flatOff) {
        this.flatOff = flatOff;
    }

    public String getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(String strikePrice) {
        this.strikePrice = strikePrice;
    }
}
