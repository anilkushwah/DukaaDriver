package com.dollop.dukaadriver.model;

public class OrderDetailsModelList {
    String itemsName,itemQuantity,perQuantityPrice;
    int itemImage;

    public String getItemsName() {
        return itemsName;
    }

    public void setItemsName(String itemsName) {
        this.itemsName = itemsName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getPerQuantityPrice() {
        return perQuantityPrice;
    }

    public void setPerQuantityPrice(String perQuantityPrice) {
        this.perQuantityPrice = perQuantityPrice;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }
}
