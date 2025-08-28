package org.example.personalexpensetracker.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.personalexpensetracker.dao.UserDao;
import org.example.personalexpensetracker.entity.Session;
import org.example.personalexpensetracker.entity.User;

import java.io.IOException;

public class ProfileController {

    @FXML
    private Label addressLabel;

    @FXML
    private Button closeBtn;

    @FXML
    private Label createdLabel;

    @FXML
    private Button editBtn;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label updatedLabel;

    @FXML
    private Label usernameLabel;

    private UserDao userDao = new UserDao();
    @FXML
    public void initialize() {
        User user = Session.getUser();
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        addressLabel.setText(user.getAddress());
        createdLabel.setText(user.getCreate_time().toString());
        updatedLabel.setText(user.getUpdate_time().toString());
    }
    @FXML
    void closeBtnClicked(ActionEvent event) {
        ((Stage) usernameLabel.getScene().getWindow()).close();
    }

    @FXML
    void editBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/edit-profile.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Edit Profile");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Stage) usernameLabel.getScene().getWindow()));
        stage.showAndWait();

        // AFTER edit window closes, reload user from DB
        User updatedUser = userDao.getById(Session.getUser().getUser_id());
        Session.setUser(updatedUser); // update session

        // refresh labels
        refreshProfileView();
    }

    private void refreshProfileView() {
        User user = Session.getUser();
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhone());
        addressLabel.setText(user.getAddress());
        createdLabel.setText(user.getCreate_time().toString());
        updatedLabel.setText(user.getUpdate_time().toString());
    }


}
