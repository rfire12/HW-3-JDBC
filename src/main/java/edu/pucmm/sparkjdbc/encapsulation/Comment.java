package edu.pucmm.sparkjdbc.encapsulation;

public class Comment {
    private String uid;
    private String comment;
    private User author;

    public Comment() {

    }

    public Comment(String comment, User author) {
        this.comment = comment;
        this.author = author;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
