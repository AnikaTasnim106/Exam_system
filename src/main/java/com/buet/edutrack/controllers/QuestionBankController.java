package com.buet.edutrack.controllers;

import com.buet.edutrack.models.Question;
import com.buet.edutrack.services.QuestionService;
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

public class QuestionBankController {

    @FXML private TableView<Question> questionsTable;
    @FXML private TableColumn<Question, String> subjectColumn;
    @FXML private TableColumn<Question, String> questionColumn;
    @FXML private TableColumn<Question, String> difficultyColumn;
    @FXML private TableColumn<Question, String> correctAnswerColumn;
    @FXML private TableColumn<Question, Void> actionsColumn;
    @FXML private ComboBox<String> subjectFilterCombo;
    @FXML private Label questionCountLabel;
    @FXML private VBox emptyStateBox;

    private ObservableList<Question> questionsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupSubjectFilter();
        loadQuestions();
    }

    private void setupTable() {
        // Configure columns
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

        // Style SUBJECT column - WHITE TEXT
        subjectColumn.setCellFactory(column -> new TableCell<Question, String>() {
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

        // Style QUESTION column - WHITE TEXT
        questionColumn.setCellFactory(column -> new TableCell<Question, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });

        // Style DIFFICULTY column - COLORED TEXT
        difficultyColumn.setCellFactory(column -> new TableCell<Question, String>() {
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
                            setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                            break;
                    }
                }
            }
        });

        // Style CORRECT ANSWER column - WHITE TEXT
        correctAnswerColumn.setCellFactory(column -> new TableCell<Question, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
                }
            }
        });

        // Add action buttons (View & Delete)
        actionsColumn.setCellFactory(column -> new TableCell<Question, Void>() {
            private final Button viewButton = new Button("View");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(10, viewButton, deleteButton);

            {
                viewButton.setStyle("-fx-background-color: #6c63ff; -fx-text-fill: white; -fx-padding: 5 15; -fx-background-radius: 5; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-padding: 5 15; -fx-background-radius: 5; -fx-cursor: hand;");

                viewButton.setOnAction(event -> {
                    Question question = getTableView().getItems().get(getIndex());
                    showQuestionDetails(question);
                });

                deleteButton.setOnAction(event -> {
                    Question question = getTableView().getItems().get(getIndex());
                    handleDeleteQuestion(question);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        questionsTable.setItems(questionsList);
    }

    private void setupSubjectFilter() {
        subjectFilterCombo.getItems().addAll(
                "All Subjects", "Mathematics", "Physics", "Chemistry", "Biology",
                "Computer Science", "English", "History", "Geography"
        );
        subjectFilterCombo.setValue("All Subjects");

        subjectFilterCombo.setOnAction(e -> filterQuestions());
    }

    private void loadQuestions() {
        List<Question> questions = QuestionService.getAllQuestions();

        // DEBUG
        System.out.println("=== LOADING QUESTIONS ===");
        System.out.println("Total questions: " + questions.size());
        for (Question q : questions) {
            System.out.println("Subject: " + q.getSubject());
            System.out.println("Question: " + q.getQuestionText());
            System.out.println("Difficulty: " + q.getDifficulty());
            System.out.println("Answer: " + q.getCorrectAnswer());
            System.out.println("---");
        }

        questionsList.setAll(questions);
        updateQuestionCount();
        toggleEmptyState();
    }

    private void filterQuestions() {
        String selected = subjectFilterCombo.getValue();

        if ("All Subjects".equals(selected)) {
            loadQuestions();
        } else {
            List<Question> filtered = QuestionService.getQuestionsBySubject(selected);
            questionsList.setAll(filtered);
            updateQuestionCount();
        }
    }

    private void updateQuestionCount() {
        questionCountLabel.setText("Total Questions: " + questionsList.size());
    }

    private void toggleEmptyState() {
        boolean isEmpty = questionsList.isEmpty();
        questionsTable.setVisible(!isEmpty);
        questionsTable.setManaged(!isEmpty);
        emptyStateBox.setVisible(isEmpty);
        emptyStateBox.setManaged(isEmpty);
    }

    private void showQuestionDetails(Question question) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Question Details");
        alert.setHeaderText(question.getSubject() + " - " + question.getDifficulty());

        String content = "Question: " + question.getQuestionText() + "\n\n" +
                "A) " + question.getOptionA() + "\n" +
                "B) " + question.getOptionB() + "\n" +
                "C) " + question.getOptionC() + "\n" +
                "D) " + question.getOptionD() + "\n\n" +
                "Correct Answer: " + question.getCorrectAnswer() + "\n\n" +
                "Explanation: " + question.getExplanation();

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleDeleteQuestion(Question question) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Question");
        confirmAlert.setHeaderText("Are you sure?");
        confirmAlert.setContentText("This will permanently delete this question.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = QuestionService.deleteQuestion(question.getId());

            if (deleted) {
                loadQuestions();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Question deleted successfully!");
                successAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleShowAll() {
        subjectFilterCombo.setValue("All Subjects");
        loadQuestions();
    }

    @FXML
    private void handleAddQuestion() {
        SceneManager.switchScene("/views/add-question.fxml");
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/views/teacher-dashboard.fxml");
    }
}