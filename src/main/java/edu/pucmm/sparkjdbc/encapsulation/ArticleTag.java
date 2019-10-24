package edu.pucmm.sparkjdbc.encapsulation;

public class ArticleTag {
    private Article article;
    private Tag tag;

    public ArticleTag() {

    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
