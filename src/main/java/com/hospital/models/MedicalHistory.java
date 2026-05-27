package com.hospital.models;

import java.io.Serializable;

public class MedicalHistory implements Serializable {

    private static final long serialVersionUID = 7L; // Recommended for Serializable classes

    private String recordId;
    private String date;
    private String doctorName;
    private String patientName;
    private String diagnosisAndTreatment;

    public MedicalHistory(String recordId, String date, String doctorName, String diagnosis, String patientName) {
        this.recordId = recordId;
        this.date = date;
        this.doctorName = doctorName;
        this.diagnosisAndTreatment = diagnosis;
        this.patientName = patientName;
    }

    // Getters and Setters
    public String getRecordId() {
        return recordId;
    }

    public String getDate() {
        return date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDiagnosis() {
        return diagnosisAndTreatment;
    }

    public String getPatientName() {
        return patientName;
    }
}