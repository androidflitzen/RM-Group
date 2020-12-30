package com.flitzen.rmapp.AdaptersAndItems;

import java.util.List;

public class CartItems {
    public int cartId;
    public int categoryId;
    public String categoryName;
    public String variation1;
    public String variation2;
    public String variation3;
    public String variation4;
    public String variation5;
    public String variation6;
    public String variation7;
    public String variation8;
    public String variationString;
    public String note;
    public String imgUrl;
    public List<CartSubItems> list;

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setVariation1(String variation1) {
        this.variation1 = variation1;
    }

    public void setVariation2(String variation2) {
        this.variation2 = variation2;
    }

    public void setVariation3(String variation3) {
        this.variation3 = variation3;
    }

    public void setVariation4(String variation4) {
        this.variation4 = variation4;
    }

    public void setVariation5(String variation5) {
        this.variation5 = variation5;
    }

    public void setVariation6(String variation6) {
        this.variation6 = variation6;
    }

    public void setVariation7(String variation7) {
        this.variation7 = variation7;
    }

    public void setVariation8(String variation8) {
        this.variation8 = variation8;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setList(List<CartSubItems> list) {
        this.list = list;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setVariationString(String variationString) {
        this.variationString = variationString;
    }
}
