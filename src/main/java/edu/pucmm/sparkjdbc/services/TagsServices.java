package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagsServices {

    private static TagsServices instance;

    public static TagsServices getInstance() {
        if (instance == null) {
            instance = new TagsServices();
        }
        return instance;
    }

    public ArrayList<Tag> getTags() {
        ArrayList<Tag> tags = new ArrayList<>();
        Connection con = null;
        try {
            String query = "select * from tags";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Tag tag = new Tag(rs.getString("uid"), rs.getString("tag"));
                tags.add(tag);
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
        return tags;
    }

    public Tag getTag(long uid) {
        Tag tag = null;
        Connection con = null;
        try {
            String query = "select * from tags where uid = ?";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1, uid);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                tag = new Tag(rs.getString("uid"), rs.getString("tag"));
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

        return tag;
    }

    public boolean createTag(Tag tag) {
        boolean ok = false;
        Connection con = null;
        try {
            String query = "insert into tags(uid,tag) values(?,?)";
            con = DataBaseServices.getInstance().getConnection();

            String uniqueID = UUID.randomUUID().toString();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, uniqueID);
            preparedStatement.setString(2, tag.getTag());
            int row = preparedStatement.executeUpdate();
            ok = row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }
        return ok;
    }
}
