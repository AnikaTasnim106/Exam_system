package com.buet.edutrack;

import java.io.*;
import java.net.*;

public class NetworkClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static String sendLoginRequest(String username, String password) {
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("LOGIN:" + username + ":" + password);
            return in.readLine();
        } catch (IOException e) {
            return "FAILURE:Could not connect to server";
        }
    }
}