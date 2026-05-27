package com.hospital.models;

import java.io.Serializable;

public class EmergencyCase implements Serializable {
    private static final long serialVersionUID = 5L;

    private String caseId;
    private String patientName;
    private String emergencyType;
    private String priority;
    private String status;

    public EmergencyCase() {
    }

    public EmergencyCase(String caseId, String patientName, String emergencyType, String priority, String status) {
        this.caseId = caseId;
        this.patientName = patientName;
        this.emergencyType = emergencyType;
        this.priority = priority;
        this.status = status;
    }

    // Getters and Setters
    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}