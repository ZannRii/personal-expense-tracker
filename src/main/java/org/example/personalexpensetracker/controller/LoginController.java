package org.example.personalexpensetracker.controller;

import com.mysql.cj.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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
    private PasswordField passwordField;

    @FXML
    private Button signupBtn;
    private int id ;
    private User user;
    private UserDao userDao = new UserDao();
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

@FXML
void loginBtnClicked(ActionEvent event) throws IOException {
    String email = emailField.getText().trim();
    String password = passwordField.getText().trim();

    if (email.isEmpty() || password.isEmpty()) {
        new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING,
                "Email and password cannot be empty!"
        ).showAndWait();
        return;
    }

    User user = userDao.findByEmail(email);

    if (user == null || !password.equals(user.getPassword())) {
        loginAttempts++; // increment failed attempts
        if (loginAttempts >= MAX_ATTEMPTS) {
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Maximum login attempts exceeded! Application will close."
            ).showAndWait();
            System.exit(0); // close the application
        } else {
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING,
                    "Invalid email or password! Attempt " + loginAttempts + " of " + MAX_ATTEMPTS
            ).showAndWait();
        }
        return;
    }

    // Reset attempts on successful login
    loginAttempts = 0;

    // Create session and load home
    org.example.personalexpensetracker.entity.Session session = new org.example.personalexpensetracker.entity.Session();
    session.setUser(user);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/home.fxml"));
    Parent root = loader.load();
    HomeController homeController = loader.getController();
    CommonUtil.getPrimaryStage().setScene(new Scene(root));
    CommonUtil.setResizable(false);
    CommonUtil.setHomeController(homeController);
    homeController.loadIncomeChart();
    homeController.loadExpenseChart();
    homeController.loadSummaryData();
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
