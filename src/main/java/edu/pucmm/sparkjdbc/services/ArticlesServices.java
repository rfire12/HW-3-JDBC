package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.Article;
import edu.pucmm.sparkjdbc.encapsulation.Tag;
import edu.pucmm.sparkjdbc.encapsulation.User;
import edu.pucmm.sparkjdbc.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class ArticlesServices {

    private static ArticlesServices instance;

    public static ArticlesServices getInstance() {
        if (instance == null) {
            instance = new ArticlesServices();
        }
        return instance;
    }

    public ArrayList<Article> getArticles() {
        ArrayList<Article> articles = new ArrayList<>();
        Connection con = null;
        try {
            String query = "select * from articles order by article_date desc";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Article article = getArticle(rs.getString("uid"));
                if (article.getInformation().length() > 70) {
                    article.setInformation(Utils.truncate(article.getInformation(), 70) + "...");
                }
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }

    public Article getArticle(String uid) {
        Article article = null;
        Connection con = null;
        try {
            String query = "select * from articles where uid = ?";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, uid);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                article = new Article();
                article.setUid(rs.getString("uid"));
                article.setTitle(rs.getString("title"));
                article.setInformation(rs.getString("body"));
                article.setDate(rs.getTimestamp("article_date"));

                User author = UsersServices.getInstance().getUser(rs.getString("author_id"));
                article.setAuthor(author);

                article.setComments(CommentsServices.getInstance().getComments(article.getUid()));

                article.setTags(ArticlesTagsServices.getInstance().getArticleTags(article.getUid()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return article;
    }

    public boolean createArticle(Article article) {
        boolean ok = false;
        Connection con = null;
        try {
            String query = "insert into articles(uid,title,body,author_id,article_date) values(?,?,?,?,?)";
            con = DataBaseServices.getInstance().getConnection();
            String uniqueID = UUID.randomUUID().toString();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, uniqueID);
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getInformation());
            preparedStatement.setString(4, article.getAuthor().getUid());
            preparedStatement.setTimestamp(5, article.getDate());

            int row = preparedStatement.executeUpdate();
            ok = row > 0;

            ArrayList<Tag> createdTags = TagsServices.getInstance().getTags(); // Get tags from the Database
            for (Tag tag : article.getTags()) {
                if (!Utils.isTagInArray(tag, createdTags)) //If the tag is not created, then insert it on the database
                    TagsServices.getInstance().createTag(tag);
            }

            for (Tag tag : article.getTags()) { //Create Article-Tag entries on the Database
                ArticlesTagsServices.getInstance().createArticleTag(uniqueID, TagsServices.getInstance().getTag(tag.getTag()).getUid());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    public boolean updateArticle(Article article) {
        boolean ok = false;
        Connection con = null;
        try {
            String query = "update articles set title=?, body=? where uid =?";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getInformation());
            preparedStatement.setString(3, article.getUid());


            int row = preparedStatement.executeUpdate();
            ok = row > 0;

            ArticlesTagsServices.getInstance().deleteArticleTags(article.getUid()); // Delete previous article-tags entries from this article

            ArrayList<Tag> createdTags = TagsServices.getInstance().getTags(); // Get tags from the Database
            for (Tag tag : article.getTags()) {
                if (!Utils.isTagInArray(tag, createdTags)) //If the tag is not created, then insert it on the database
                    TagsServices.getInstance().createTag(tag);
            }

            for (Tag tag : article.getTags()) { //Create Article-Tag entries on the Database
                ArticlesTagsServices.getInstance().createArticleTag(article.getUid(), TagsServices.getInstance().getTag(tag.getTag()).getUid());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }
}
