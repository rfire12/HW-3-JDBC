package edu.pucmm.sparkjdbc.encapsulation;

public class Tag {
    private String uid;
    private String tag;

    public Tag() {

    }

    public Tag(String uid, String tag) {
        this.uid = uid;
        this.tag = tag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
