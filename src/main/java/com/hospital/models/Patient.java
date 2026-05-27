package com.hospital.models;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {

    private static final long serialVersionUID = 8L; // Recommended for Serializable classes

    private List<String> medicalHistory;
    private List<String> pendingBills;
    private double currentBalance;

    public Patient(String id, String name, String password) {
        super(id, name, "PATIENT", password);
        this.medicalHistory = new ArrayList<>();
        this.pendingBills = new ArrayList<>();
        this.currentBalance = 0.0;
    }

    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public void addMedicalRecord(String record) {
        this.medicalHistory.add(record);
    }

    public List<String> getPendingBills() {
        return pendingBills;
    }

    public void addBillItem(String item, double cost) {
        this.pendingBills.add(item + " - Rs. " + cost);
        this.currentBalance += cost;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void clearBalance() {
        this.pendingBills.clear();
        this.currentBalance = 0.0;
    }

    @Override
    public String toString() {
        return String.format("%s", getName());
    }

}
