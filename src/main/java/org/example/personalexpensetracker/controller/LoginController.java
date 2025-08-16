package org.example.personalexpensetracker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.personalexpensetracker.common.CommonUtil;
import org.example.personalexpensetracker.dao.UserDao;
import org.example.personalexpensetracker.entity.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signupBtn;
    private UserDao userDao = new UserDao();
    @FXML
    void loginBtnClicked(ActionEvent event) {
        System.out.println("login clicked");
        System.out.println("UserName"+ emailField.getText());
        System.out.println("Password"+ passwordField.getText());
        String email = emailField.getText();
        User user = userDao.findByEmail(email);
        if (emailField.getText().equals(user.getEmail()) && passwordField.getText().equals(user.getPassword())){
            CommonUtil.setResizable(true);
            CommonUtil.getPrimaryStage().setScene(new Scene(CommonUtil.loadFxmlLayout("home.fxml")));
        }
    }

    @FXML
    void signupBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/user-create.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Create User");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

}
