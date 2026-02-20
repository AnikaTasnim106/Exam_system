package com.buet.edutrack.controllers;

import com.buet.edutrack.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.buet.edutrack.services.UserService;

public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label errorLabel;

    @FXML
    private Button signupButton;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Student", "Teacher");
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        errorLabel.setVisible(false);
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields!");
            return;
        }

        if (role == null) {
            showError("Please select your role!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            return;
        }

        if (password.length() < 3) {
            showError("Password must be at least 3 characters!");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email!");
            return;
        }
        boolean success = UserService.registerUser(username, email, password, role);

        if (!success) {
            showError("Username already exists! Please choose another.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Account created successfully! Please login.");
        alert.showAndWait();
        SceneManager.switchScene("/views/login.fxml");
    }

    @FXML
    private void handleGoToLogin() {
        SceneManager.switchScene("/views/login.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}