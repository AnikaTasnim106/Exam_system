package com.buet.edutrack.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String examId;
    private String studentUsername;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private double percentage;
    private Map<String, String> studentAnswers;
    private LocalDateTime submittedAt;
    private int timeTaken;

    public Result() {
        this.id = generateId();
        this.studentAnswers = new HashMap<>();
        this.submittedAt = LocalDateTime.now();
    }

    public Result(String examId, String studentUsername) {
        this.id = generateId();
        this.examId = examId;
        this.studentUsername = studentUsername;
        this.studentAnswers = new HashMap<>();
        this.submittedAt = LocalDateTime.now();
    }

    private String generateId() {
        return "RESULT" + System.currentTimeMillis();
    }

    public void calculateScore(int total, int correct) {
        this.totalQuestions = total;
        this.correctAnswers = correct;
        this.wrongAnswers = total - correct;
        this.percentage = (correct * 100.0) / total;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Map<String, String> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(Map<String, String> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getGrade() {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }
}