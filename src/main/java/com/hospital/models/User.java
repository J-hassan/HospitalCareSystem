package com.hospital.models;

import java.io.Serializable;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 9L; // Recommended for Serializable classes

    private String id;
    private String name;
    private String role;
    private String password;

    public User(String id, String name, String role, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    // Getters and Setters (Encapsulation Rules)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String role) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
