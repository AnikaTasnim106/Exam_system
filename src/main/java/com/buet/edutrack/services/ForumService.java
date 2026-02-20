package com.buet.edutrack.services;

import com.buet.edutrack.models.ForumPost;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForumService {
    private static final String FORUM_FILE = "forum_data.ser";
    private static List<ForumPost> posts = new ArrayList<>();

    static {
        loadPosts();
    }

    public static boolean addPost(ForumPost post) {
        posts.add(post);
        return savePosts();
    }

    public static List<ForumPost> getAllPosts() {
        return new ArrayList<>(posts);
    }

    public static List<ForumPost> getPostsBySubject(String subject) {
        return posts.stream().filter(p -> p.getSubject().equals(subject)).collect(Collectors.toList());
    }

    public static boolean savePost(ForumPost post) {
        return savePosts();
    }

    private static boolean savePosts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FORUM_FILE))) {
            oos.writeObject(posts);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving forum data: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadPosts() {
        File file = new File(FORUM_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FORUM_FILE))) {
                posts = (List<ForumPost>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading forum data: " + e.getMessage());
                posts = new ArrayList<>();
            }
        }
    }
}