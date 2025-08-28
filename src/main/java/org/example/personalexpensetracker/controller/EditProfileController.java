package org.example.personalexpensetracker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.personalexpensetracker.dao.UserDao;
import org.example.personalexpensetracker.entity.Session;
import org.example.personalexpensetracker.entity.User;

public class EditProfileController {

    @FXML
    private TextField addressField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField usernameField;

    private User user;

    @FXML
    public void initialize() {
        user = Session.getUser();

        // Fill fields with current data
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        addressField.setText(user.getAddress());
    }

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    @FXML
    void saveBtnClicked(ActionEvent event) {
        // Update user object
        user.setEmail(emailField.getText());
        user.setPhone(phoneField.getText());
        user.setAddress(addressField.getText());

        // Update database
        UserDao userDao = new UserDao();
        userDao.update(user);

        // Close the window
        ((Stage) saveBtn.getScene().getWindow()).close();
    }

}
