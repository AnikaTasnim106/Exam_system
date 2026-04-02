package com.buet.edutrack;

import com.buet.edutrack.models.User;
import com.buet.edutrack.services.UserService;

import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Server starting on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running. Waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request = in.readLine();
            System.out.println("Received: " + request);

            if (request != null && request.startsWith("LOGIN:")) {
                String[] parts = request.split(":");
                String username = parts[1];
                String password = parts[2];

                User user = UserService.authenticateUser(username, password);
                if (user != null) {
                    out.println("SUCCESS:" + user.getRole());
                    System.out.println("Login successful for: " + username);
                } else {
                    out.println("FAILURE:Invalid username or password");
                    System.out.println("Login failed for: " + username);
                }
            }
        } catch (IOException e) {
            System.out.println("Client handler error: " + e.getMessage());
        }
    }
}