package com.example.automobilestore.model;

import android.net.Uri;

public class HorizontalCarData {

    String name;
    String price;
    private Uri imageUrl;
    String id;
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






    public HorizontalCarData(String id,String name, String price,  Uri imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.id=id;
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
