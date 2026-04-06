package com.buet.edutrack.controllers;

import com.buet.edutrack.models.ForumPost;
import com.buet.edutrack.services.ForumService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
public class ForumNewPostController {
    @FXML private TextField titleField;
    @FXML private ComboBox<String> subjectCombo;
    @FXML private TextArea contentArea;
    @FXML private Label errorLabel;

    @FXML private Button backButton;
    @FXML
    private void initialize(){
        setupSubjectCombo();
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2d1b69; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20;"));
    }

    private void setupSubjectCombo(){
        subjectCombo.getItems().addAll("Mathematics", "Physics", "Chemistry", "Biology", "Computer Science", "English", "History", "Geography");
    }

    @FXML
    private void handleSubmit(){
        String title = titleField.getText().trim();
        String subject = subjectCombo.getValue();
        String content = contentArea.getText();
        if(title.isEmpty()){
            errorLabel.setText("Please enter a title!");
            errorLabel.setVisible(true);
            return;
        }
        if(subject == null){
            errorLabel.setText("Please select a subject!");
            errorLabel.setVisible(true);
            return;
        }
        if(content.isEmpty()){
            errorLabel.setText("Please write the description!");
            errorLabel.setVisible(true);
            return;
        }
        ForumPost newPost = new ForumPost(title, content, subject, SessionManager.getCurrentUsername());
        ForumService.addPost(newPost);
        SceneManager.switchScene("/views/forum-list.fxml");

    }

    @FXML
    private void handleBack(){
        SceneManager.switchScene("/views/forum-list.fxml");
    }
}
