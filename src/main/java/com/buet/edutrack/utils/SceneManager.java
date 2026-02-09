package com.buet.edutrack.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) {
        try {
            System.out.println("Switching to: " + fxmlPath); // Debug message

            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            Scene scene = new Scene(root, 1200, 700);

            // ALWAYS add CSS to every scene
            try {
                String css = SceneManager.class.getResource("/css/dark-theme.css").toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception e) {
                System.out.println("CSS not loaded for: " + fxmlPath);
            }

            primaryStage.setScene(scene);
            System.out.println("Scene switched successfully!"); // Debug message

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load scene: " + fxmlPath);
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}




