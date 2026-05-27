package com.hospital.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR = System.getProperty("user.home") + File.separator + "HospitalCare"
            + File.separator;
    private static final String USER_FILE = DATA_DIR + "users.dat";
    private static final String APPOINTMENT_FILE = DATA_DIR + "appointments.dat";
    private static final String BILL_FILE = DATA_DIR + "bills.dat";
    // private static final String CONSULTATION_FILE = DATA_DIR +
    // "consultations.dat";
    // private static final String LABTESTS_FILE = DATA_DIR + "labtests.dat";
    // private static final String TREATMENT_FILE = DATA_DIR + "treatments.dat";
    private static final String EMERGENCY_FILE = DATA_DIR + "emergencies.dat";
    private static final String HISTORY_FILE = DATA_DIR + "history.dat";

    private static void ensureDirExists() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static boolean checkFileExistence(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * Generic Save Method (Object Arrays Data Persistence)
     */
    public static <T> void saveRecords(String filePath, List<T> records) {
        ensureDirExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(records);
            System.out.println("Data saved successfully to: " + filePath);
        } catch (IOException e) {
            System.err.println("Serialization Error inside saveRecords: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generic Load Method (Object Arrays Data Retrieval)
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadRecords(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Empty state management if file doesn't exist yet
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Deserialization Error inside loadRecords: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static String getUserFile() {
        return USER_FILE;
    }

    public static String getAppointmentFile() {
        return APPOINTMENT_FILE;
    }

    public static String getBillFile() {
        return BILL_FILE;
    }

    // public static String getLabtestFile() {
    // return LABTESTS_FILE;
    // }

    // public static String getConsulationFile() {
    // return CONSULTATION_FILE;
    // }

    // public static String getTreatmentFile() {
    // return TREATMENT_FILE;
    // }

    public static String getEmergencyFile() {
        return EMERGENCY_FILE;
    }

    public static String getHistoryFile() {
        return HISTORY_FILE;
    }
}
