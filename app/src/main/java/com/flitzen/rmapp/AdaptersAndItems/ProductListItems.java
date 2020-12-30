package com.flitzen.rmapp.AdaptersAndItems;

public class ProductListItems {
    public int id;
    public String name;
    public String details;
    public String thubnailImage;
    public String image;
    public int qty = 0;

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setThubnailImage(String thubnailImage) {
        this.thubnailImage = thubnailImage;
    }
}
