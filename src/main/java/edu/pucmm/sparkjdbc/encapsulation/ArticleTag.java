package edu.pucmm.sparkjdbc.encapsulation;

public class ArticleTag {
    private String uidArticle;
    private String uidTag;

    public ArticleTag() {

    }

    public String getUidArticle() {
        return uidArticle;
    }

    public void setUidArticle(String uidArticle) {
        this.uidArticle = uidArticle;
    }

    public String getUidTag() {
        return uidTag;
    }

    public void setUidTag(String uidTag) {
        this.uidTag = uidTag;
    }
}
