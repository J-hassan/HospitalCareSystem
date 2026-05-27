package com.hospital.models;

import java.io.Serializable;

// Connects patient, doctor, date, time, and status according to the new FXML dashboard design
public class Appointment implements Serializable {

    private static final long serialVersionUID = 2L; // Recommended for Serializable classes

    private String apptId;
    private String apptPatient;
    private String apptDoctor;
    private String apptDate; // Added to match fx:id="aptDatePicker" / "aptDateCol"
    private String apptTime; // Added to match fx:id="aptTimeComboBox" / "aptTimeCol"
    private String apptStatus; // Added to match fx:id="aptStatusComboBox" / "aptStatusCol"
    private String apptNotesFromDoctor = "";

    // 1. Default Constructor (Required for various frameworks/loaders)
    public Appointment() {
    }

    // 2. Parameterized Constructor (Convenient for creating objects from DB or
    // Form)
    public Appointment(String apptId, String apptPatient, String apptDoctor, String apptDate, String apptTime,
            String apptStatus) {
        this.apptId = apptId;
        this.apptPatient = apptPatient;
        this.apptDoctor = apptDoctor;
        this.apptDate = apptDate;
        this.apptTime = apptTime;
        this.apptStatus = apptStatus;
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public String getApptPatient() {
        return apptPatient;
    }

    public void setApptPatient(String apptPatient) {
        this.apptPatient = apptPatient;
    }

    public String getApptDoctor() {
        return apptDoctor;
    }

    public void setApptDoctor(String apptDoctor) {
        this.apptDoctor = apptDoctor;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getApptTime() {
        return apptTime;
    }

    public void setApptTime(String apptTime) {
        this.apptTime = apptTime;
    }

    public String getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(String apptStatus) {
        this.apptStatus = apptStatus;
    }

    public String getApptNotesFromDoctor() {
        return apptNotesFromDoctor;
    }

    public void setApptNotesFromDoctor(String apptNote) {
        this.apptNotesFromDoctor = apptNote;
    }

    // 3. Optional: toString() method for easy debugging/logging
    @Override
    public String toString() {
        return "Appointment{" +
                "apptId='" + apptId + '\'' +
                ", apptPatient='" + apptPatient + '\'' +
                ", apptDoctor='" + apptDoctor + '\'' +
                ", apptDate='" + apptDate + '\'' +
                ", apptTime='" + apptTime + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                '}';
    }
}