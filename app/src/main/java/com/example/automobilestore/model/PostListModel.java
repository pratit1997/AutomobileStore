package com.example.automobilestore.model;

import android.net.Uri;

public class PostListModel {



    String CarId;
    String Model,Engine_type;
    String Year;
    String Amount;
    private Uri image;

    public PostListModel(String carId, String model, String engine_type, String year, String amount, Uri image) {
        CarId = carId;
        Model = model;
        Engine_type = engine_type;
        Year = year;
        Amount = amount;
        this.image = image;
    }





    public String getCarId() {
        return CarId;
    }

    public void setCarId(String carId) {
        CarId = carId;
    }
    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getEngine_type() {
        return Engine_type;
    }

    public void setEngine_type(String engine_type) {
        Engine_type = engine_type;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }


}
