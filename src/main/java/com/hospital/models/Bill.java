package com.hospital.models;

import java.io.Serializable;

public class Bill implements Serializable {
    private static final long serialVersionUID = 3L;

    private String billId;
    private String patientName;
    private String service;
    private int quantity;
    private double totalAmount;

    public Bill() {
    }

    public Bill(String billId, String patientName, String service, int quantity, double totalAmount) {
        this.billId = billId;
        this.patientName = patientName;
        this.service = service;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}