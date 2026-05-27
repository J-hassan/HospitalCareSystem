package com.hospital.utils;

public class SessionManager {

    private static String username;
    private static String role;

    public SessionManager() {
    }

    public static void setCredentials(String name, String r) {
        username = name;
        role = r;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static void clearCredentials() {
        username = "";
        role = "";
    }

}
