package org.example.personalexpensetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.personalexpensetracker.common.CommonUtil;

import java.io.IOException;
import java.io.InputStream;

public class PersonalExpenseTracker extends Application {
    public PersonalExpenseTracker(){

    }
    public PersonalExpenseTracker(String... args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        InputStream imageStream = getClass().getResourceAsStream("images/logo.png");
        Image image = new Image(imageStream);
        stage.getIcons().add(image);
        new CommonUtil(stage);
        Scene scene = new Scene(CommonUtil.loadFxmlLayout("login.fxml"));
        CommonUtil.getPrimaryStage().setTitle("Personal  Expense Tracker");
        CommonUtil.setResizable(false);
        CommonUtil.getPrimaryStage().setScene(scene);
        CommonUtil.getPrimaryStage().show();
    }
}