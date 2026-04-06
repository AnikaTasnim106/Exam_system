package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Result;
import com.buet.edutrack.models.User;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.services.UserService;
import com.buet.edutrack.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class ManageStudentsController {
    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label activeStudentsLabel;
    @FXML
    private Label avgPerformanceLabel;
    @FXML
    private TextField searchField;
    @FXML
    private Label studentCountLabel;
    @FXML
    private TableView<StudentRow> studentsTable;
    @FXML
    private TableColumn<StudentRow, String> usernameColumn;
    @FXML
    private TableColumn<StudentRow, String> emailColumn;
    @FXML
    private TableColumn<StudentRow, String> examsColumn;
    @FXML
    private TableColumn<StudentRow, String> avgScoreColumn;
    @FXML
    private TableColumn<StudentRow, String> bestGradeColumn;
    @FXML
    private TableColumn<StudentRow, String> lastActiveColumn;
    @FXML
    private TableColumn<StudentRow, Void> actionsColumn;
    @FXML
    private VBox emptyStateBox;
    private ObservableList<StudentRow> studentRows = FXCollections.observableArrayList();

    @FXML private Button backButton;
    @FXML
    public void initialize() {
        setupTable();
        loadStudents();
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 10 25; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #2d1b69; -fx-text-fill: white; -fx-padding: 10 25; -fx-background-radius: 8; -fx-font-weight: bold; -fx-border-color: #6c63ff; -fx-border-width: 1; -fx-border-radius: 8;"));
    }

    private void setupTable() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        examsColumn.setCellValueFactory(new PropertyValueFactory<>("examsTaken"));
        avgScoreColumn.setCellValueFactory(new PropertyValueFactory<>("avgScore"));
        bestGradeColumn.setCellValueFactory(new PropertyValueFactory<>("bestGrade"));
        lastActiveColumn.setCellValueFactory(new PropertyValueFactory<>("lastActive"));
        usernameColumn.setCellFactory(col -> createTextCell("black", true));
        emailColumn.setCellFactory(col -> createTextCell("#b0b0b0", false));
        examsColumn.setCellFactory(col -> createTextCell("#6c63ff", true));
        avgScoreColumn.setCellFactory(col -> createTextCell("black", true));
        lastActiveColumn.setCellFactory(col -> createTextCell("#b0b0b0", false));
        bestGradeColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String grade, boolean empty) {
                super.updateItem(grade, empty);
                if (empty || grade == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(grade);
                    String color;
                    if (grade.equals("-")) {
                        color = "#888888";
                    } else if (grade.startsWith("A")) {
                        color = "#00ff00";
                    } else if (grade.startsWith("B")) {
                        color = "#00C851";
                    } else if (grade.startsWith("C")) {
                        color = "#ffa500";
                    } else if (grade.startsWith("D")) {
                        color = "#ff6b6b";
                    } else {
                        color = "#ff0000";
                    }
                    setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 16px;");
                }
            }
        });
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View");

            {
                viewBtn.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;");
                viewBtn.setOnAction(e -> {
                    StudentRow row = getTableView().getItems().get(getIndex());
                    showStudentDetails(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
            }
        });
        studentsTable.setItems(studentRows);
    }

    private TableCell<StudentRow, String> createTextCell(String color, boolean bold) {
        return new TableCell<>() {
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

    private void loadStudents() {
        List<User> allStudents = UserService.getAllUsers().stream()
                .filter(user -> "Student".equals(user.getRole())).collect(Collectors.toList());
        if (allStudents.isEmpty()) {
            showEmptyState();
            return;
        }
        hideEmptyState();
        studentRows.clear();
        int activeCount = 0;
        double totalPercentage = 0;
        int totalResults = 0;
        for (User student : allStudents) {
            List<Result> results = ResultService.getResultsByStudent(student.getUsername());
            double avgScore = 0;
            String bestGrade = "-";
            double bestPercent = 0;
            if (!results.isEmpty()) {
                activeCount++;
                double studentTotal = 0;
                for (Result r : results) {
                    studentTotal += r.getPercentage();
                    totalPercentage += r.getPercentage();
                    totalResults++;
                    if (r.getPercentage() > bestPercent) {
                        bestPercent = r.getPercentage();
                        bestGrade = r.getGrade();
                    }
                }
                avgScore = studentTotal / results.size();
            }
            studentRows.add(new StudentRow(
                    student.getUsername(),
                    student.getEmail(),
                    results.size(),
                    avgScore,
                    bestGrade,
                    results
            ));
        }
        totalStudentsLabel.setText(String.valueOf(allStudents.size()));
        activeStudentsLabel.setText(String.valueOf(activeCount));
        avgPerformanceLabel.setText(totalResults > 0 ?
                String.format("%.1f%%", totalPercentage / totalResults) : "0%");
        studentCountLabel.setText(allStudents.size() + " student" +
                (allStudents.size() != 1 ? "s" : ""));
    }

    @FXML
    private void handleSearch() {
        String search = searchField.getText().toLowerCase().trim();
        if (search.isEmpty()) {
            studentsTable.setItems(studentRows);
            return;
        }
        ObservableList<StudentRow> filtered = studentRows.stream()
                .filter(row -> row.getUsername().toLowerCase().contains(search) ||
                        row.getEmail().toLowerCase().contains(search))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        studentsTable.setItems(filtered);
    }

    private void showStudentDetails(StudentRow row) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Student Details");
        alert.setHeaderText(row.getUsername());
        StringBuilder content = new StringBuilder();
        content.append("Email: ").append(row.getEmail()).append("\n");
        content.append("Exams Taken: ").append(row.getExamsTaken()).append("\n");
        content.append("Average Score: ").append(row.getAvgScore()).append("\n");
        content.append("Best Grade: ").append(row.getBestGrade()).append("\n\n");
        if (row.getResults().isEmpty()) {
            content.append("No exam history yet.");
        } else {
            content.append("--- Exam History ---\n\n");
            for (Result r : row.getResults()) {
                content.append("Score: ").append(r.getCorrectAnswers())
                        .append("/").append(r.getTotalQuestions()).append("\n");
                content.append("Percentage: ").append(String.format("%.2f%%", r.getPercentage())).append("\n");
                content.append("Grade: ").append(r.getGrade()).append("\n");
                content.append("Date: ").append(r.getSubmittedAt().toString().substring(0, 16)).append("\n\n");
            }
        }
        alert.setContentText(content.toString());
        alert.getDialogPane().setPrefWidth(500);
        alert.getDialogPane().setPrefHeight(500);
        alert.showAndWait();
    }

    private void showEmptyState() {
        studentsTable.setVisible(false);
        studentsTable.setManaged(false);
        emptyStateBox.setVisible(true);
        emptyStateBox.setManaged(true);
        totalStudentsLabel.setText("0");
        activeStudentsLabel.setText("0");
        avgPerformanceLabel.setText("0%");
        studentCountLabel.setText("0 students");
    }

    private void hideEmptyState() {
        studentsTable.setVisible(true);
        studentsTable.setManaged(true);
        emptyStateBox.setVisible(false);
        emptyStateBox.setManaged(false);
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/teacher-dashboard.fxml");
    }

    public static class StudentRow {
        private final String username;
        private final String email;
        private final int examsTaken;
        private final double avgScore;
        private final String bestGrade;
        private final List<Result> results;

        public StudentRow(String username, String email, int examsTaken,
                          double avgScore, String bestGrade, List<Result> results) {
            this.username = username;
            this.email = email;
            this.examsTaken = examsTaken;
            this.avgScore = avgScore;
            this.bestGrade = bestGrade;
            this.results = results;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getExamsTaken() {
            return String.valueOf(examsTaken);
        }

        public String getAvgScore() {
            return examsTaken > 0 ? String.format("%.1f%%", avgScore) : "-";
        }

        public String getBestGrade() {
            return bestGrade;
        }

        public String getLastActive() {
            if (results.isEmpty()) return "Never";
            return results.get(results.size() - 1).getSubmittedAt()
                    .toString().substring(0, 10);
        }

        public List<Result> getResults() {
            return results;
        }
    }
}