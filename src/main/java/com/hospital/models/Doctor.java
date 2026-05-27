package com.hospital.models;

public class Doctor extends User {

    private static final long serialVersionUID = 4L; // Recommended for Serializable classes

    private String specialization;

    public Doctor(String id, String name, String specialization, String password) {
        super(id, name, "DOCTOR", password);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // UI displays direct name wrapper inside list combobox references
    @Override
    public String toString() {
        return "Dr. " + getName() + " [" + specialization + "]";
    }
}
