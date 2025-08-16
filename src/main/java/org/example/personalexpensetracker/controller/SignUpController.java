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
    private Button saveBtn;
    private User user;
    private UserDao userDao = new UserDao();

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        ((Stage)cancelBtn.getScene().getWindow()).close();
    }

    @FXML
    void saveBtnClicked(ActionEvent event) {
       user = new User();
        user.setName(nameTextField.getText());
        user.setPassword(passwordTextField.getText());
        user.setEmail(emailTextField.getText());
        if (user.getName() == null || user.getPassword() == null || user.getEmail() == null){
            ((Stage)saveBtn.getScene().getWindow()).close();
        }else {
            userDao.insert(user);
        }
        ((Stage)saveBtn.getScene().getWindow()).close();

    }

}
