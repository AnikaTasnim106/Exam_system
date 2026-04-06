package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Result;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.services.UserService;
import com.buet.edutrack.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.buet.edutrack.models.Exam;
import com.buet.edutrack.services.ExamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.control.Button;

public class LeaderboardController {
    @FXML private ComboBox<String> subjectFilterCombo;
    @FXML private VBox leaderboardContainer;
    @FXML private Label titleLabel;
    @FXML private Button backButton;

    @FXML
    public void initialize(){
        setupSubjectFilter();
        loadLeaderboard();
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2d1b69; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 10 20;"));

    }
    private void setupSubjectFilter(){
        subjectFilterCombo.getItems().add("All Subjects");
        subjectFilterCombo.getItems().addAll("Mathematics", "Physics", "Chemistry", "Biology", "Computer Science", "English", "History", "Geography");
        subjectFilterCombo.setValue("All Subjects");
    }

    private void loadLeaderboard(){
        String selected = subjectFilterCombo.getValue();
        List<Result> results;
        if("All Subjects".equals(selected)){
            results = ResultService.getAllResults();
            titleLabel.setText("Overall Leaderboard");
        } else{
            results = getResultsBySubject(selected);
            titleLabel.setText(selected + " Leaderboard");
        }

        Map<String, List<Result>> resultsByStudent = new HashMap<>();
        for(Result result: results){
            String username = result.getStudentUsername();
            if(!resultsByStudent.containsKey(username)){
                resultsByStudent.put(username, new ArrayList<>());
            }
            resultsByStudent.get(username).add(result);
        }
        displayLeaderboard(resultsByStudent);
    }

    private List<Result> getResultsBySubject(String subject){
        List<Result> allResults = ResultService.getAllResults();
        List<Result> filtered = new ArrayList<>();

        for(Result result: allResults){
            Exam exam = ExamService.getExamById(result.getExamId());
            if(exam != null && exam.getSubject().equals(subject)){
                filtered.add(result);
            }
        }
        return filtered;
    }

    private void displayLeaderboard(Map<String, List<Result>> resultsByStudent){
        leaderboardContainer.getChildren().clear();

        List<double[]> studentScores = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();

        for(Map.Entry<String, List<Result>> entry: resultsByStudent.entrySet()){
            String username = entry.getKey();
            List<Result> results = entry.getValue();

            double total = 0;
            for(Result r: results){
                total += r.getPercentage();
            }
            double average = total/results.size();

            studentNames.add(username);
            studentScores.add(new double[]{average, results.size()});
        }

        for(int i = 0; i < studentNames.size() - 1; i++){
            for(int j = i+1; j < studentNames.size(); j++){
                if(studentScores.get(j)[0] > studentScores.get(i)[0]){
                    double[] tempScore = studentScores.get(i);
                    studentScores.set(i, studentScores.get(j));
                    studentScores.set(j, tempScore);

                    String tempName = studentNames.get(i);
                    studentNames.set(i, studentNames.get(j));
                    studentNames.set(j, tempName);
                }
            }
        }
        for(int i = 0; i < studentNames.size(); i++){
            HBox card = createStudentCard(i+1, studentNames.get(i), studentScores.get(i));
            leaderboardContainer.getChildren().add(card);

        }
    }

    private HBox createStudentCard(int rank, String username, double[] scores){
        HBox card = new HBox();
        card.setSpacing(20);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setAlignment(Pos.CENTER_LEFT);

        if(rank == 1){
            card.setStyle("-fx-background-color: #2b2b3c; -fx-border-color: #FFD700; -fx-border-width: 0 0 0 4; -fx-background-radius: 8;");
        }
        else if(rank == 2){
            card.setStyle("-fx-background-color: #2b2b3c; -fx-border-color: #C0C0C0; -fx-border-width: 0 0 0 4; -fx-background-radius: 8;");
        }
        else if(rank == 3){
            card.setStyle("-fx-background-color: #2b2b3c; -fx-border-color: #CD7F32; -fx-border-width: 0 0 0 4; -fx-background-radius: 8;");
        }

        String rankEmoji = rank == 1 ? "\uD83E\uDD47" : rank == 2 ? "\uD83E\uDD48" : rank == 3? "\uD83E\uDD49": "#" + rank;
        Label rankLabel = new Label(rankEmoji);
        rankLabel.setStyle("-fx-font-size: 22px;");
        rankLabel.setPrefWidth(60);

        Label nameLabel = new Label(username);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setPrefWidth(300);

        Label scoreLabel = new Label(String.format("%.1f%%", scores[0]));
        scoreLabel.setStyle("-fx-text-fill: #6c63ff; -fx-font-size: 18px; -fx-font-weight: bold;");
        scoreLabel.setPrefWidth(150);

        Label examsLabel = new Label((int)scores[1] + "exams");
        examsLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 13px;");

        card.getChildren().addAll(rankLabel, nameLabel, scoreLabel, examsLabel);
        return card;
    }

    @FXML
    private void handleFilter(){
        loadLeaderboard();
    }

    @FXML
    private void handleBack(){
        SceneManager.switchScene("/views/student-dashboard.fxml");
    }

    @FXML
    private void handleLeaderboard(){
        SceneManager.switchScene("/views/leaderboard.fxml");
    }
}
