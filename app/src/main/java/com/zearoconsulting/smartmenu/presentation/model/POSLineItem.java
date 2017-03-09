package com.zearoconsulting.smartmenu.presentation.model;

import java.io.Serializable;

import java.io.Serializable;

/**
 * Created by saravanan on 24-05-2016.
 */
public class POSLineItem implements Serializable {

    private long posId;
    private long productId;
    private long categoryId;
    private String productName;
    private String productValue;
    private long posUOMId;
    private String posUOMValue;
    private int posQty;
    private double stdPrice;
    private double costPrice;
    private double totalPrice;
    private int discType;
    private int discValue;

    private String isUpdated;
    private String isPosted;
    private String prodArabicName;
    private String isDiscounted;

    public String getIsDiscounted() {
        return isDiscounted;
    }

    public void setIsDiscounted(String isDiscounted) {
        this.isDiscounted = isDiscounted;
    }

    public String getProdArabicName() {
        return prodArabicName;
    }

    public void setProdArabicName(String prodArabicName) {
        this.prodArabicName = prodArabicName;
    }

    public long getPosId() {
        return posId;
    }

    public void setPosId(long posId) {
        this.posId = posId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getCategoryId() { return categoryId; }

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductValue() {
        return productValue;
    }

    public void setProductValue(String productValue) {
        this.productValue = productValue;
    }

    public long getPosUOMId() {
        return posUOMId;
    }

    public void setPosUOMId(long posUOMId) {
        this.posUOMId = posUOMId;
    }

    public String getPosUOMValue() {
        return posUOMValue;
    }

    public void setPosUOMValue(String posUOMValue) {
        this.posUOMValue = posUOMValue;
    }

    public int getPosQty() {
        return posQty;
    }

    public void setPosQty(int posQty) {
        this.posQty = posQty;
    }

    public double getStdPrice() {
        return stdPrice;
    }

    public void setStdPrice(double stdPrice) {
        this.stdPrice = stdPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDiscType() {
        return discType;
    }

    public void setDiscType(int discType) {
        this.discType = discType;
    }

    public int getDiscValue() {
        return discValue;
    }

    public void setDiscValue(int discValue) {
        this.discValue = discValue;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }

    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(posId);
        dest.writeLong(productId);
        dest.writeLong(categoryId);
        dest.writeString(productName);
        dest.writeString(productValue);
        dest.writeLong(posUOMId);
        dest.writeString(posUOMValue);
        dest.writeInt(posQty);
        dest.writeInt(discount);
        dest.writeDouble(stdPrice);
        dest.writeDouble(costPrice);
        dest.writeDouble(totalPrice);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public POSLineItem createFromParcel(Parcel in) {
            return new POSLineItem(in);
        }

        public POSLineItem[] newArray(int size) {
            return new POSLineItem[size];
        }
    };

    // "De-parcel object
    public POSLineItem(Parcel in) {
        posId = in.readLong();
        productId = in.readLong();
        categoryId = in.readLong();
        productName = in.readString();
        productValue = in.readString();
        posUOMId = in.readLong();
        posUOMValue = in.readString();
        posQty = in.readInt();
        discount = in.readInt();
        stdPrice = in.readLong();
        costPrice = in.readLong();
        totalPrice = in.readLong();
    }*/
}
