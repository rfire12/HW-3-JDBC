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
            String query = "select * from articles";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Article article = getArticle(rs.getString("uid"));
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
                article.setDate(rs.getDate("article_date"));

                User author = UsersServices.getInstance().getUser(rs.getString("author_id"));
                article.setAuthor(author);

                article.setTags(ArticlesTagsServices.getInstance().getArticleTags(rs.getString("uid")));
                System.out.println("TAGS: " + article.getTags().size());
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
            preparedStatement.setDate(5, (Date) article.getDate());

            int row = preparedStatement.executeUpdate();
            ok = row > 0;

            ArrayList<Tag> createdTags = TagsServices.getInstance().getTags(); // Get tags from the Database
            for (Tag tag : article.getTags()) {
                if (!Utils.isTagInArray(tag, createdTags)) //If the tag is not created, then insert it on the database
                    TagsServices.getInstance().createTag(tag);
            }

            for (Tag tag : article.getTags()) {
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
}
