package com.hospital.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import com.hospital.models.Appointment;
import com.hospital.models.Bill;
import com.hospital.models.Doctor;
import com.hospital.models.MedicalHistory;
import com.hospital.models.User;
import com.hospital.storage.FileManager;
import com.hospital.utils.SceneManager;
import com.hospital.utils.SessionManager;

import java.time.LocalDate;

public class PatientController {

    // Main Portal Header
    @FXML
    private Button logoutBtn;

    // Tab 1: New Appointment Scheduler Form Components (Admin View Style)
    @FXML
    private TextField aptIdField;
    @FXML
    private ComboBox<Doctor> doctorSelectBox; // Dynamic item matching placeholder
    @FXML
    private DatePicker aptDatePicker;
    @FXML
    private ComboBox<String> priorityComboBox; // Acts as Time Slot Selector now
    @FXML
    private ComboBox<String> aptStatusComboBox;
    @FXML
    private Label aptMessageLabel;
    @FXML
    private Button bookAppointmentBtn;

    // Tab 1: Personal Bookings Live Directory Log Table View
    @FXML
    private TableView<Appointment> appointmentsTable; // Model class mapping goes here (e.g., TableView<Appointment>)
    @FXML
    private TableColumn<Appointment, String> aptIdCol;
    @FXML
    private TableColumn<Appointment, String> aptDoctorCol;
    @FXML
    private TableColumn<Appointment, String> aptDateCol;
    @FXML
    private TableColumn<Appointment, String> aptTimeCol;
    @FXML
    private TableColumn<Appointment, String> aptStatusCol;

    // ==========================================
    // Medical History Tab Components
    // ==========================================
    @FXML
    private TableView<MedicalHistory> historyTable;
    @FXML
    private TableColumn<MedicalHistory, String> historyIdCol;
    @FXML
    private TableColumn<MedicalHistory, String> historyDateCol;
    @FXML
    private TableColumn<MedicalHistory, String> historyDoctorCol;
    @FXML
    private TableColumn<MedicalHistory, String> historyDiagnosisCol;

    // ==========================================
    // Invoice Tab Components
    // ==========================================
    // ==========================================
    // Invoice Tab Components (Updated to TableView)
    // ==========================================
    @FXML
    private TableView<Bill> invoiceTable;
    @FXML
    private TableColumn<Bill, String> billItemCol;
    @FXML
    private TableColumn<Bill, Double> billCostCol;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Button payBillBtn;

    /**
     * Scene graph system instantiation hook pipeline.
     */
    @FXML
    public void initialize() {
        refreshAppointmentData();
        // Set default text values setup
        totalCostLabel.setText("Rs. 0.00");
        aptMessageLabel.setText("");

        setupMedicalHistoryColumns();
        loadMedicalHistoryData();
        loadInvoiceData();

        // Auto generation mock string or timestamp placeholder for field mapping
        String uniqueIdSuffix = UUID.randomUUID().toString().substring(0, 15);
        String finalId = "APT-" + uniqueIdSuffix;
        aptIdField.setText(finalId);
        aptDatePicker.setValue(LocalDate.now());

        List<User> users = FileManager.loadRecords(FileManager.getUserFile());
        List<Doctor> doctors = users.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("doctor"))
                .map(u -> (Doctor) u)
                .collect(Collectors.toList());

