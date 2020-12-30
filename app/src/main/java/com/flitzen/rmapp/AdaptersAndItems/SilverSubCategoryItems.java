package com.flitzen.rmapp.AdaptersAndItems;

public class SilverSubCategoryItems {
    public int id;
    public int parentCategoryId;
    public int mainCategoryId;
    public String name;
    public String image;
    public String data;
    public boolean isFavorite;
    public boolean setFavorite = false;

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setSetFavorite(boolean setFavorite) {
        this.setFavorite = setFavorite;
    }
}
