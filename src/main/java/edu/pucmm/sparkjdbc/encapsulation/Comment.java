package edu.pucmm.sparkjdbc.encapsulation;

public class Comment {
    private long uid;
    private String comment;
    private User author;
    private Article article;

    public Comment() {

    }

    public Comment(String comment, User author, Article article) {
        this.comment = comment;
        this.author = author;
        this.article = article;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
