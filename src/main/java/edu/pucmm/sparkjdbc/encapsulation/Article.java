package edu.pucmm.sparkjdbc.encapsulation;

import java.util.ArrayList;
import java.util.Date;

public class Article {
    private String uid;
    private String title;
    private String information;
    private User author;
    private Date date;
    private ArrayList<Comment> comments;
    private ArrayList<Tag> tags;

    public Article() {
    }

    public Article(String title, String information, User author, Date date) {
        this.title = title;
        this.information = information;
        this.author = author;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
