package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.Article;
import edu.pucmm.sparkjdbc.encapsulation.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticlesServices {
    public List<Article> listArticle() {
        List<Article> articles = new ArrayList<>();
        Connection con = null;
        try {
            String query = "select * from articles";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Article article = getArticle(rs.getLong("uid"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public Article getArticle(long uid) {
        Article article = null;
        Connection con = null;
        try {
            String query = "select * from articles where uid = ?";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setLong(1, uid);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                article.setUid(rs.getLong("uid"));
                article.setTitle(rs.getString("title"));
                article.setInformation(rs.getString("body"));
                article.setDate(rs.getDate("article_date"));
                User author = UsersServices.getInstance().getUser(rs.getLong("author_id"));
                article.setAuthor(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }
}
