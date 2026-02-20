package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Exam;
import com.buet.edutrack.models.Question;
import com.buet.edutrack.services.ExamService;
import com.buet.edutrack.services.QuestionService;
import com.buet.edutrack.utils.SceneManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExamController {
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<String> subjectCombo;
    @FXML
    private Spinner<Integer> durationSpinner;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label errorLabel;
    @FXML
    private Label selectedCountLabel;
    @FXML
    private ComboBox<String> filterSubjectCombo;
    @FXML
    private ComboBox<String> filterDifficultyCombo;
    @FXML
    private TableView<QuestionRow> questionsTable;
    @FXML
    private TableColumn<QuestionRow, Boolean> selectColumn;
    @FXML
    private TableColumn<QuestionRow, String> subjectColumn;
    @FXML
    private TableColumn<QuestionRow, String> questionColumn;
    @FXML
    private TableColumn<QuestionRow, String> difficultyColumn;
    @FXML
    private TableColumn<QuestionRow, String> answerColumn;
    private ObservableList<QuestionRow> questionRows = FXCollections.observableArrayList();
    private Map<String, Boolean> selectedQuestions = new HashMap<>();

    @FXML
    public void initialize() {
        setupSubjectCombo();
        setupDurationSpinner();
        setupFilters();
        setupTable();
        loadQuestions();
    }

    private void setupSubjectCombo() {
        subjectCombo.getItems().addAll(
                "Mathematics", "Physics", "Chemistry", "Biology",
                "Computer Science", "English", "History", "Geography"
        );
    }

    private void setupDurationSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 180, 60, 5);
        durationSpinner.setValueFactory(valueFactory);
    }

    private void setupFilters() {
        filterSubjectCombo.getItems().add("All Subjects");
        filterSubjectCombo.getItems().addAll(
                "Mathematics", "Physics", "Chemistry", "Biology",
                "Computer Science", "English", "History", "Geography"
        );
        filterSubjectCombo.setValue("All Subjects");
        filterDifficultyCombo.getItems().addAll("All Levels", "Easy", "Medium", "Hard");
        filterDifficultyCombo.setValue("All Levels");
    }

    private void setupTable() {
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        subjectColumn.setCellFactory(column -> new TableCell<QuestionRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                }
            }
        });
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        questionColumn.setCellFactory(column -> new TableCell<QuestionRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.length() > 60 ? item.substring(0, 60) + "..." : item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        difficultyColumn.setCellFactory(column -> new TableCell<QuestionRow, String>() {
            @Override
            protected void updateItem(String difficulty, boolean empty) {
                super.updateItem(difficulty, empty);
                if (empty || difficulty == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(difficulty);
                    switch (difficulty) {
                        case "Easy":
                            setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;");
                            break;
                        case "Medium":
                            setStyle("-fx-text-fill: #ffa500; -fx-font-weight: bold;");
                            break;
                        case "Hard":
                            setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                            break;
                    }
                }
            }
        });
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        answerColumn.setCellFactory(column -> new TableCell<QuestionRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                }
            }
        });
        questionsTable.setEditable(true);
        questionsTable.setItems(questionRows);
    }

    private void loadQuestions() {
        List<Question> allQuestions = QuestionService.getAllQuestions();
        questionRows.clear();
        for (Question q : allQuestions) {
            QuestionRow row = new QuestionRow(q);
            row.selectedProperty().addListener((obs, oldVal, newVal) -> updateSelectedCount());
            questionRows.add(row);
        }
        updateSelectedCount();
    }

    @FXML
    private void handleFilterChange() {
        String selectedSubject = filterSubjectCombo.getValue();
        String selectedDifficulty = filterDifficultyCombo.getValue();
        List<Question> allQuestions = QuestionService.getAllQuestions();
        questionRows.clear();
        for (Question q : allQuestions) {
            boolean matchesSubject = "All Subjects".equals(selectedSubject) ||
                    q.getSubject().equals(selectedSubject);
            boolean matchesDifficulty = "All Levels".equals(selectedDifficulty) ||
                    q.getDifficulty().equals(selectedDifficulty);
            if (matchesSubject && matchesDifficulty) {
                QuestionRow row = new QuestionRow(q);
                row.selectedProperty().addListener((obs, oldVal, newVal) -> updateSelectedCount());
                questionRows.add(row);
            }
        }
        updateSelectedCount();
    }

    @FXML
    private void handleSelectAll() {
        for (QuestionRow row : questionRows) {
            row.setSelected(true);
        }
        updateSelectedCount();
    }

    @FXML
    private void handleDeselectAll() {
        for (QuestionRow row : questionRows) {
            row.setSelected(false);
        }
        updateSelectedCount();
    }

    private void updateSelectedCount() {
        long count = questionRows.stream().filter(QuestionRow::isSelected).count();
        selectedCountLabel.setText(count + " question" + (count != 1 ? "s" : "") + " selected");
    }

    @FXML
    private void handleCreateExam() {
        errorLabel.setVisible(false);
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showError("Please enter exam title!");
            return;
        }
        if (subjectCombo.getValue() == null) {
            showError("Please select a subject!");
            return;
        }
        int duration = durationSpinner.getValue();
        if (duration < 5) {
            showError("Duration must be at least 5 minutes!");
            return;
        }
        List<String> selectedQuestionIds = questionRows.stream()
                .filter(QuestionRow::isSelected)
                .map(row -> row.getQuestion().getId())
                .toList();

        if (selectedQuestionIds.isEmpty()) {
            showError("Please select at least one question!");
            return;
        }
        Exam exam = new Exam(
                title,
                subjectCombo.getValue(),
                descriptionArea.getText().trim(),
                duration,
                "teacher" // TODO: Get from logged-in user
        );
        exam.setQuestionIds(selectedQuestionIds);
        boolean saved = ExamService.addExam(exam);
        if (saved) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Exam created successfully!\n\n" +
                    "Title: " + title + "\n" +
                    "Questions: " + selectedQuestionIds.size() + "\n" +
                    "Duration: " + duration + " minutes");
            alert.showAndWait();
            SceneManager.switchScene("/views/teacher-dashboard.fxml");
        } else {
            showError("Failed to create exam. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        SceneManager.switchScene("/views/teacher-dashboard.fxml");
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/teacher-dashboard.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    public static class QuestionRow {
        private final Question question;
        private final SimpleBooleanProperty selected;

        public QuestionRow(Question question) {
            this.question = question;
            this.selected = new SimpleBooleanProperty(false);
        }

        public Question getQuestion() {
            return question;
        }

        public boolean isSelected() {
            return selected.get();
        }

        public void setSelected(boolean value) {
            selected.set(value);
        }

        public SimpleBooleanProperty selectedProperty() {
            return selected;
        }

        public String getSubject() {
            return question.getSubject();
        }

        public String getQuestionText() {
            return question.getQuestionText();
        }

        public String getDifficulty() {
            return question.getDifficulty();
        }

        public String getCorrectAnswer() {
            return question.getCorrectAnswer();
        }
    }
}