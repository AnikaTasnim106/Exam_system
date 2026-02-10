package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Exam;
import com.buet.edutrack.models.Result;
import com.buet.edutrack.services.ExamService;
import com.buet.edutrack.services.ResultService;
import com.buet.edutrack.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class ManageExamsController {

    @FXML private ComboBox<String> subjectFilterCombo;
    @FXML private Label examCountLabel;
    @FXML private TableView<ExamRow> examsTable;
    @FXML private TableColumn<ExamRow, String> titleColumn;
    @FXML private TableColumn<ExamRow, String> subjectColumn;
    @FXML private TableColumn<ExamRow, String> questionsColumn;
    @FXML private TableColumn<ExamRow, String> durationColumn;
    @FXML private TableColumn<ExamRow, String> studentsColumn;
    @FXML private TableColumn<ExamRow, String> dateColumn;
    @FXML private TableColumn<ExamRow, Void> actionsColumn;
    @FXML private VBox emptyStateBox;

    private ObservableList<ExamRow> examRows = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSubjectFilter();
        setupTable();
        loadExams();
    }

    private void setupSubjectFilter() {
        subjectFilterCombo.getItems().add("All Subjects");
        subjectFilterCombo.getItems().addAll(
                "Mathematics", "Physics", "Chemistry", "Biology",
                "Computer Science", "English", "History", "Geography"
        );
        subjectFilterCombo.setValue("All Subjects");
    }

    private void setupTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        questionsColumn.setCellValueFactory(new PropertyValueFactory<>("questions"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        studentsColumn.setCellValueFactory(new PropertyValueFactory<>("students"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Style columns
        titleColumn.setCellFactory(column -> createStyledCell("white", true));
        subjectColumn.setCellFactory(column -> createStyledCell("#6c63ff", true));
        questionsColumn.setCellFactory(column -> createStyledCell("white", false));
        durationColumn.setCellFactory(column -> createStyledCell("white", false));
        studentsColumn.setCellFactory(column -> createStyledCell("#00C851", true));
        dateColumn.setCellFactory(column -> createStyledCell("#b0b0b0", false));

        // Actions column
        actionsColumn.setCellFactory(column -> new TableCell<ExamRow, Void>() {
            private final Button viewButton = new Button("View");
            private final Button resultsButton = new Button("Results");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(8, viewButton, resultsButton, deleteButton);

            {
                viewButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px;");
                resultsButton.setStyle("-fx-background-color: #00C851; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px;");
                deleteButton.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px;");

                viewButton.setOnAction(event -> {
                    ExamRow row = getTableView().getItems().get(getIndex());
                    showExamDetails(row.getExam());
                });

                resultsButton.setOnAction(event -> {
                    ExamRow row = getTableView().getItems().get(getIndex());
                    showExamResults(row.getExam());
                });

                deleteButton.setOnAction(event -> {
                    ExamRow row = getTableView().getItems().get(getIndex());
                    handleDeleteExam(row.getExam());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        examsTable.setItems(examRows);
    }

    private TableCell<ExamRow, String> createStyledCell(String color, boolean bold) {
        return new TableCell<ExamRow, String>() {
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

    private void loadExams() {
        List<Exam> allExams = ExamService.getAllExams();
        displayExams(allExams);
    }

    @FXML
    private void handleFilter() {
        String selected = subjectFilterCombo.getValue();

        List<Exam> exams;
        if ("All Subjects".equals(selected)) {
            exams = ExamService.getAllExams();
        } else {
            exams = ExamService.getExamsBySubject(selected);
        }

        displayExams(exams);
    }

    private void displayExams(List<Exam> exams) {
        examRows.clear();

        if (exams.isEmpty()) {
            showEmptyState();
            return;
        }

        hideEmptyState();

        for (Exam exam : exams) {
            int studentCount = ResultService.getResultsByExam(exam.getId()).size();
            ExamRow row = new ExamRow(exam, studentCount);
            examRows.add(row);
        }

        examCountLabel.setText(exams.size() + " exam" + (exams.size() != 1 ? "s" : ""));
    }

    private void showEmptyState() {
        examsTable.setVisible(false);
        examsTable.setManaged(false);
        emptyStateBox.setVisible(true);
        emptyStateBox.setManaged(true);
        examCountLabel.setText("0 exams");
    }

    private void hideEmptyState() {
        examsTable.setVisible(true);
        examsTable.setManaged(true);
        emptyStateBox.setVisible(false);
        emptyStateBox.setManaged(false);
    }

    private void showExamDetails(Exam exam) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exam Details");
        alert.setHeaderText(exam.getTitle());

        String content = "Subject: " + exam.getSubject() + "\n" +
                "Duration: " + exam.getDuration() + " minutes\n" +
                "Questions: " + exam.getQuestionIds().size() + "\n" +
                "Created: " + exam.getCreatedDate() + "\n\n" +
                "Description:\n" + (exam.getDescription().isEmpty() ? "No description" : exam.getDescription());

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showExamResults(Exam exam) {
        List<Result> results = ResultService.getResultsByExam(exam.getId());
        if (results.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Results");
            alert.setHeaderText(exam.getTitle());
            alert.setContentText("No students have taken this exam yet.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exam Results");
        alert.setHeaderText(exam.getTitle() + " - Student Results");

        StringBuilder content = new StringBuilder();
        content.append("Total Students: ").append(results.size()).append("\n\n");

        // Calculate average
        double totalPercentage = 0;
        for (Result r : results) {
            totalPercentage += r.getPercentage();
        }
        double avgPercentage = totalPercentage / results.size();
        content.append("Average Score: ").append(String.format("%.2f%%", avgPercentage)).append("\n\n");
        content.append("--- Student Details ---\n\n");

        for (Result r : results) {
            content.append("Student: ").append(r.getStudentUsername()).append("\n");
            content.append("Score: ").append(r.getCorrectAnswers()).append(" / ").append(r.getTotalQuestions()).append("\n");
            content.append("Percentage: ").append(String.format("%.2f%%", r.getPercentage())).append("\n");
            content.append("Grade: ").append(r.getGrade()).append("\n");
            content.append("Time Taken: ").append(r.getTimeTaken() / 60).append(" min ").append(r.getTimeTaken() % 60).append(" sec\n\n");
        }

        alert.setContentText(content.toString());
        alert.getDialogPane().setPrefWidth(500);
        alert.getDialogPane().setPrefHeight(500);
        alert.showAndWait();
    }

    private void handleDeleteExam(Exam exam) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Exam");
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("This will permanently delete the exam:\n\"" + exam.getTitle() + "\"");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = ExamService.deleteExam(exam.getId());

            if (deleted) {
                loadExams();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Exam deleted successfully!");
                successAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleCreateNew() {
        SceneManager.switchScene("/views/create-exam.fxml");
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/teacher-dashboard.fxml");
    }

    // Inner class for table rows
    public static class ExamRow {
        private final Exam exam;
        private final int studentCount;

        public ExamRow(Exam exam, int studentCount) {
            this.exam = exam;
            this.studentCount = studentCount;
        }

        public Exam getExam() { return exam; }

        public String getTitle() { return exam.getTitle(); }
        public String getSubject() { return exam.getSubject(); }
        public String getQuestions() { return String.valueOf(exam.getQuestionIds().size()); }
        public String getDuration() { return exam.getDuration() + " min"; }
        public String getStudents() { return String.valueOf(studentCount); }
        public String getDate() {
            return exam.getCreatedDate() != null ? exam.getCreatedDate().substring(0, 10) : "N/A";
        }
    }
}