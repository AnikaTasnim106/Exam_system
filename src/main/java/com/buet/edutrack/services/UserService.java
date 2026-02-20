package com.buet.edutrack.services;

import com.buet.edutrack.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String USER_FILE = "users.txt";
    private static List<User> users = new ArrayList<>();

    static {
        loadUsers();
        if (users.isEmpty()) {
            users.add(new User("student", "student@edutrack.com", "123", "Student"));
            users.add(new User("teacher", "teacher@edutrack.com", "123", "Teacher"));
            saveUsers();
        }
    }

    public static boolean registerUser(String username, String email, String password, String role) {
        if (getUserByUsername(username) != null) {
            return false;
        }
        User newUser = new User(username, email, password, role);
        users.add(newUser);
        saveUsers();
        return true;
    }

    public static User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadUsers() {
        File file = new File(USER_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading users: " + e.getMessage());
                users = new ArrayList<>();
            }
        }
    }
}