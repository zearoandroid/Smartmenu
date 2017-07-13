package com.zearoconsulting.smartmenu.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 30-10-2016.
 */

public class Tables implements Serializable {

    private long tableId;
    private String tableName;
    private long clientId;
    private long orgId;
    private String isCoversLevel;
    private long tableGroupId;
    private int orderGroup;

    public String getOrderAvailable() {
        return orderAvailable;
    }

    public void setOrderAvailable(String orderAvailable) {
        this.orderAvailable = orderAvailable;
    }

    private String orderAvailable;

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getIsCoversLevel() {
        return isCoversLevel;
    }

    public void setIsCoversLevel(String isCoversLevel) {
        this.isCoversLevel = isCoversLevel;
    }

    public long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getOrderGroup() {
        return orderGroup;
    }

    public void setOrderGroup(int orderGroup) {
        this.orderGroup = orderGroup;
    }

}
