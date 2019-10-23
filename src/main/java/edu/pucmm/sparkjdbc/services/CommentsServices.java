package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.Comment;
import edu.pucmm.sparkjdbc.encapsulation.User;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsServices {
    public List<Comment> listComments(long articleId) {
        List<Comment> comments = new ArrayList<>();

        Connection con = null;

        try {
            String query = "select * from comments where article_id = ?";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setLong(1, articleId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setUid(rs.getLong("uid"));
                comment.setComment(rs.getString("body"));
                long authorId = rs.getLong("author_id");
                User author = UsersServices.getInstance().getUser(authorId);
                comment.setAuthor(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }
}
