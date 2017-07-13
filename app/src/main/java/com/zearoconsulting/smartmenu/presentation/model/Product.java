package com.zearoconsulting.smartmenu.presentation.model;

import java.io.Serializable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by saravanan on 27-05-2016.
 */
public class Product implements Serializable {

    private long categoryId;
    private long prodId;
    private String prodName;
    private String prodValue;
    private long uomId;
    private String uomValue;
    private double salePrice;
    private double costPrice;
    private String prodImage;
    private String prodArabicName;
    private int defaultQty;
    private String showDigitalMenu;
    private String prodVideoPath;
    private String calories;
    private String preparationTime;
    private String description;
    private long terminalId;
    private long clientId;
    private long orgId;
    private String prodArabicCalories;
    private String prodArabicPreTime;
    private String prodArabicDescription;
    private int qty;
    private double totalPrice;
    private List<ProductMultiImage> mImageList;
    private long parentId;
    private String isSelected;
    private long rowId;
    private long tableId;
    private long kotLineId;
    private long coverId;

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public long getKotLineId() {
        return kotLineId;
    }

    public void setKotLineId(long kotLineId) {
        this.kotLineId = kotLineId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getSelected() {
        return isSelected;
    }

    public void setSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public List<ProductMultiImage> getImageList() {
        return mImageList;
    }

    public void setImageList(List<ProductMultiImage> mImageList) {
        this.mImageList = mImageList;
    }

    public String getProdArabicCalories() {
        return prodArabicCalories;
    }

    public void setProdArabicCalories(String prodArabicCalories) {
        this.prodArabicCalories = prodArabicCalories;
    }

    public String getProdArabicPreTime() {
        return prodArabicPreTime;
    }

    public void setProdArabicPreTime(String prodArabicPreTime) {
        this.prodArabicPreTime = prodArabicPreTime;
    }

    public String getProdArabicDescription() {
        return prodArabicDescription;
    }

    public void setProdArabicDescription(String prodArabicDescription) {
        this.prodArabicDescription = prodArabicDescription;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdValue() {
        return prodValue;
    }

    public void setProdValue(String prodValue) {
        this.prodValue = prodValue;
    }

    public long getUomId() {
        return uomId;
    }

    public void setUomId(long uomId) {
        this.uomId = uomId;
    }

    public String getUomValue() {
        return uomValue;
    }

    public void setUomValue(String uomValue) {
        this.uomValue = uomValue;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public String getProdArabicName() {
        return prodArabicName;
    }

    public void setProdArabicName(String prodArabicName) {
        this.prodArabicName = prodArabicName;
    }

    public int getDefaultQty() {
        return defaultQty;
    }

    public void setDefaultQty(int defaultQty) {
        this.defaultQty = defaultQty;
    }

    public String getShowDigitalMenu() {
        return showDigitalMenu;
    }

    public void setShowDigitalMenu(String showDigitalMenu) {
        this.showDigitalMenu = showDigitalMenu;
    }

    public String getProdVideoPath() {
        return prodVideoPath;
    }

    public void setProdVideoPath(String prodVideoPath) {
        this.prodVideoPath = prodVideoPath;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public long getCoverId() {
        return coverId;
    }

    public void setCoverId(long coverId) {
        this.coverId = coverId;
    }
}
