package com.zearoconsulting.smartmenu.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 01-11-2016.
 */

public class KOTLineItems implements Serializable {

    private Product mProduct;
    private long tableId;
    private long kotNumber;
    private int qty;
    private double totalPrice;
    private String notes;
    private String printed;
    private long rowId;
    private long refRowId;
    private String isExtraProduct;
    private long kotLineId;

    public long getKotLineId() {
        return kotLineId;
    }

    public void setKotLineId(long kotLineId) {
        this.kotLineId = kotLineId;
    }

    public long getRefRowId() {
        return refRowId;
    }

    public void setRefRowId(long refRowId) {
        this.refRowId = refRowId;
    }

    public String getIsExtraProduct() {
        return isExtraProduct;
    }

    public void setIsExtraProduct(String isExtraProduct) {
        this.isExtraProduct = isExtraProduct;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getPrinted() {
        return printed;
    }

    public void setPrinted(String printed) {
        this.printed = printed;
    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product mProduct) {
        this.mProduct = mProduct;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getKotNumber() {
        return kotNumber;
    }

    public void setKotNumber(long kotNumber) {
        this.kotNumber = kotNumber;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
