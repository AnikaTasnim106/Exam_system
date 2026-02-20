package com.buet.edutrack.services;

import com.buet.edutrack.models.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultService {
    private static final String RESULTS_FILE = "results.dat";
    private static List<Result> results = new ArrayList<>();

    static {
        loadResults();
    }
    public static boolean addResult(Result result) {
        results.add(result);
        return saveResults();
    }
    public static List<Result> getAllResults() {
        return new ArrayList<>(results);
    }
    public static List<Result> getResultsByStudent(String studentUsername) {
        return results.stream()
                .filter(r -> r.getStudentUsername().equals(studentUsername))
                .collect(Collectors.toList());
    }
    public static List<Result> getResultsByExam(String examId) {
        return results.stream()
                .filter(r -> r.getExamId().equals(examId))
                .collect(Collectors.toList());
    }
    public static boolean hasStudentTakenExam(String studentUsername, String examId) {
        return results.stream()
                .anyMatch(r -> r.getStudentUsername().equals(studentUsername) &&
                        r.getExamId().equals(examId));
    }
    public static Result getResult(String studentUsername, String examId) {
        return results.stream()
                .filter(r -> r.getStudentUsername().equals(studentUsername) &&
                        r.getExamId().equals(examId))
                .findFirst()
                .orElse(null);
    }
    private static boolean saveResults() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RESULTS_FILE))) {
            oos.writeObject(results);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    private static void loadResults() {
        File file = new File(RESULTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RESULTS_FILE))) {
                results = (List<Result>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading results: " + e.getMessage());
                results = new ArrayList<>();
            }
        }
    }
}