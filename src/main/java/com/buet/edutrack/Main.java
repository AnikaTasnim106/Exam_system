package com.buet.edutrack;

import com.buet.edutrack.utils.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
        Scene scene = new Scene(root, 1200, 700);
        try {
            String css = getClass().getResource("/css/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("CSS not found - running without styling");
        }
        primaryStage.setTitle("EduTrack - Online Exam System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}