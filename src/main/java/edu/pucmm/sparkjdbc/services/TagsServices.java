package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TagsServices {

    private static TagsServices instance;

    public static TagsServices getInstance() {
        if (instance == null) {
            instance = new TagsServices();
        }
        return instance;
    }

    public boolean createTag(Tag tag) {
        boolean ok = false;
        Connection con = null;
        try {
            String query = "insert into tags(tag) values(?)";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tag.getTag());
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
