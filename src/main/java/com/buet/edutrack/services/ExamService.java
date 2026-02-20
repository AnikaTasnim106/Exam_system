package com.buet.edutrack.services;

import com.buet.edutrack.models.Exam;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamService {
    private static final String EXAMS_FILE = "exams.dat";
    private static List<Exam> exams = new ArrayList<>();

    static {
        loadExams();
    }

    public static boolean addExam(Exam exam) {
        exams.add(exam);
        return saveExams();
    }

    public static List<Exam> getAllExams() {
        return new ArrayList<>(exams);
    }

    public static List<Exam> getExamsBySubject(String subject) {
        return exams.stream()
                .filter(e -> e.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }

    public static Exam getExamById(String examId) {
        return exams.stream()
                .filter(e -> e.getId().equals(examId))
                .findFirst()
                .orElse(null);
    }

    public static boolean updateExam(Exam exam) {
        for (int i = 0; i < exams.size(); i++) {
            if (exams.get(i).getId().equals(exam.getId())) {
                exams.set(i, exam);
                return saveExams();
            }
        }
        return false;
    }

    public static boolean deleteExam(String examId) {
        boolean removed = exams.removeIf(e -> e.getId().equals(examId));
        if (removed) {
            saveExams();
        }
        return removed;
    }

    // Save exams to file
    private static boolean saveExams() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EXAMS_FILE))) {
            oos.writeObject(exams);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving exams: " + e.getMessage());
            return false;
        }
    }

    private static void loadExams() {
        File file = new File(EXAMS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXAMS_FILE))) {
                exams = (List<Exam>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading exams: " + e.getMessage());
                exams = new ArrayList<>();
            }
        }
    }
}