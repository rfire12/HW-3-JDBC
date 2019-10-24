package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.ArticleTag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticlesTagsServices {
    private static ArticlesTagsServices instance;

    public static ArticlesTagsServices getInstance() {
        if (instance == null) {
            instance = new ArticlesTagsServices();
        }
        return instance;
    }

    public ArticleTag getArticleTag(long uidArticle, long uidTag) {
        ArticleTag articleTag = null;
        Connection con = null;
        try {
            String query = "select * from articlestags where id_article = ? and id_tag = ?";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1, uidArticle);
            preparedStatement.setLong(2, uidTag);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                articleTag = new ArticleTag();
                articleTag.setUidArticle(rs.getString("id_article"));
                articleTag.setUidTag(rs.getString("id_tag"));
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

        return articleTag;
    }

    public boolean createArticleTag(String uidArticle, String uidTag) {
        boolean ok = false;

        Connection con = null;

        try {
            String query = "insert into articlestags(id_article,id_tag) values(?,?)";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, uidArticle);
            preparedStatement.setString(2, uidTag);

            int row = preparedStatement.executeUpdate();
            ok = row > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ok;
    }
}
