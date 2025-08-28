package org.example.personalexpensetracker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.personalexpensetracker.dao.UserDao;
import org.example.personalexpensetracker.entity.User;

public class SignUpController {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button saveBtn;
    private User user;
    private UserDao userDao = new UserDao();

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        ((Stage)cancelBtn.getScene().getWindow()).close();
    }


@FXML
void saveBtnClicked(ActionEvent event) {
    String username = nameTextField.getText().trim();
    String password = passwordTextField.getText().trim();
    String email = emailTextField.getText().trim();
    String phone = phoneTextField.getText().trim();
    String address = addressTextField.getText().trim();

    if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
        new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING,
                "Username, password, and email are required!"
        ).showAndWait();
        return;
    }

    // Email validation using regex
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    if (!email.matches(emailRegex)) {
        new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING,
                "Invalid email format!"
        ).showAndWait();
        return;
    }

    user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    user.setPhone(phone);
    user.setAddress(address);

    try {
        userDao.insert(user);

        new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION,
                "Sign-up successful!"
        ).showAndWait();

        ((Stage) saveBtn.getScene().getWindow()).close();

    } catch (Exception e) {
        // Show error alert if DB insert fails
        new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,
                "Failed to sign up! " + e.getMessage()
        ).showAndWait();
    }
}



}
