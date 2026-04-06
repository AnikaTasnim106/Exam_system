package com.buet.edutrack.controllers;

import com.buet.edutrack.models.ForumPost;
import com.buet.edutrack.services.ForumService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ForumListController{
    @FXML private ComboBox<String> subjectFilterCombo;
    @FXML private Label postCountLabel;
    @FXML private VBox postsContainer;
    @FXML private Button newPostButton;
    @FXML private Button backButton;

    @FXML
    public void initialize(){
        setupSubjectFilter();
        loadPosts();
        if(SessionManager.getCurrentUserRole().equals("Teacher")){
            newPostButton.setVisible(false);
        }
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2d1b69; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20;"));
        newPostButton.setOnMouseEntered(e -> newPostButton.setStyle("-fx-background-color: #5848ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;"));
        newPostButton.setOnMouseExited(e -> newPostButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20;"));
    }

    private void setupSubjectFilter(){
        subjectFilterCombo.getItems().add("All Subjects");
        subjectFilterCombo.getItems().addAll(
                "Mathematics", "Physics", "Chemistry", "Biology", "Computer Science", "English", "History", "Geography"
        );
        subjectFilterCombo.setValue("All Subjects");
    }

    private void loadPosts(){
        List<ForumPost> posts = ForumService.getAllPosts();
        displayPosts(posts);
    }

    @FXML
    private void handleFilter(){
        String selected = subjectFilterCombo.getValue();
        List<ForumPost> posts;

        if("All Subjects".equals(selected)){
            posts = ForumService.getAllPosts();
        }
        else{
            posts = ForumService.getPostsBySubject(selected);
        }
        displayPosts(posts);
    }

    private void displayPosts(List<ForumPost> posts){
        postsContainer.getChildren().clear();
        postCountLabel.setText("Showing " + posts.size() + " posts");
        for(ForumPost post: posts){
            postsContainer.getChildren().add(createPostCard(post));
        }
    }

    private HBox createPostCard(ForumPost post){
        HBox card = new HBox();
        card.setStyle("-fx-background-color: #2b2b3c; -fx-background-radius: 8;");
        card.setSpacing(15);
        card.setPadding(new Insets(20));

        VBox leftBox = new VBox();
        leftBox.setSpacing(8);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        Label titleLabel = new Label(post.getTitle());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label subjectBadgeLabel = new Label(post.getSubject());
        subjectBadgeLabel.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 3 12; -fx-background-radius: 12;");

        Label metaLabel = new Label("Posted by: " + post.getPostedBy() + " • " + post.getPostedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        metaLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 13px;");

        leftBox.getChildren().addAll(titleLabel, subjectBadgeLabel, metaLabel);

        VBox rightBox = new VBox();
        rightBox.setSpacing(3);
        rightBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Label replyCountLabel = new Label(String.valueOf(post.getReplies().size()));
        replyCountLabel.setStyle("-fx-text-fill: #6c63ff; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label repliesLabel = new Label("replies");
        repliesLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 13px;");

        rightBox.getChildren().addAll(replyCountLabel, repliesLabel);

        card.getChildren().addAll(leftBox, rightBox);

        card.setOnMouseClicked(e -> handleOpenPost(post));
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #363648; -fx-cursor: hand; -fx-background-radius: 8;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #2b2b3c; -fx-background-radius: 8;"));

        return card;
    }

    @FXML
    private void handleNewPost(){
        SceneManager.switchScene("/views/forum-new-post.fxml");
    }

    @FXML
    private void handleBack(){
        if(SessionManager.getCurrentUserRole().equals("Student")){
            SceneManager.switchScene("/views/student-dashboard.fxml");
        }
        else if(SessionManager.getCurrentUserRole().equals("Teacher")){
            SceneManager.switchScene("/views/teacher-dashboard.fxml");
        }
    }

    private void  handleOpenPost(ForumPost post){
        ForumThreadController.setCurrentPost(post);
        SceneManager.switchScene("/views/forum-thread.fxml");
    }
}
