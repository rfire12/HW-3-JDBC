package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersServices {
    private static UsersServices instance;

    public static UsersServices getInstance() {
        if (instance == null) {
            instance = new UsersServices();
        }
        return instance;
    }

    public List<User> listUsers() {
        List<User> users = new ArrayList<>();
        Connection con = null;
        try {
            String query = "select * from users";
            con = DataBaseServices.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = getUser(rs.getString("uid"));
                users.add(user);
            }
        } catch (SQLException ex) {

        } finally {
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return users;
    }

    public User getUser(String uid) {
        User user = null;

        Connection con = null;

        try {
            String query = "select * from users where uid = ?";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, uid);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setUid(rs.getString("uid"));
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }

        } catch (SQLException ex) {

        } finally {
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return user;
    }

    public boolean createUser(User user) {
        boolean ok = false;

        Connection con = null;
        try {
            String query = "insert into users(uid, username, name, password, role) values (?,?,?,?,?)";
            con = DataBaseServices.getInstance().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(query);

            String uniqueID = UUID.randomUUID().toString();

            preparedStatement.setString(1, uniqueID);
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole());

            int row = preparedStatement.executeUpdate();
            ok = row > 0;
        } catch (SQLException ex) {

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
