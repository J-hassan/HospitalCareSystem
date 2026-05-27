package com.hospital.models;

public class AdminStaff extends User {

    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    public AdminStaff(String id, String name, String role, String password) {
        super(id, name, role, password);
    }
}
