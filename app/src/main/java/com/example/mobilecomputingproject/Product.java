package com.example.mobilecomputingproject;

public class Product {
    private int ImageResource;
    private String name;
    private String rating;

    public Product(int imageResource, String name, String rating) {
        this.ImageResource = imageResource;
        this.name = name;
        this.rating = rating;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }


}
