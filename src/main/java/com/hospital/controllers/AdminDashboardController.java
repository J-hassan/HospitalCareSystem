package com.hospital.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hospital.models.*;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.hospital.storage.FileManager;
import com.hospital.utils.SceneManager;
import com.hospital.utils.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminDashboardController {

    // ===============================================-------
    // Sidebar Navigation Widgets
    // ===============================================-------
    @FXML
    private Button navHomeBtn;
    @FXML
    private Button navRegisterBtn;
    @FXML
    private Button navAppointmentsBtn;
    @FXML
    private Button navBillingBtn;
    @FXML
    private Button navEmergencyBtn;
    @FXML
    private Button navReportsBtn;
    @FXML
    private Button logoutBtn;

    // ===============================================-------
    // Creation Profile Inputs - registration panel
    // ===============================================-------
    @FXML
    private VBox registrationPanel;
    @FXML
    private ComboBox<String> actionComboBox;
    @FXML
    private TextField regNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> regRoleComboBox;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private VBox specContainer;
    @FXML
    private TextField specField;
    @FXML
    private Button saveUserBtn;

    // ===============================================-------
    // Home Panel
    // ===============================================-------
    @FXML
    private VBox homePanel;
    @FXML
    private TableView<User> allUsersTable;
    @FXML
    private TableColumn<User, String> idCol;
    @FXML
    private TableColumn<User, String> nameCol;
    @FXML
    private TableColumn<User, String> roleCol;
    @FXML
    private Label emergencyCountLabel;
    @FXML
    private Label adminCountLabel;
    @FXML
    private Label doctorCountLabel;
    @FXML
    private Label aptCountLabel;
    @FXML
    private Label patientCountLabel;

    // ===============================================-------
    // Billing Panel
    // ===============================================-------
    @FXML
    private HBox billingPanel;
    @FXML
    private TextField billIdField;
    @FXML
    private ComboBox<Patient> billPatientComboBox;
    @FXML
    private ComboBox<String> billServiceComboBox;
    @FXML
    private TextField billQuantityField;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label billMessageLabel;
    // Table infrastructure
    @FXML
    private TableView<Bill> billingTable;
    @FXML
    private TableColumn<Bill, String> billIdCol;
    @FXML
    private TableColumn<Bill, String> billPatientCol;
    @FXML
    private TableColumn<Bill, String> billServiceCol;
    @FXML
    private TableColumn<Bill, Integer> billQtyCol;
    @FXML
    private TableColumn<Bill, Double> billTotalCol;

    // ===============================================-------
    // Emergency Panel
    // ===============================================-------
    @FXML
    private HBox emergencyPanel;
    @FXML
    private TextField emgCaseIdField;
    @FXML
    private TextField emgPatientField;
    @FXML
    private ComboBox<String> emgTypeComboBox;
    @FXML
    private ComboBox<String> emgStatusComboBox;
    @FXML
    private Label emgMessageLabel;
    // Table controls - Emergency
    @FXML
    private TableView<EmergencyCase> emergencyTable;
    @FXML
    private TableColumn<EmergencyCase, String> emgIdCol;
    @FXML
    private TableColumn<EmergencyCase, String> emgPatientCol;
    @FXML
    private TableColumn<EmergencyCase, String> emgTypeCol;
    @FXML
    private TableColumn<EmergencyCase, String> emgStatusCol;

    // ===============================================-------
    // Report Panel
    // ===============================================-------
    @FXML
    private HBox reportsPanel;
    @FXML
    private ListView<String> reportTypeListView;
    @FXML
    private TextArea reportPreviewArea;

    // ===============================================-------
    // Appointment panel
    // ===============================================-------
    @FXML
    private HBox appointmentsPanel; // Changed from VBox to HBox to support split screen
    @FXML
    private TextField aptIdField;
    @FXML
    private ComboBox<Patient> aptPatientComboBox;
    @FXML
    private ComboBox<Doctor> aptDoctorComboBox;
    @FXML
    private DatePicker aptDatePicker;
    @FXML
    private ComboBox<String> aptTimeComboBox;
    @FXML
    private ComboBox<String> aptStatusComboBox;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, String> aptIdCol;
    @FXML
    private TableColumn<Appointment, String> aptPatientCol;
    @FXML
    private TableColumn<Appointment, String> aptDoctorCol;
    @FXML
    private TableColumn<Appointment, String> aptDateCol;
    @FXML
    private TableColumn<Appointment, String> aptTimeCol;
    @FXML
    private TableColumn<Appointment, String> aptStatusCol;
    @FXML
    private Label aptMessageLabel;

    @FXML
    public void initialize() {
        // ===============================================
        // App loading default state structure configuration
        // ===============================================
        String errorStyle = "-fx-text-fill: #ef4444";
        errorMessageLabel.setText("");
        errorMessageLabel.setStyle(errorStyle);
        aptMessageLabel.setText("");
        aptMessageLabel.setStyle(errorStyle);
        emgMessageLabel.setText("");
        emgMessageLabel.setStyle(errorStyle);
        billMessageLabel.setText("");
        billMessageLabel.setStyle(errorStyle);

        fillComboBoxes();

        // ===============================================
        // Setting Count Labels
        // ===============================================
        refreshCountLabels();

        // ===============================================
        // home panel users entries
        // ===============================================
        refreshHomePanelData();

        // ===============================================
        // Appointment panel
        // ===============================================
        refreshAppointmentData();

        // ===============================================
        // Emergency panel
        // ===============================================
        refreshEmergencyData();

        // ===============================================
        // Billing panel
        // ===============================================
        refreshBillingData();

        // ===============================================
        // Set avtive nav style
        // ===============================================
        setActiveNavStyle(navHomeBtn);

    }

    // ===============================================
    // fill the comboBoxes with doctors and patiens
    // ===============================================
    private void fillComboBoxes() {
        List<User> users = FileManager.loadRecords(FileManager.getUserFile());
        List<Patient> patients = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("patient"))
                .map(u -> (Patient) u)
                .collect(Collectors.toList());
        aptPatientComboBox.getItems().setAll(patients);

        List<Doctor> doctors = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("doctor"))
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());
        aptDoctorComboBox.getItems().setAll(doctors);
        aptDatePicker.setValue(LocalDate.now());
        // fill comboBox for patients
        billPatientComboBox.getItems().setAll(patients);
    }

    // ===============================================
    // Refresh count labels on home panel
    // ===============================================

    private void refreshCountLabels() {
        List<User> users = FileManager.loadRecords(FileManager.getUserFile());

        List<AdminStaff> adminStaffs = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("admin"))
                .map(u -> (AdminStaff) u)
                .collect(Collectors.toList());

        List<Doctor> doctors = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("doctor"))
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());

        List<Patient> patients = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("patient"))
                .map(u -> (Patient) u)
                .collect(Collectors.toList());

        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        List<EmergencyCase> emergencyCases = FileManager.loadRecords(FileManager.getEmergencyFile());
        adminCountLabel.setText(String.valueOf(adminStaffs.size()));
        doctorCountLabel.setText(String.valueOf(doctors.size()));
        patientCountLabel.setText(String.valueOf(patients.size()));
        aptCountLabel.setText(String.valueOf(appointments.size()));
        emergencyCountLabel.setText(String.valueOf(emergencyCases.size()));
    }

    private void refreshHomePanelData() {
        List<User> users = FileManager.loadRecords(FileManager.getUserFile());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        allUsersTable.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    void handleRegisterUser(ActionEvent event) {
        String username = regNameField.getText();
        String password = passwordField.getText();
        String selectedRole = regRoleComboBox.getValue();
        String specialization = specField.getText();
        String action = actionComboBox.getValue();

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                selectedRole == null || selectedRole.trim().isEmpty() ||
                action == null || action.isEmpty()) {
            errorMessageLabel.setText("Please fill all the fields!");
            return;
        }

        if (action.equalsIgnoreCase("create")) {
            createAndSaveUser(username, password, selectedRole, specialization);
        }

        if (action.equalsIgnoreCase("delete")) {
            handleDeleteUserAction(event);
        }

        regNameField.clear();
        passwordField.clear();
        regRoleComboBox.setValue(null);
        specField.clear();
        actionComboBox.setValue(null);

        refreshCountLabels();
        fillComboBoxes();
        System.out.println("Registering: " + username + " | Role: " + selectedRole);

    }

    void handleDeleteUserAction(ActionEvent event) {
        String username = regNameField.getText();
        String selectedRole = regRoleComboBox.getValue(); // e.g., "Patient", "Doctor", "Admin"
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || selectedRole == null) {
            errorMessageLabel.setText("ERROR: Please enter username and select a role!");
            errorMessageLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            return;
        }

        List<User> users = FileManager.loadRecords(FileManager.getUserFile());
        for (User user : users) {
            if (user.getName().equals(username) && !user.getPassword().equals(password)) {
                showError("Password is incorrect!");
                return;
            }
        }

        String targetUser = username.trim();

        // Role ke mutabiq relevant function ko pipeline switch karwadein
        if (selectedRole.equalsIgnoreCase("patient")) {
            deletePatient(targetUser);
        } else if (selectedRole.equalsIgnoreCase("doctor")) {
            deleteDoctor(targetUser);
        } else if (selectedRole.equalsIgnoreCase("admin")) {
            deleteAdmin(targetUser);
        }
    }

    private void deletePatient(String username) {
        List<User> users = FileManager.loadRecords(FileManager.getUserFile());

        // Exact username match check
        User patient = users.stream()
                .filter(u -> u.getName().equals(username) && u.getRole().equalsIgnoreCase("patient"))
                .findFirst().orElse(null);

        if (patient == null) {
            showError("ERROR: Patient username not found!");
            return;
        }

        // Load only patient-linked files
        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        List<Bill> bills = FileManager.loadRecords(FileManager.getBillFile());
        List<MedicalHistory> histories = FileManager.loadRecords(FileManager.getHistoryFile());

        // Cascade delete using exact matching
        appointments.removeIf(apt -> apt.getApptPatient() != null && apt.getApptPatient().equals(username));
        bills.removeIf(bill -> bill.getPatientName() != null && bill.getPatientName().equals(username));
        histories.removeIf(h -> h.getPatientName() != null && h.getPatientName().equals(username));
        users.remove(patient);

        // Save changes
        FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
        FileManager.saveRecords(FileManager.getBillFile(), bills);
        FileManager.saveRecords(FileManager.getHistoryFile(), histories);
        FileManager.saveRecords(FileManager.getUserFile(), users);

        showSuccess("Patient and all financial/medical profiles wiped successfully!");
    }

    private void deleteDoctor(String username) {
        List<User> users = FileManager.loadRecords(FileManager.getUserFile());

        // Doctor profile filtering logic (Safe wrapper matching)
        User doctor = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("doctor") && u.getName().contains(username))
                .findFirst().orElse(null);

        if (doctor == null) {
            showError("ERROR: Doctor matching that name pattern not found!");
            return;
        }

        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());

        // Match exactly what was stored in the doctor name profile registry
        String doctorFullName = doctor.getName();
        appointments.removeIf(apt -> apt.getApptDoctor() != null && apt.getApptDoctor().equals(doctorFullName));

        users.remove(doctor);

        FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
        FileManager.saveRecords(FileManager.getUserFile(), users);

        showSuccess("Doctor profile and queued assignments deleted successfully!");
    }

    private void deleteAdmin(String username) {
        if (!username.equals("admin")) {
            showError("SECURITY ERROR: Only Super Admin account can manage other admin roles!");
            return;
        }

        List<User> users = FileManager.loadRecords(FileManager.getUserFile());
        boolean removed = users.removeIf(u -> u.getName().equals("admin") && u.getRole().equalsIgnoreCase("admin"));

        if (removed) {
            FileManager.saveRecords(FileManager.getUserFile(), users);
            showSuccess("Admin system account removed from registry.");
        } else {
            showError("ERROR: Admin profile instance missing!");
        }
    }

    // ===============================================
    // Success and Error messages for user registration
    // ===============================================

    private void showError(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
        refreshCountLabels();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            errorMessageLabel.setText("");
            showHomePanel(null);
        });
        delay.play();
    }

    // ===============================================
    // Logout Handler
    // ===============================================
    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Admin logged out.");
        SessionManager.clearCredentials();
        SceneManager.switchScene("login", "Hospital Care - Portal Login");
    }

    // Helper methods
    private void createAndSaveUser(String username, String password, String selectedRole, String specField) {

        if (selectedRole.equalsIgnoreCase("doctor")) {
            if (specField == null || specField.trim().isEmpty()) {
                errorMessageLabel.setText("Please fill in the specialization field!");
                return;
            }
        }

        // 1. Load the existing user registry from the file system
        List<User> users = FileManager.loadRecords(FileManager.getUserFile());

        // 2. Check if username already exists to prevent duplicates
        boolean userExists = users.stream().anyMatch(u -> u.getName().equalsIgnoreCase(username));
        if (userExists) {
            showError("Username is already taken!");
            return;
        }

        // 3. Generate a clean, short unique identifier prefix (e.g., ADM-8f2a1b)
        String uniqueIdSuffix = java.util.UUID.randomUUID().toString().substring(0, 6);
        User newUser = null;

        // 4. Instantiate the correct subclass object based on selection
        if (selectedRole.equalsIgnoreCase("admin")) {
            String finalId = "ADM-" + uniqueIdSuffix;
            newUser = new AdminStaff(finalId, username, selectedRole, password);
        } else if (selectedRole.equalsIgnoreCase("doctor")) {
            String finalId = "DOC-" + uniqueIdSuffix;
            newUser = new Doctor(finalId, username, specField, password);
        } else if (selectedRole.equalsIgnoreCase("patient")) {
            String finalId = "PAT-" + uniqueIdSuffix;
            newUser = new Patient(finalId, username, password);
        } else {
            showError("Please select a correct Role!");
            return;
        }

        if (newUser != null) {
            users.add(newUser);
            FileManager.saveRecords(FileManager.getUserFile(), users);

            // Clear UI inputs and show success
            regNameField.clear();
            passwordField.clear();
            showSuccess("User registered successfully with ID: " + newUser.getId());
            refreshCountLabels();

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                errorMessageLabel.setText("");
                errorMessageLabel.setStyle("-fx-text-fill: #ef4444");
                showHomePanel(e);
            });
            delay.play();

        }
    }

    // ===============================================
    // Appointments panel
    // ===============================================
    @FXML
    private void handleScheduleAppointment() {
        String aptId = aptIdField.getText();
        String aptPatient = (aptPatientComboBox.getValue() != null) ? aptPatientComboBox.getValue().toString() : null;
        String aptDoctor = (aptDoctorComboBox.getValue() != null) ? aptDoctorComboBox.getValue().toString() : null;
        String aptDate = (aptDatePicker.getValue() != null) ? aptDatePicker.getValue().toString() : null;
        String aptTime = aptTimeComboBox.getValue();
        String aptStatus = aptStatusComboBox.getValue();

        if (aptPatient == null || aptPatient.trim().isEmpty() ||
                aptDoctor == null || aptDoctor.trim().isEmpty() ||
                aptDate == null || aptDate.trim().isEmpty() ||
                aptTime == null || aptTime.trim().isEmpty() ||
                aptStatus == null || aptStatus.trim().isEmpty()) {

            showErrorApt("Please fill all the fields!");
            return;
        }

        if (aptDatePicker.getValue().isBefore(java.time.LocalDate.now())) {
            showErrorApt("Appointment date cannot be in the past!");
            return;
        }

        // todo: add the status changed logic here
        if (aptStatus.equalsIgnoreCase("scheduled")) {
            // aptStatus = sheduled
            aptSheduledHandler(aptId, aptPatient, aptDoctor, aptDate, aptTime, aptStatus);
        } else if (aptStatus.equalsIgnoreCase("completed")) {
            // aptStatus = completed
            aptCompletedHandler(aptId);
        } else {
            // aptStatus = cancelled
            aptCancelledHandler(aptId);
        }

        // clear appointment form fields for next entry
        aptDatePicker.setValue(null);
        aptPatientComboBox.setValue(null);
        aptDoctorComboBox.setValue(null);
        aptTimeComboBox.setValue(null);
        aptStatusComboBox.setValue(null);

        // refresh table for new appointment
        refreshAppointmentData();

        System.out.println("Schedule appointment logic completed successfully.");
    }

    // private void showError(String message) {

    private void showErrorApt(String message) {
        aptMessageLabel.setText(message);
        aptMessageLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
    }

    private void showSuccessApt(String message) {
        aptMessageLabel.setText(message);
        aptMessageLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
        refreshCountLabels();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            aptMessageLabel.setText("");
        });
        delay.play();
    }

    // Appointment status handlers

    private void aptSheduledHandler(String aptId, String aptPatient, String aptDoctor, String aptDate, String aptTime,
            String aptStatus) {
        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        Appointment appointment = new Appointment(aptId, aptPatient, aptDoctor, aptDate, aptTime, aptStatus);
        appointments.add(appointment);
        FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
        showSuccessApt("Appointment Scheduled Successfully");
    }

    private void aptCompletedHandler(String aptId) {
        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        List<Bill> bills = FileManager.loadRecords(FileManager.getBillFile());
        List<MedicalHistory> medicalHistories = FileManager.loadRecords(FileManager.getHistoryFile());

        for (Appointment appointment : appointments) {
            if (appointment.getApptId().equals(aptId)) {
                appointment.setApptStatus("Completed");

                // Save medical history
                String uniqueIdSuffix = UUID.randomUUID().toString().substring(0, 15);
                String finalId = "HISTORY-" + uniqueIdSuffix;

                MedicalHistory medicalHistory = new MedicalHistory(finalId, appointment.getApptDate(),
                        appointment.getApptDoctor(), "None", appointment.getApptPatient());
                medicalHistories.add(medicalHistory);
                FileManager.saveRecords(FileManager.getHistoryFile(), medicalHistories);

                String uniqueIdSuffixBill = UUID.randomUUID().toString().substring(0, 15);
                String finalIdBill = "BILL-" + uniqueIdSuffixBill;

                double totalAmount = 500.0; // Base Consultation Fee
                String billServiceDescription = "Doctor Consultation Fee ( Routine Checkup )";
                Bill bill = new Bill(finalIdBill, appointment.getApptPatient(), billServiceDescription, 1, totalAmount);
                bills.add(bill);

                FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
                FileManager.saveRecords(FileManager.getBillFile(), bills);
                showSuccessApt("Appointment completed successfully!");
                return;
            }
        }
        showErrorApt("No matching appointment ID found!");
    }

    private void aptCancelledHandler(String aptId) {
        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        for (Appointment appointment : appointments) {
            // ID base matching logic (Bug Fixed)
            if (appointment.getApptId().equals(aptId)) {
                appointment.setApptStatus("Cancelled");
                FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
                showSuccessApt("Appointment cancelled successfully");
                return;
            }
        }
        showErrorApt("No matching appointment ID found!");
    }

    private void refreshAppointmentData() {
        if (aptIdField != null) {
            aptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            aptPatientCol.setCellValueFactory(new PropertyValueFactory<>("apptPatient"));
            aptDoctorCol.setCellValueFactory(new PropertyValueFactory<>("apptDoctor"));
            aptDateCol.setCellValueFactory(new PropertyValueFactory<>("apptDate"));
            aptTimeCol.setCellValueFactory(new PropertyValueFactory<>("apptTime"));
            aptStatusCol.setCellValueFactory(new PropertyValueFactory<>("apptStatus"));
        }
        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
    }

    // ===============================================
    // Billing Panel
    // ===============================================
    private void refreshBillingData() {
        if (billIdCol != null) {
            billIdCol.setCellValueFactory(new PropertyValueFactory<>("billId"));
            billPatientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
            billServiceCol.setCellValueFactory(new PropertyValueFactory<>("service"));
            billQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            billTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        }
        List<Bill> bills = FileManager.loadRecords(FileManager.getBillFile());
        billingTable.setItems(FXCollections.observableArrayList(bills));
    }

    @FXML
    private void handleCalculateBill() {
        String serviceString = billServiceComboBox.getValue();
        String qtyText = billQuantityField.getText();

        if (serviceString == null || qtyText.trim().isEmpty()) {
            billMessageLabel.setText("Please select a service and input quantity first!");
            billMessageLabel.setStyle("-fx-text-fill: #dc2626;");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText.trim());
            if (qty <= 0)
                throw new NumberFormatException();

            double pricePerUnit = 0;
            if (billServiceComboBox.getValue().contains("Consultation")) {
                pricePerUnit = 500.0;
            } else if (billServiceComboBox.getValue().contains("Blood Test")) {
                pricePerUnit = 300.0;
            } else if (billServiceComboBox.getValue().contains("X-Ray Scan")) {
                pricePerUnit = 1200.0;
            } else if (billServiceComboBox.getValue().contains("ER Operational Care")) {
                pricePerUnit = 2500.0;
            } else if (billServiceComboBox.getValue().contains("General Ward Stay")) {
                pricePerUnit = 1500.0;
            } else {
                pricePerUnit = 0.0;
            }

            double absoluteTotal = pricePerUnit * qty;
            totalAmountLabel.setText("Total Amount: Rs." + absoluteTotal);
            billMessageLabel.setText(""); // clear warning if success

        } catch (NumberFormatException e) {
            billMessageLabel.setText("Invalid entry! Quantity must be a valid positive integer.");
            billMessageLabel.setStyle("-fx-text-fill: #dc2626;");
        }
    }

    @FXML
    private void handleGenerateBill() {
        String billId = billIdField.getText();
        String patientName = (billPatientComboBox.getValue() != null) ? billPatientComboBox.getValue().toString()
                : null;
        String serviceString = (billServiceComboBox.getValue() != null) ? billServiceComboBox.getValue() : null;
        String qtyText = billQuantityField.getText();

        // Structural input check guarantees safety
        if (patientName == null || serviceString == null || qtyText.trim().isEmpty()) {
            billMessageLabel.setText("All billing information fields are mandatory!");
            billMessageLabel.setStyle("-fx-text-fill: #dc2626;");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText.trim());

            // Parse final price evaluation dynamically
            double pricePerUnit = 0;
            if (billServiceComboBox.getValue().contains("Consultation")) {
                pricePerUnit = 500.0;
            } else if (billServiceComboBox.getValue().contains("Blood Test")) {
                pricePerUnit = 300.0;
            } else if (billServiceComboBox.getValue().contains("X-Ray Scan")) {
                pricePerUnit = 1200.0;
            } else if (billServiceComboBox.getValue().contains("ER Operational Care")) {
                pricePerUnit = 2500.0;
            } else if (billServiceComboBox.getValue().contains("General Ward Stay")) {
                pricePerUnit = 1500.0;
            } else {
                pricePerUnit = 0.0;
            }
            double finalTotal = pricePerUnit * qty;

            List<Bill> ledgerList = FileManager.loadRecords(FileManager.getBillFile());
            if (ledgerList == null)
                ledgerList = new ArrayList<>();

            String shortServiceName = serviceString.split(" - ")[0];

            Bill rawBill = new Bill(billId, patientName, shortServiceName, qty, finalTotal);
            ledgerList.add(rawBill);

            FileManager.saveRecords(FileManager.getBillFile(), ledgerList);
            billingTable.getItems().add(rawBill);

            refreshBillingData();

            billMessageLabel.setText("Invoice Generated and Recorded Successfully!");
            billMessageLabel.setStyle("-fx-text-fill: #16a34a;");

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                billMessageLabel.setText("");
                showHomePanel(null);
            });
            delay.play();

            billPatientComboBox.setValue(null);
            billServiceComboBox.setValue(null);
            billQuantityField.setText("1");

        } catch (Exception ex) {
            billMessageLabel.setText("Transaction processing failed check calculation values.");
            billMessageLabel.setStyle("-fx-text-fill: #dc2626;");
        }
    }

    // ===============================================
    // Emergency Panel
    // ===============================================

    private void refreshEmergencyData() {
        if (emgIdCol != null) {
            emgIdCol.setCellValueFactory(new PropertyValueFactory<>("caseId"));
            emgPatientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
            emgTypeCol.setCellValueFactory(new PropertyValueFactory<>("emergencyType"));
            emgStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
        List<EmergencyCase> emergencyCases = FileManager.loadRecords(FileManager.getEmergencyFile());
        emergencyTable.setItems(FXCollections.observableArrayList(emergencyCases));
    }

    @FXML
    private void handleAdmitEmergency() {
        System.out.println("Emergency button clicked.");

        String caseId = emgCaseIdField.getText();
        String patientName = emgPatientField.getText();

        String emgType = (emgTypeComboBox.getValue() != null) ? emgTypeComboBox.getValue().toString() : null;
        String emgStatus = (emgStatusComboBox.getValue() != null) ? emgStatusComboBox.getValue().toString() : null;

        String priority = "CRITICAL - IMMEDIATE ACTION";

        if (patientName == null || patientName.trim().isEmpty() ||
                emgType == null || emgType.trim().isEmpty() ||
                emgStatus == null || emgStatus.trim().isEmpty()) {

            emgMessageLabel.setText("Please fill all required fields!");
            emgMessageLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;"); // Crimson Red color
            return; // CRITICAL: Stop further execution if validation fails
        }

        try {
            // 3. Load existing records, append new case, and save back to file
            List<EmergencyCase> cases = FileManager.loadRecords(FileManager.getEmergencyFile());

            // Agar pehle se koi record na ho toh list initialize karein safely
            if (cases == null) {
                cases = new ArrayList<>();
            }

            EmergencyCase newCase = new EmergencyCase(caseId, patientName, emgType, priority, emgStatus);
            cases.add(newCase);

            FileManager.saveRecords(FileManager.getEmergencyFile(), cases);

            emergencyTable.getItems().add(newCase);

            emgMessageLabel.setText("Emergency Case Registered & Admitted!");
            emgMessageLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");

            emgPatientField.clear();
            emgTypeComboBox.setValue(null);
            emgStatusComboBox.setValue(null);

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                emgMessageLabel.setText("");
                showHomePanel(null);
            });
            delay.play();

        } catch (Exception ex) {
            System.out.println("System saving failed inside Emergency Handler: " + ex.getMessage());
            emgMessageLabel.setText("System Error: Failed to save record!");
            emgMessageLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
        }
    }

    // ===============================================
    // Report Panel
    // ===============================================

    @FXML
    private void handleGenerateReport() {
        String selectedReport = reportTypeListView.getSelectionModel().getSelectedItem();

        if (selectedReport == null) {
            reportPreviewArea.setText("Please select a report type from the list first!");
            return;
        }

        String timestamp = LocalDateTime.now().toString().replace("T", " ");

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("         HOSPITAL MANAGEMENT SYSTEM          \n");
        sb.append(" Report Generated: ").append(timestamp).append("\n");
        sb.append("==================================================\n\n");

        String cleanName = selectedReport.replaceAll("[^a-zA-Z ]", "").trim();
        sb.append(" 📊 ").append(cleanName.toUpperCase()).append("\n");
        sb.append(" ------------------------------------------------\n");

        try {
            int totalUsers = FileManager.loadRecords(FileManager.getUserFile()) != null
                    ? FileManager.loadRecords(FileManager.getUserFile()).size()
                    : 0;
            int totalAppointments = FileManager.loadRecords(FileManager.getAppointmentFile()) != null
                    ? FileManager.loadRecords(FileManager.getAppointmentFile()).size()
                    : 0;
            int totalEmergency = FileManager.loadRecords(FileManager.getEmergencyFile()) != null
                    ? FileManager.loadRecords(FileManager.getEmergencyFile()).size()
                    : 0;
            int totalBills = FileManager.loadRecords(FileManager.getBillFile()) != null
                    ? FileManager.loadRecords(FileManager.getBillFile()).size()
                    : 0;

            if (selectedReport.contains("Patient")) {
                sb.append(" Total Registered Patients in system: ").append(totalUsers).append("\n");
                sb.append(" Status: Directory Active and fully operational.\n");
            } else if (selectedReport.contains("Doctor")) {
                sb.append(" Total Active Medical Specialist Staff: ").append(totalUsers).append("\n");
                sb.append(" Guard: Verification validation clear.\n");
            } else if (selectedReport.contains("Appointment")) {
                sb.append(" Total Appointments scheduled log: ").append(totalAppointments).append("\n");
                if (totalAppointments == 0) {
                    sb.append(" Status Alert: No active scheduled appointments detected.\n");
                }
            } else if (selectedReport.contains("Revenue") || selectedReport.contains("Billing")) {
                sb.append(" Total Invoiced Bill Logs Processed: ").append(totalBills).append("\n");
                sb.append(" Financial Status: Ledger balanced safely.\n");
            } else if (selectedReport.contains("Emergency")) {
                sb.append(" Total Emergency Trauma Admissions: ").append(totalEmergency).append("\n");
                sb.append(" Unit Status: High Alert / Active Monitoring.\n");
            } else {
                sb.append(" [SYSTEM DIRECTORY MATRIX OVERVIEW]\n");
                sb.append("  • Active Database Users   : ").append(totalUsers).append("\n");
                sb.append("  • Appointments Logged    : ").append(totalAppointments).append("\n");
                sb.append("  • Emergency ER Trauma    : ").append(totalEmergency).append("\n");
                sb.append("  • Generated Bills Count  : ").append(totalBills).append("\n");
            }

        } catch (Exception ex) {
            sb.append("System Error reading database files: ").append(ex.getMessage()).append("\n");
        }

        sb.append("\n==================================================\n");
        sb.append(" *** End of Generated Report *** \n");
        sb.append("==================================================");

        // Output formatted final text representation directly to preview area layout
        // window
        reportPreviewArea.setText(sb.toString());
    }

    @FXML
    private void handleExportReport() {
        String reportContent = reportPreviewArea.getText();

        if (reportContent == null || reportContent.trim().isEmpty() || reportContent.contains("Select a report type")) {
            reportPreviewArea.setText("Cannot Export! Please generate a valid report first.");
            return;
        }

        String selectedReport = reportTypeListView.getSelectionModel().getSelectedItem();
        String fileName = "Hospital_Report.txt";

        if (selectedReport != null) {
            fileName = selectedReport.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(reportContent);

            reportPreviewArea.appendText("\n\n==================================================");
            reportPreviewArea.appendText("\nSUCCESS: Report exported successfully as: " + fileName);
            reportPreviewArea.appendText("\n==================================================");
            System.out.println("Report exported successfully as " + fileName);

        } catch (IOException e) {
            System.out.println("Failed to write text data to file system: " + e.getMessage());
            reportPreviewArea.appendText("\n\nEXPORT ERROR: " + e.getMessage());
        }
    }

    // ===============================================
    // Different screens methods
    // ===============================================

    private void setActiveNavStyle(Button btn) {
        btn.setStyle(
                "-fx-background-color: #1e293b; -fx-text-fill: #f8fafc; -fx-padding: 10px 15px; -fx-background-radius: 6px;");
    }

    private void setPassiveNavStyle(Button btn) {
        btn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-padding: 10px 15px; -fx-background-radius: 6px;");
    }

    private void setAllInactiveButtons(List<Button> buttons) {
        for (Button button : buttons) {
            setPassiveNavStyle(button);
        }
    }

    @FXML
    void showHomePanel(ActionEvent event) {
        registrationPanel.setVisible(false);
        registrationPanel.setManaged(false);
        homePanel.setVisible(true);
        homePanel.setManaged(true);
        setActiveNavStyle(navHomeBtn);
        List<Button> buttons = new ArrayList<>();
        buttons.add(navAppointmentsBtn);
        buttons.add(navBillingBtn);
        buttons.add(navEmergencyBtn);
        buttons.add(navRegisterBtn);
        buttons.add(navReportsBtn);
        setAllInactiveButtons(buttons);
        hideAllPanels();
        homePanel.setVisible(true);
        homePanel.setManaged(true);
        refreshHomePanelData();
    }

    @FXML
    void showRegisterForm(ActionEvent event) {
        homePanel.setVisible(false);
        homePanel.setManaged(false);
        registrationPanel.setVisible(true);
        registrationPanel.setManaged(true);
        setActiveNavStyle(navRegisterBtn);
        List<Button> buttons = new ArrayList<>();
        buttons.add(navAppointmentsBtn);
        buttons.add(navBillingBtn);
        buttons.add(navEmergencyBtn);
        buttons.add(navHomeBtn);
        buttons.add(navReportsBtn);
        setAllInactiveButtons(buttons);
        hideAllPanels();
        registrationPanel.setVisible(true);
        registrationPanel.setManaged(true);
    }

    @FXML
    void showAppointmentsPanel(ActionEvent event) {
        setActiveNavStyle(navAppointmentsBtn);
        List<Button> buttons = new ArrayList<>();
        buttons.add(navHomeBtn);
        buttons.add(navBillingBtn);
        buttons.add(navEmergencyBtn);
        buttons.add(navRegisterBtn);
        buttons.add(navReportsBtn);
        setAllInactiveButtons(buttons);
        hideAllPanels();
        appointmentsPanel.setVisible(true);
        appointmentsPanel.setManaged(true);

        String uniqueIdSuffix = java.util.UUID.randomUUID().toString().substring(0, 15);
        String finalId = "APT-" + uniqueIdSuffix;
        aptIdField.setText(finalId);

    }

    @FXML
    void showBillingPanel(ActionEvent event) {
        setActiveNavStyle(navBillingBtn);
        System.out.println("Switched to Financial Invoices Ledger Interface.");
        List<Button> buttons = new ArrayList<>();
        buttons.add(navAppointmentsBtn);
        buttons.add(navHomeBtn);
        buttons.add(navEmergencyBtn);
        buttons.add(navRegisterBtn);
        buttons.add(navReportsBtn);
        setAllInactiveButtons(buttons);

        String uniqueIdSuffix = java.util.UUID.randomUUID().toString().substring(0, 15);
        String finalId = "BILL-" + uniqueIdSuffix;
        billIdField.setText(finalId);

        hideAllPanels();
        billingPanel.setVisible(true);
        billingPanel.setManaged(true);

        totalAmountLabel.setText("Total Amount: Rs. 0");
        billMessageLabel.setText("");
        refreshBillingData();
    }

    @FXML
    void showEmergencyPanel(ActionEvent event) {
        setActiveNavStyle(navEmergencyBtn);
        System.out.println("Switched to Trauma Emergency Bypass Process Matrix.");
        List<Button> buttons = new ArrayList<>();
        buttons.add(navAppointmentsBtn);
        buttons.add(navBillingBtn);
        buttons.add(navHomeBtn);
        buttons.add(navRegisterBtn);
        buttons.add(navReportsBtn);
        setAllInactiveButtons(buttons);
        hideAllPanels();
        emergencyPanel.setVisible(true);
        emergencyPanel.setManaged(true);
        String uniqueIdSuffix = java.util.UUID.randomUUID().toString().substring(0, 15);
        String finalId = "EMG-" + uniqueIdSuffix;
        emgCaseIdField.setText(finalId);
    }

    @FXML
    void showReportsPanel(ActionEvent event) {
        setActiveNavStyle(navReportsBtn);
        System.out.println("Switched to Master Clinical Performance Report View Engine.");
        List<Button> buttons = new ArrayList<>();
        buttons.add(navAppointmentsBtn);
        buttons.add(navBillingBtn);
        buttons.add(navEmergencyBtn);
        buttons.add(navRegisterBtn);
        buttons.add(navHomeBtn);
        setAllInactiveButtons(buttons);
        hideAllPanels();
        reportsPanel.setVisible(true);
        reportsPanel.setManaged(true);

        reportPreviewArea.setText("==================================================\n" +
                "        SMART HOSPITAL MANAGEMENT SYSTEM          \n" +
                "==================================================\n" +
                " Select a report type from the left menu and click\n" +
                " 'Generate Report' to view live system summaries.\n" +
                "==================================================");
    }

    private void hideAllPanels() {
        homePanel.setVisible(false);
        homePanel.setManaged(false);
        registrationPanel.setVisible(false);
        registrationPanel.setManaged(false);
        appointmentsPanel.setVisible(false);
        appointmentsPanel.setManaged(false);
        billingPanel.setVisible(false);
        billingPanel.setManaged(false);
        emergencyPanel.setVisible(false);
        emergencyPanel.setManaged(false);
        reportsPanel.setVisible(false);
        reportsPanel.setManaged(false);
    }

}