        doctorSelectBox.getItems().addAll(doctors);

    }

    // ========================================
    // Appointment Tab
    // ========================================
    @FXML
    void handleBookAppointment(ActionEvent event) {
        String generatedId = aptIdField.getText();
        String selectedDoctor = doctorSelectBox.getValue() != null ? doctorSelectBox.getValue().toString() : null;
        String selectedDate = aptDatePicker.getValue().toString();
        String selectedTimeSlot = priorityComboBox.getValue();
        String selectedStatus = aptStatusComboBox.getValue();

        if (selectedDoctor == null || selectedDoctor.trim().isEmpty() ||
                selectedDate == null || selectedDate.trim().isEmpty() ||
                selectedTimeSlot == null || selectedTimeSlot.trim().isEmpty() ||
                selectedStatus == null || selectedStatus.trim().isEmpty()) {

            showErrorApt("Please fill all the fields!");
            return;
        }

        if (aptDatePicker.getValue().isBefore(java.time.LocalDate.now())) {
            showErrorApt("Appointment date cannot be in the past!");
            return;
        }

        System.out.println("Booking Request Engine -> ID: " + generatedId +
                " | Doctor: " + selectedDoctor +
                " | Date: " + selectedDate +
                " | Slot: " + selectedTimeSlot +
                " | Status: " + selectedStatus);

        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());
        if (selectedStatus.equalsIgnoreCase("scheduled")) {
            aptSheduledHandler(generatedId, SessionManager.getUsername(), selectedDoctor, selectedDate,
                    selectedTimeSlot, selectedStatus, appointments);
        } else {
            aptCancelledHandler(generatedId, appointments);
        }

        refreshAppointmentData();

        doctorSelectBox.setValue(null);
        aptDatePicker.setValue(null);
        priorityComboBox.setValue(null);
        aptStatusComboBox.setValue(null);

        showSuccessApt("");

    }

    private void aptSheduledHandler(String generatedId, String aptPatient, String aptDoctor, String aptDate,
            String aptTime,
            String aptStatus, List<Appointment> appointments) {
        Appointment appointment = new Appointment(generatedId, SessionManager.getUsername(), aptDoctor,
                aptDate, aptTime, aptStatus);
        appointments.add(appointment);
        FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
        showSuccessApt("Appointment Scheduled Successfully!");
    }

    private void aptCancelledHandler(String generatedId, List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            if (appointment.getApptId().equals(generatedId)) {
                appointment.setApptStatus("Completed");
                FileManager.saveRecords(FileManager.getAppointmentFile(), appointments);
                showSuccessApt("Appointment cancelled successfully!");
                return;
            }
        }
    }

    private void showErrorApt(String message) {
        aptMessageLabel.setText(message);
        aptMessageLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
    }

    private void showSuccessApt(String message) {
        aptMessageLabel.setText(message);
        aptMessageLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
        refreshAppointmentData();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> {
            aptMessageLabel.setText(message);
        });
        delay.play();
    }

    /**
     * Action: User safety terminal navigation redirect sequence back to login
     * portal
     */
    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Patient portal session closed safely.");
        SessionManager.clearCredentials();
        SceneManager.switchScene("login", "Hospital Care - Portal Login");
    }

    private void refreshAppointmentData() {
        if (aptIdField != null) {
            aptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            aptDoctorCol.setCellValueFactory(new PropertyValueFactory<>("apptDoctor"));
            aptDateCol.setCellValueFactory(new PropertyValueFactory<>("apptDate"));
            aptTimeCol.setCellValueFactory(new PropertyValueFactory<>("apptTime"));
            aptStatusCol.setCellValueFactory(new PropertyValueFactory<>("apptStatus"));
        }
        List<Appointment> appointments = FileManager.loadRecords(FileManager.getAppointmentFile());

        List<Appointment> filterAppointments = appointments.stream()
                .filter(apt -> apt.getApptPatient().equals(SessionManager.getUsername()))
                .collect(Collectors.toList());
        appointmentsTable.setItems(FXCollections.observableArrayList(filterAppointments));

    }

    // ========================================
    // Medical History Tab
    // ========================================

    private void setupMedicalHistoryColumns() {
        // Model class ki properties ko table columns ke sath map karna
        historyIdCol.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        historyDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyDoctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        historyDiagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
    }

    private void loadMedicalHistoryData() {
        List<MedicalHistory> history = FileManager.loadRecords(FileManager.getHistoryFile());
        List<MedicalHistory> filtereMedicalHistories = history.stream()
                .filter(medicalHistory -> medicalHistory.getPatientName().equals(SessionManager.getUsername()))
                .collect(Collectors.toList());
        historyTable.setItems(FXCollections.observableArrayList(filtereMedicalHistories));
    }

    // ========================================
    // Invoice Tab
    // ========================================

    private void loadInvoiceData() {
        billItemCol.setCellValueFactory(new PropertyValueFactory<>("service"));
        billCostCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        List<Bill> bills = FileManager.loadRecords(FileManager.getBillFile());

        List<Bill> filteredBills = bills.stream()
                .filter(bill -> bill.getPatientName().equals(SessionManager.getUsername()))
                .collect(Collectors.toList());

        invoiceTable.setItems(FXCollections.observableArrayList(filteredBills));

        double totalCost = 0.0;
        for (Bill bill : filteredBills) {
            totalCost += bill.getTotalAmount();
        }

        totalCostLabel.setText(String.format("Rs. %.2f", totalCost));
    }

    /**
     * Action: Accounting balance ledger data wipe operations
     */
    @FXML
    void handlePayment(ActionEvent event) {
        if (invoiceTable.getItems().isEmpty()) {
            System.out.println("No pending bills to clear.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Receipt");
            alert.setHeaderText("No Pending Bills");
            alert.setContentText("You have No Pending Bills to clear!");
            alert.showAndWait();
            return;
        }

        System.out.println("Processing transaction... Clearing invoices and updating database.");

        List<Bill> allBills = FileManager.loadRecords(FileManager.getBillFile());

        allBills.removeIf(bill -> bill.getPatientName().equals(SessionManager.getUsername()));

        FileManager.saveRecords(FileManager.getBillFile(), allBills);

        invoiceTable.getItems().clear();
        totalCostLabel.setText("Rs. 0.00");

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Receipt");
        alert.setHeaderText("Transaction Completed Successfully!");
        alert.setContentText("All outstanding fees have been paid. Your active billing registry is now cleared.");
        alert.showAndWait();
    }
}