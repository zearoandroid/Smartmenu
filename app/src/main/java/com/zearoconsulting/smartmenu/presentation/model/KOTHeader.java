package com.zearoconsulting.smartmenu.presentation.model;

import java.io.Serializable;

/**
 * Created by saravanan on 31-10-2016.
 */

public class KOTHeader implements Serializable {

    private long tablesId;
    private long kotNumber;
    private long invoiceNumber;
    private long terminalId;
    private double totalAmount;
    private String status;

    public long getTablesId() {
        return tablesId;
    }

    public void setTablesId(long tablesId) {
        this.tablesId = tablesId;
    }

    public long getKotNumber() {
        return kotNumber;
    }

    public void setKotNumber(long kotNumber) {
        this.kotNumber = kotNumber;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
