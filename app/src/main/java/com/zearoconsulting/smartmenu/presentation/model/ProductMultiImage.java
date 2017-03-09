package com.zearoconsulting.smartmenu.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 23-12-2016.
 */

public class ProductMultiImage implements Serializable {

    private long prodId;
    private String prodMultiPath;
    private String prodMultiPathType;

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public String getProdMultiPath() {
        return prodMultiPath;
    }

    public void setProdMultiPath(String prodMultiPath) {
        this.prodMultiPath = prodMultiPath;
    }

    public String getProdMultiPathType() {
        return prodMultiPathType;
    }

    public void setProdMultiPathType(String prodMultiPathType) {
        this.prodMultiPathType = prodMultiPathType;
    }
}
