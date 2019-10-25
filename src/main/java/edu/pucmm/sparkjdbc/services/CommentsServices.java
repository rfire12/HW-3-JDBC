package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.Comment;
import edu.pucmm.sparkjdbc.encapsulation.User;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentsServices {

    private static CommentsServices instance;

    public static CommentsServices getInstance() {
        if (instance == null) {
            instance = new CommentsServices();
        }

        return instance;
    }

    public boolean createComment(String uidArticle, String uidAuthor, Comment comment) {
        boolean ok = false;
        Connection con = null;
        try {
            String query = "insert into comments(uid,body,author_id,article_id) values (?,?,?,?)";
            con = DataBaseServices.getInstance().getConnection();
            String uniqueID = UUID.randomUUID().toString();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, uniqueID);
            preparedStatement.setString(2, comment.getComment());
            preparedStatement.setString(3, uidAuthor);
            preparedStatement.setString(4, uidArticle);

            int row = preparedStatement.executeUpdate();
            ok = row > 0;
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

    public ArrayList<Comment> getComments(String articleId) {
        ArrayList<Comment> comments = new ArrayList<>();

        Connection con = null;

        try {
            String query = "select * from comments where article_id = ?";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, articleId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setUid(rs.getString("uid"));
                comment.setComment(rs.getString("body"));
                String authorId = rs.getString("author_id");
                User author = UsersServices.getInstance().getUser(authorId);
                comment.setAuthor(author);
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    public boolean deleteComment(String uid) {
        boolean ok = false;

        Connection con = null;

        try {
            String query = "delete from comments where uid = ?";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, uid);

            int row = preparedStatement.executeUpdate();
            ok = row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ok;
    }
}
