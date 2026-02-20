package com.buet.edutrack.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForumPost implements Serializable {
    private String id;
    private String title;
    private String content;
    private String subject;
    private String postedBy;
    private LocalDateTime postedAt;
    private List<Reply> replies;

    public ForumPost(String title, String content, String subject, String postedBy){
        this.id = "POST" + System.currentTimeMillis();
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.postedBy = postedBy;
        this.postedAt = LocalDateTime.now();
        this.replies = new ArrayList<>();
    }

    public String getId() {return id;}
    public String getTitle() {return title;}
    public String getContent() {return content;}
    public String getSubject() {return subject;}
    public String getPostedBy() {return postedBy;}
    public LocalDateTime getPostedAt() {return postedAt;}
    public List<Reply> getReplies() {return replies;}

    public void addReply(Reply reply) {replies.add(reply);}

    public static class Reply implements Serializable{
        private static final long serialVersionUID = 1L;

        private String content;
        private String repliedBy;
        private LocalDateTime repliedAt;
        private boolean isTeacher;

        public Reply(String content, String repliedBy, boolean isTeacher){
            this.content = content;
            this.repliedBy = repliedBy;
            this.isTeacher = isTeacher;
            this.repliedAt = LocalDateTime.now();
        }

        public String getContent() {return content;}
        public String getRepliedBy() {return repliedBy;}
        public LocalDateTime getRepliedAt() {return repliedAt;}
        public boolean isTeacher(){return isTeacher;}
    }
}
