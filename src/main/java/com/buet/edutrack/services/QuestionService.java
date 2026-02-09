package com.buet.edutrack.services;

import com.buet.edutrack.models.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionService {
    private static final String QUESTIONS_FILE = "questions.dat";
    private static List<Question> questions = new ArrayList<>();

    static {
        loadQuestions();
    }

    // Add a new question
    public static boolean addQuestion(Question question) {
        questions.add(question);
        return saveQuestions();
    }

    // Get all questions
    public static List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

    // Get questions by subject
    public static List<Question> getQuestionsBySubject(String subject) {
        return questions.stream()
                .filter(q -> q.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }

    // Get questions by difficulty
    public static List<Question> getQuestionsByDifficulty(String difficulty) {
        return questions.stream()
                .filter(q -> q.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
    }

    // Delete a question
    public static boolean deleteQuestion(String questionId) {
        boolean removed = questions.removeIf(q -> q.getId().equals(questionId));
        if (removed) {
            saveQuestions();
        }
        return removed;
    }

    // Save questions to file
    private static boolean saveQuestions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(QUESTIONS_FILE))) {
            oos.writeObject(questions);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving questions: " + e.getMessage());
            return false;
        }
    }

    // Load questions from file
    @SuppressWarnings("unchecked")
    private static void loadQuestions() {
        File file = new File(QUESTIONS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(QUESTIONS_FILE))) {
                questions = (List<Question>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading questions: " + e.getMessage());
                questions = new ArrayList<>();
            }
        }
    }
}