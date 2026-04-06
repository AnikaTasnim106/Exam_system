package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Question;
import com.buet.edutrack.models.Result;
import com.buet.edutrack.services.QuestionService;
import com.buet.edutrack.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PracticeSessionController {
    @FXML private Label questionLabel;
    @FXML private Label progressLabel;
    @FXML private Label feedbackLabel;
    @FXML private Label subjectLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label optionALabel;
    @FXML private Label optionBLabel;
    @FXML private Label optionCLabel;
    @FXML private Label optionDLabel;
    @FXML private VBox optionABox;
    @FXML private VBox optionBBox;
    @FXML private VBox optionCBox;
    @FXML private VBox optionDBox;
    @FXML private Button nextButton;
    @FXML private ProgressBar progressBar;

    @FXML private Label backLabel;

    private static Result currentResult;
    private List<Question> wrongQuestions;
    private int currentIndex = 0;

    public static void setCurrentResult(Result result){
        currentResult = result;
    }

    @FXML
    public void initialize(){
        if(currentResult == null){
            SceneManager.switchScene("/views/practice.fxml");
            return;
        }
        loadWrongQuestions();
        showQuestion();
        backLabel.setOnMouseEntered(e -> backLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #6c63ff; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8;"));
        backLabel.setOnMouseExited(e -> backLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #2d1b69; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8;"));
    }

    private void loadWrongQuestions(){
        wrongQuestions = new ArrayList<>();
        List<Question> allQuestions = QuestionService.getAllQuestions();
        Map<String, String> studentAnswers = currentResult.getStudentAnswers();
        for(Question question : allQuestions){
            if(studentAnswers.containsKey(question.getId()) && !question.getCorrectAnswer().equals(studentAnswers.get(question.getId()))){
                wrongQuestions.add(question);
            }
        }
    }

    private void showQuestion(){
        if(wrongQuestions.isEmpty() || currentIndex >= wrongQuestions.size()){
            questionLabel.setText("Practice Complete! You reviewed all wrong answers.");
            optionABox.setVisible(false);
            optionBBox.setVisible(false);
            optionCBox.setVisible(false);
            optionDBox.setVisible(false);
            feedbackLabel.setVisible(false);
            nextButton.setVisible(false);
            return;
        }

        Question question = wrongQuestions.get(currentIndex);
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + wrongQuestions.size());
        progressBar.setProgress((double)(currentIndex + 1) / wrongQuestions.size());
        questionLabel.setText(question.getQuestionText());
        subjectLabel.setText(question.getSubject());
        difficultyLabel.setText("Difficulty: " + question.getDifficulty());

        optionALabel.setText(question.getOptionA());
        optionBLabel.setText(question.getOptionB());
        optionCLabel.setText(question.getOptionC());
        optionDLabel.setText(question.getOptionD());

        String defaultStyle = "-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-border-color: #dee2e6; -fx-border-radius: 8; -fx-border-width: 1; -fx-cursor: hand;";
        optionABox.setStyle(defaultStyle);
        optionBBox.setStyle(defaultStyle);
        optionCBox.setStyle(defaultStyle);
        optionDBox.setStyle(defaultStyle);

        optionABox.setVisible(true);
        optionBBox.setVisible(true);
        optionCBox.setVisible(true);
        optionDBox.setVisible(true);

        optionABox.setOnMouseClicked(e -> checkAnswer("A", question));
        optionBBox.setOnMouseClicked(e -> checkAnswer("B", question));
        optionCBox.setOnMouseClicked(e -> checkAnswer("C", question));
        optionDBox.setOnMouseClicked(e -> checkAnswer("D", question));

        feedbackLabel.setVisible(false);
        nextButton.setVisible(false);
    }

    private void checkAnswer(String selected, Question question){
        String selectedStyle = "-fx-background-color: #e3f2fd; -fx-background-radius: 8; -fx-border-color: #2196F3; -fx-border-radius: 8; -fx-border-width: 2; -fx-cursor: hand;";
        String correctStyle = "-fx-background-color: #e8f5e9; -fx-background-radius: 8; -fx-border-color: #00cc66; -fx-border-radius: 8; -fx-border-width: 2; -fx-cursor: hand;";
        String wrongStyle = "-fx-background-color: #ffebee; -fx-background-radius: 8; -fx-border-color: #ff4444; -fx-border-radius: 8; -fx-border-width: 2; -fx-cursor: hand;";

        if(selected.equals(question.getCorrectAnswer())){
            getBoxByLetter(selected).setStyle(selectedStyle);
            feedbackLabel.setText("Correct!");
            feedbackLabel.setStyle("-fx-text-fill: #00cc66; -fx-font-size: 15px; -fx-font-weight: bold;");
        } else {
            getBoxByLetter(selected).setStyle(wrongStyle);
            getBoxByLetter(question.getCorrectAnswer()).setStyle(correctStyle);
            feedbackLabel.setText("Wrong! Correct answer: " + question.getCorrectAnswer() + ". " + getOptionText(question, question.getCorrectAnswer()));
            feedbackLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 15px; -fx-font-weight: bold;");
        }

        feedbackLabel.setVisible(true);
        nextButton.setVisible(true);

        optionABox.setOnMouseClicked(null);
        optionBBox.setOnMouseClicked(null);
        optionCBox.setOnMouseClicked(null);
        optionDBox.setOnMouseClicked(null);
    }

    private VBox getBoxByLetter(String letter){
        switch(letter){
            case "A": return optionABox;
            case "B": return optionBBox;
            case "C": return optionCBox;
            case "D": return optionDBox;
            default: return optionABox;
        }
    }

    private String getOptionText(Question question, String letter){
        switch(letter){
            case "A": return question.getOptionA();
            case "B": return question.getOptionB();
            case "C": return question.getOptionC();
            case "D": return question.getOptionD();
            default: return "";
        }
    }

    @FXML
    private void handleNext(){
        currentIndex++;
        showQuestion();
    }

    @FXML
    private void handleBack(){
        SceneManager.switchScene("/views/practice.fxml");
    }
    @FXML
    private void handleBackLabel() {
        SceneManager.switchScene("/views/practice.fxml");
    }
}