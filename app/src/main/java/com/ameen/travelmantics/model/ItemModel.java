package com.ameen.travelmantics.model;

import java.io.Serializable;

public class ItemModel implements Serializable {

    String id;
    String title;
    String desc;
    String price;
    String imageUrl;
    private String imageName;

    public ItemModel() {
    }

    public ItemModel(String title, String desc, String price, String imageUrl, String imageName) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
