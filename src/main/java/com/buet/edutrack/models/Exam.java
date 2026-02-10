package com.buet.edutrack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String subject;
    private String description;
    private int duration; // in minutes
    private List<String> questionIds; // IDs of questions in this exam
    private String createdBy; // teacher username
    private String createdDate;

    public Exam() {
        this.id = generateId();
        this.questionIds = new ArrayList<>();
    }

    public Exam(String title, String subject, String description, int duration, String createdBy) {
        this.id = generateId();
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.duration = duration;
        this.createdBy = createdBy;
        this.questionIds = new ArrayList<>();
        this.createdDate = java.time.LocalDateTime.now().toString();
    }

    private String generateId() {
        return "EXAM" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public List<String> getQuestionIds() { return questionIds; }
    public void setQuestionIds(List<String> questionIds) { this.questionIds = questionIds; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public void addQuestion(String questionId) {
        if (!questionIds.contains(questionId)) {
            questionIds.add(questionId);
        }
    }

    public void removeQuestion(String questionId) {
        questionIds.remove(questionId);
    }

    @Override
    public String toString() {
        return title + " (" + questionIds.size() + " questions)";
    }
}