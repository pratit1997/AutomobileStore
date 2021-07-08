package com.example.automobilestore.model;

import android.net.Uri;

public class VerticalCarData {
    String id;
    String name;
    String price;
    private Uri imageUrl;
    String condition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public VerticalCarData(String id,String name, String price, Uri imageUrl, String condition) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.condition = condition;
        this.id=id;
    }



    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
