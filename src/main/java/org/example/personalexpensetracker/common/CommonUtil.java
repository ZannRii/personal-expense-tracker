package org.example.personalexpensetracker.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class CommonUtil {
    private static Stage primaryStage;
    public CommonUtil(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    public static Stage getPrimaryStage(){
        return primaryStage;
    }
    public static void setResizable(boolean isResizable){
        primaryStage.setResizable(isResizable);
    }
    public static Parent loadFxmlLayout(String fxml ){
        FXMLLoader fxmlLoader = new FXMLLoader(CommonUtil.class.getResource(fxml));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
