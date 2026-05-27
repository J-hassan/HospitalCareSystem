package com.hospital.controllers;

import com.hospital.models.Appointment;
import com.hospital.models.Bill;
import com.hospital.models.MedicalHistory;
import com.hospital.storage.FileManager;
import com.hospital.utils.SceneManager;
import com.hospital.utils.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DoctorController {

    // Sidebar Patient Queue Registry
    @FXML
    private ListView<String> appointmentListView; // Hum unique formats ke liye String use kar rahe hain

    @FXML
    private Button logoutBtn;

    // Active Consultation Workspace Widgets
    @FXML
    private Label activePatientLabel;
    @FXML
    private TextArea diagnosisTextArea;
    @FXML
    private ComboBox<String> labTestComboBox;
    @FXML
    private Button completeSessionBtn;

    // Local state trackers
    private List<Appointment> allAppointments;
    private List<Appointment> doctorFilteredAppointments;
    private Appointment selectedAppointment = null;

    // Hardcoded logge-in doctor name (Aap ise apne session/login context se replace
    // kar sakte hain)
    private final String currentDoctorName = SessionManager.getUsername();

    /**
     * Initializer hook system properties loading ke waqt chalta ha.
     */
    @FXML
    public void initialize() {
        // 1. Load data into queue instantly
        refreshPatientQueue();

        // 2. Selection Listener: List view row click hone par activePatientLabel set
        // hoga
        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = appointmentListView.getSelectionModel().getSelectedIndex();
                selectedAppointment = doctorFilteredAppointments.get(selectedIndex);

                activePatientLabel.setText("Patient: " + selectedAppointment.getApptPatient());
                diagnosisTextArea.clear();
                labTestComboBox.setValue("None / Routine Check");
            }
        });
    }

    /**
     * Re-loads appointments file and filters records specifically for this doctor
     */
    private void refreshPatientQueue() {
        // 1. Safe File Reading
        allAppointments = FileManager.loadRecords(FileManager.getAppointmentFile());

        System.out.println("=== DOCTOR DASHBOARD DEBUG ===");
        System.out.println("Current Logged-in Username: '" + currentDoctorName + "'");

        if (allAppointments == null || allAppointments.isEmpty()) {
            System.out.println("ALERT: Database file is completely EMPTY or NULL!");
            return;
        }

        // 2. Optimized Filtering using .contains() to handle names like 'Dr. rahman
        // [Cardiologist]'
        doctorFilteredAppointments = allAppointments.stream()
                .filter(apt -> {
                    if (apt.getApptDoctor() == null || apt.getApptStatus() == null)
                        return false;

                    // Transfrom everything to lower case for case-insensitive matching
                    String docInFile = apt.getApptDoctor().toLowerCase().trim();
                    String statusInFile = apt.getApptStatus().toLowerCase().trim();
                    String loggedInDoc = (currentDoctorName != null) ? currentDoctorName.toLowerCase().trim() : "";

                    // SMART CHECK: 'Dr. rahman [Cardiologist]' contains 'rahman'
                    boolean doctorMatches = docInFile.contains(loggedInDoc);
                    boolean statusMatches = statusInFile.equals("scheduled");

                    return doctorMatches && statusMatches;
                })
                .collect(Collectors.toList());

        System.out.println("Total filtered appointments found: " + doctorFilteredAppointments.size());
        System.out.println("=================================");

        // 3. UI Presentation Binding
        ObservableList<String> displayItems = FXCollections.observableArrayList();
        for (Appointment apt : doctorFilteredAppointments) {
            displayItems
                    .add("🆔 " + apt.getApptId() + " | 👤 " + apt.getApptPatient() + " (" + apt.getApptTime() + ")");
        }

        appointmentListView.setItems(displayItems);
    }

    /**
     * Action: Transmits details into medical database pipeline and resets view
     * state
     */
    @FXML
    void handleCompleteConsultation(ActionEvent event) {
        if (selectedAppointment == null) {
            activePatientLabel.setText("ERROR: Please select a patient from queue first!");
            activePatientLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            return;
        }

        String diagnosisNotes = diagnosisTextArea.getText();
        String selectedTest = (labTestComboBox.getValue() != null) ? labTestComboBox.getValue()
                : "None / Routine Check";

        if (diagnosisNotes.trim().isEmpty()) {
            activePatientLabel.setText("ERROR: Please write Treatment notes / diagnosis!");
            activePatientLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            return;
        }

        System.out.println(
                "Processing Patient Checkout Log -> Notes: " + diagnosisNotes + " | Assigned Test: " + selectedTest);

        List<MedicalHistory> medicalHistories = FileManager.loadRecords(FileManager.getHistoryFile());
        List<Bill> bills = FileManager.loadRecords(FileManager.getBillFile());

        for (Appointment apt : allAppointments) {
            if (apt.getApptId().equals(selectedAppointment.getApptId())) {

                // 1. Update Appointment Status
                apt.setApptNotesFromDoctor(diagnosisNotes);
                apt.setApptStatus("Completed");

                // 2. Create Medical History Record
                String uniqueIdSuffix = UUID.randomUUID().toString().substring(0, 15);
                String finalId = "HISTORY-" + uniqueIdSuffix;
                MedicalHistory medicalHistory = new MedicalHistory(
                        finalId,
                        apt.getApptDate(),
                        apt.getApptDoctor(),
                        diagnosisNotes,
                        apt.getApptPatient());

                medicalHistories.add(medicalHistory);

                // 3. Create Bill Record (With Base Consultation Fee)
                String uniqueIdSuffixBill = UUID.randomUUID().toString().substring(0, 15);
                String finalIdBill = "BILL-" + uniqueIdSuffixBill;

                // Har checkup ki base fee let's say 500 hai, tests ki fee isme plus hogi
                double totalAmount = 500.0; // Base Consultation Fee
                String billServiceDescription = "Doctor Consultation Fee";

                if (selectedTest.contains("Blood Test")) {
                    totalAmount += 300.0;
                    billServiceDescription += " + Blood Test";
                } else if (selectedTest.contains("X-Ray Scan")) {
                    totalAmount += 1200.0;
                    billServiceDescription += " + X-Ray Scan";
                } else {
                    // Agar "None" ya "Routine Check" hai toh sirf base fee hi rahegi
                    billServiceDescription += " (Routine Check)";
                }

                // Aapke PatientController ke switch-case se match karne ke liye description
                // safe pass karein
                Bill bill = new Bill(finalIdBill, apt.getApptPatient(), billServiceDescription, 1, totalAmount);
                bills.add(bill);

                break;
            }
        }

        // Save all state data modifications to files
        FileManager.saveRecords(FileManager.getAppointmentFile(), allAppointments);
        FileManager.saveRecords(FileManager.getHistoryFile(), medicalHistories);
        FileManager.saveRecords(FileManager.getBillFile(), bills);

        // Reset UI consultation workspace controls
        diagnosisTextArea.clear();
        labTestComboBox.setValue(null);
        selectedAppointment = null;

        activePatientLabel.setText("No Patient Selected (Select from Queue)");
        activePatientLabel.setStyle("-fx-text-fill: #0f172a;");

        refreshPatientQueue();
    }

    /**
     * Action: User safety terminal redirect sequence
     */
    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Doctor leaving shift. Cleaning workspace state hooks...");
        SessionManager.clearCredentials();
        SceneManager.switchScene("login", "Hospital Care - Portal Login");
    }
}