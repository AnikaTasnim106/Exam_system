package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Exam;
import com.buet.edutrack.models.Question;
import com.buet.edutrack.models.Result;
import com.buet.edutrack.services.ExamService;
import com.buet.edutrack.services.QuestionService;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.utils.SceneManager;
import com.buet.edutrack.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewResultsController {
    @FXML
    private Label totalExamsLabel;
    @FXML
    private Label avgScoreLabel;
    @FXML
    private Label bestGradeLabel;
    @FXML
    private TableView<ResultRow> resultsTable;
    @FXML
    private TableColumn<ResultRow, String> examTitleColumn;
    @FXML
    private TableColumn<ResultRow, String> subjectColumn;
    @FXML
    private TableColumn<ResultRow, String> scoreColumn;
    @FXML
    private TableColumn<ResultRow, String> percentageColumn;
    @FXML
    private TableColumn<ResultRow, String> gradeColumn;
    @FXML
    private TableColumn<ResultRow, String> dateColumn;
    @FXML
    private TableColumn<ResultRow, Void> actionsColumn;
    @FXML
    private VBox emptyStateBox;
    private ObservableList<ResultRow> resultRows = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        loadResults();
    }

    private void setupTable() {
        examTitleColumn.setCellValueFactory(new PropertyValueFactory<>("examTitle"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        examTitleColumn.setCellFactory(column -> createStyledCell("black", false));
        subjectColumn.setCellFactory(column -> createStyledCell("black", true));
        scoreColumn.setCellFactory(column -> createStyledCell("black", true));
        percentageColumn.setCellFactory(column -> createStyledCell("black", true));
        dateColumn.setCellFactory(column -> createStyledCell("#b0b0b0", false));
        gradeColumn.setCellFactory(column -> new TableCell<ResultRow, String>() {
            @Override
            protected void updateItem(String grade, boolean empty) {
                super.updateItem(grade, empty);
                if (empty || grade == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(grade);
                    String color = getGradeColor(grade);
                    setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 16px;");
                }
            }
        });
        actionsColumn.setCellFactory(column -> new TableCell<ResultRow, Void>() {
            private final Button viewButton = new Button("View Details");

            {
                viewButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;");
                viewButton.setOnAction(event -> {
                    ResultRow row = getTableView().getItems().get(getIndex());
                    showResultDetails(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });
        resultsTable.setItems(resultRows);
    }

    private TableCell<ResultRow, String> createStyledCell(String color, boolean bold) {
        return new TableCell<ResultRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: " + color + (bold ? "; -fx-font-weight: bold;" : ";"));
                }
            }
        };
    }

    private String getGradeColor(String grade) {
        switch (grade) {
            case "A+":
            case "A":
                return "#00ff00";
            case "B":
                return "#00C851";
            case "C":
                return "#ffa500";
            case "D":
                return "#ff6b6b";
            case "F":
                return "#ff0000";
            default:
                return "red";
        }
    }

    private void loadResults() {
        String currentUsername = SessionManager.getCurrentUsername();
        if (currentUsername == null) {
            currentUsername = "student";
        }
        List<Result> results = ResultService.getResultsByStudent(currentUsername);
        if (results.isEmpty()) {
            showEmptyState();
            return;
        }
        hideEmptyState();
        int totalExams = results.size();
        double totalPercentage = 0;
        String bestGrade = "F";
        double bestPercentage = 0;
        for (Result result : results) {
            totalPercentage += result.getPercentage();
            if (result.getPercentage() > bestPercentage) {
                bestPercentage = result.getPercentage();
                bestGrade = result.getGrade();
            }
            Exam exam = ExamService.getExamById(result.getExamId());
            if (exam != null) {
                ResultRow row = new ResultRow(result, exam);
                resultRows.add(row);
            }
        }
        double avgPercentage = totalPercentage / totalExams;
        totalExamsLabel.setText(String.valueOf(totalExams));
        avgScoreLabel.setText(String.format("%.1f%%", avgPercentage));
        bestGradeLabel.setText(bestGrade);
    }

    private void showEmptyState() {
        resultsTable.setVisible(false);
        resultsTable.setManaged(false);
        emptyStateBox.setVisible(true);
        emptyStateBox.setManaged(true);
        totalExamsLabel.setText("0");
        avgScoreLabel.setText("0%");
        bestGradeLabel.setText("-");
    }

    private void hideEmptyState() {
        resultsTable.setVisible(true);
        resultsTable.setManaged(true);
        emptyStateBox.setVisible(false);
        emptyStateBox.setManaged(false);
    }

    private void showResultDetails(ResultRow row) {
        Result result = row.getResult();
        Exam exam = row.getExam();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exam Details");
        alert.setHeaderText(exam.getTitle());
        StringBuilder content = new StringBuilder();
        content.append("Subject: ").append(exam.getSubject()).append("\n");
        content.append("Date: ").append(row.getDate()).append("\n\n");
        content.append("Score: ").append(result.getCorrectAnswers()).append(" / ").append(result.getTotalQuestions()).append("\n");
        content.append("Percentage: ").append(String.format("%.2f%%", result.getPercentage())).append("\n");
        content.append("Grade: ").append(result.getGrade()).append("\n");
        content.append("Time Taken: ").append(result.getTimeTaken() / 60).append(" min ").append(result.getTimeTaken() % 60).append(" sec\n\n");
        content.append("--- Answer Details ---\n\n");
        List<Question> questions = QuestionService.getAllQuestions();
        int qNum = 1;
        for (String questionId : exam.getQuestionIds()) {
            Question q = questions.stream()
                    .filter(question -> question.getId().equals(questionId))
                    .findFirst()
                    .orElse(null);
            if (q != null) {
                String studentAnswer = result.getStudentAnswers().getOrDefault(questionId, "Not answered");
                boolean isCorrect = studentAnswer.equals(q.getCorrectAnswer());
                content.append("Q").append(qNum++).append(": ").append(q.getQuestionText()).append("\n");
                content.append("Your Answer: ").append(studentAnswer);
                content.append(isCorrect ? " ✓\n" : " ✗\n");
                if (!isCorrect) {
                    content.append("Correct Answer: ").append(q.getCorrectAnswer()).append("\n");
                }
                content.append("\n");
            }
        }
        alert.setContentText(content.toString());
        alert.getDialogPane().setPrefWidth(600);
        alert.getDialogPane().setPrefHeight(500);
        alert.showAndWait();
    }

    @FXML
    private void handleTakeExam() {
        SceneManager.switchScene("/views/exam-list.fxml");
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/student-dashboard.fxml");
    }

    public static class ResultRow {
        private final Result result;
        private final Exam exam;

        public ResultRow(Result result, Exam exam) {
            this.result = result;
            this.exam = exam;
        }

        public Result getResult() {
            return result;
        }

        public Exam getExam() {
            return exam;
        }

        public String getExamTitle() {
            return exam.getTitle();
        }

        public String getSubject() {
            return exam.getSubject();
        }

        public String getScore() {
            return result.getCorrectAnswers() + " / " + result.getTotalQuestions();
        }

        public String getPercentage() {
            return String.format("%.2f%%", result.getPercentage());
        }

        public String getGrade() {
            return result.getGrade();
        }

        public String getDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            return result.getSubmittedAt().format(formatter);
        }
    }
}