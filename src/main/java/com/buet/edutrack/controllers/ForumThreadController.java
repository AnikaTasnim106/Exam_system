package com.buet.edutrack.controllers;

import com.buet.edutrack.models.ForumPost;
import com.buet.edutrack.services.ForumService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.time.format.DateTimeFormatter;

public class ForumThreadController {
    @FXML private Label postTitleLabel;
    @FXML private Label postMetaLabel;
    @FXML private Label postSubjectLabel;
    @FXML private Label postContentLabel;
    @FXML private VBox repliesContainer;
    @FXML private TextArea replyArea;
    @FXML private Label replyErrorLabel;

    private static ForumPost currentPost;

    public static void setCurrentPost(ForumPost post){
        currentPost = post;
    }

    @FXML
    private void initialize(){
        if(currentPost == null){
            SceneManager.switchScene("/views/forum-list.fxml");
            return;
        }
        postTitleLabel.setText(currentPost.getTitle());
        postSubjectLabel.setText(currentPost.getSubject());
        postMetaLabel.setText("Posted by: " + currentPost.getPostedBy() + " • " + currentPost.getPostedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        postContentLabel.setText(currentPost.getContent());
        loadReplies();

    }

    private void loadReplies(){
        repliesContainer.getChildren().clear();
        for(ForumPost.Reply reply : currentPost.getReplies()){
            repliesContainer.getChildren().add(createReplyCard(reply));
        }
    }

    private VBox createReplyCard(ForumPost.Reply reply){
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        if(reply.isTeacher()){
            card.setStyle("-fx-background-color: #2b2b3c; -fx-border-color: #ffa500; -fx-border-width: 0 0 0 4; -fx-background-radius: 8;");
        }
        else{
            card.setStyle("-fx-background-color: #2b2b3c; -fx-border-color: #6c63ff; -fx-border-width: 0 0 0 4; -fx-background-radius: 8;");
        }
        Label metaLabel = new Label(reply.getRepliedBy() + " • " + reply.getRepliedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        metaLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 13px;");
        if(reply.isTeacher()){
            Label teacherBadge = new Label("👨‍🏫 Teacher");
            teacherBadge.setStyle("-fx-text-fill: #ffa500; -fx-font-weight: bold; -fx-font-size: 12px;");
            card.getChildren().add(teacherBadge);
        }
        Label content = new Label(reply.getContent());
        content.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        content.setWrapText(true);
        card.getChildren().add(metaLabel);
        card.getChildren().add(content);
        return card;
    }

    @FXML
    private void handleSubmitReply(){
        String replyText = replyArea.getText();
        if(replyText.isEmpty()){
            replyErrorLabel.setText("Please write a reply!");
            replyErrorLabel.setVisible(true);
            return;
        }
        ForumPost.Reply reply = new ForumPost.Reply(replyText, SessionManager.getCurrentUsername(), SessionManager.getCurrentUserRole().equals("Teacher"));
        currentPost.addReply(reply);
        ForumService.savePost(currentPost);
        loadReplies();
        replyArea.clear();

    }

    @FXML
    private void handleBack(){
        SceneManager.switchScene("/views/forum-list.fxml");
    }
}
