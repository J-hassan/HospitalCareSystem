package com.hospital.controllers;

import com.hospital.storage.FileManager;
import com.hospital.utils.SceneManager;
import com.hospital.utils.SessionManager;
import com.hospital.models.AdminStaff;
import com.hospital.models.User;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        errorMessageLabel.setText("");

        // Safe check: file_existence runtime parameters validation check
        boolean fileCheck = FileManager.checkFileExistence(FileManager.getUserFile());
        if (!fileCheck) {
            checkAndCreateDefaultAdmin();
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        // 1. Validation: Fields Empty Check
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                selectedRole == null || selectedRole.trim().isEmpty()) {
            errorMessageLabel.setText("Please fill all the fields!");
            return;
        }

        System.out.println("Login Attempt -> User: " + username + " | Role: " + selectedRole);

        List<User> users = FileManager.loadRecords(FileManager.getUserFile());
        boolean userFound = false;

        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                userFound = true;

                // Role checking matrix matching
                if (user.getRole().equalsIgnoreCase(selectedRole)) {
                    System.out.println("Login Successful! Redirecting...");

                    // Route users dynamically based on their roles
                    if (selectedRole.equalsIgnoreCase("ADMIN")) {
                        SessionManager.setCredentials(username, selectedRole);
                        SceneManager.switchScene("admin_dashboard", "Hospital Care - Admin Dashboard");
                    } else if (selectedRole.equalsIgnoreCase("DOCTOR")) {
                        SessionManager.setCredentials(username, selectedRole);
                        SceneManager.switchScene("doctor_dashboard", "Hospital Care - Doctor Dashboard");
                    } else if (selectedRole.equalsIgnoreCase("PATIENT")) {
                        SessionManager.setCredentials(username, selectedRole);
                        SceneManager.switchScene("patient_dashboard", "Hospital Care - Patient Dashboard");
                    }
                    return; // Crucial: Stop method execution after scene switches safely!
                } else {
                    errorMessageLabel.setText("Please select the correct role!");
                    return;
                }
            }
        }

        if (!userFound) {
            errorMessageLabel.setText("Invalid username or password!");
        }
    }

    /**
     * Seeds the system database framework with initial root profiles.
     */
    private void checkAndCreateDefaultAdmin() {
        User user = new AdminStaff("ADM-001", "admin", "ADMIN", "admin");
        List<User> users = new ArrayList<>();
        users.add(user);
        FileManager.saveRecords(FileManager.getUserFile(), users);
        System.out.println("Default Admin account created successfully inside users.dat");
    }
}
