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
        // IMPORTANT: Set up the SceneManager FIRST
        SceneManager.setPrimaryStage(primaryStage);

        // Load the login screen FXML
        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));

        // Create scene and load CSS
        Scene scene = new Scene(root, 1200, 700);

        // Try to load CSS, but don't crash if it fails
        try {
            String css = getClass().getResource("/css/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("CSS not found - running without styling");
        }

        // Set up the stage
        primaryStage.setTitle("EduTrack - Online Exam System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}