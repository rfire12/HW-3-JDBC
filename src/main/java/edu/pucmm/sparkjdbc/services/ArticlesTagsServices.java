package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.ArticleTag;
import edu.pucmm.sparkjdbc.encapsulation.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticlesTagsServices {
    private static ArticlesTagsServices instance;

    public static ArticlesTagsServices getInstance() {
        if (instance == null) {
            instance = new ArticlesTagsServices();
        }
        return instance;
    }

    public ArrayList<Tag> getArticleTags(String uidArticle) {
        ArrayList<Tag> articleTags = new ArrayList<>();
        Connection con = null;
        try {
            String query = "select * from articlestags where article_id = ?";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, uidArticle);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Tag tag = TagsServices.getInstance().getTagByUid(rs.getString("tag_id"));
                articleTags.add(tag);
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

        return articleTags;
    }

    public ArticleTag getArticleTag(String uidArticle, String nameTag) {
        ArticleTag articleTag = null;
        Connection con = null;
        try {
            String query = "select * from articlestags where id_article = ? and id_tag = ?";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, uidArticle);
            preparedStatement.setString(2, nameTag);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                articleTag = new ArticleTag();
                articleTag.setArticle(ArticlesServices.getInstance().getArticle(rs.getString("id_article")));
                articleTag.setTag(TagsServices.getInstance().getTag(rs.getString("name_tag")));
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
            String query = "insert into articlestags(article_id,tag_id) values(?,?)";
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
