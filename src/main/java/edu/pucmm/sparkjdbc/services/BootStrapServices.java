package edu.pucmm.sparkjdbc.services;

import edu.pucmm.sparkjdbc.encapsulation.User;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BootStrapServices {
    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

    public static void createTables() throws SQLException {
        System.out.println("Creating tables...");
        String usersSQL = "CREATE TABLE IF NOT EXISTS USERS" +
                "(" +
                "UID VARCHAR(100) PRIMARY KEY NOT NULL," +
                "USERNAME VARCHAR(100)," +
                "NAME VARCHAR(100)," +
                "PASSWORD VARCHAR(100)," +
                "ROLE VARCHAR (100)," +
                "UNIQUE KEY USERNAME_UNIQUE(USERNAME)" +
                ");";
        String tagsSQL = "CREATE TABLE IF NOT EXISTS TAGS" +
                "(" +
                "UID VARCHAR(100) PRIMARY KEY NOT NULL," +
                "TAG VARCHAR(100) NOT NULL," +
                "UNIQUE KEY TAG_UNIQUE(TAG)" +
                ");";
        String articlesSQL = "CREATE TABLE IF NOT EXISTS ARTICLES" +
                "(" +
                "UID VARCHAR(100) PRIMARY KEY NOT NULL," +
                "TITLE VARCHAR(100) NOT NULL," +
                "BODY TEXT NOT NULL," +
                "AUTHOR_ID VARCHAR(100)," +
                "ARTICLE_DATE TIMESTAMP " +
                ");";
        String commentsSQL = "CREATE TABLE IF NOT EXISTS COMMENTS" +
                "(" +
                "UID VARCHAR(100) PRIMARY KEY NOT NULL," +
                "BODY VARCHAR(200) NOT NULL," +
                "AUTHOR_ID VARCHAR(100)," +
                "ARTICLE_ID VARCHAR(100)," +
                "FOREIGN KEY(AUTHOR_ID) REFERENCES USERS(UID)," +
                "FOREIGN KEY(ARTICLE_ID) REFERENCES ARTICLES(UID)" +
                ");";
        String articlesTagsSQL = "CREATE TABLE IF NOT EXISTS ARTICLESTAGS" +
                "(" +
                "ARTICLE_ID VARCHAR(100)," +
                "TAG_ID VARCHAR(100)," +
                "FOREIGN KEY(ARTICLE_ID) REFERENCES ARTICLES(UID)," +
                "FOREIGN KEY(TAG_ID) REFERENCES TAGS(UID)," +
                "PRIMARY KEY(ARTICLE_ID, TAG_ID)" +
                ");";
        Connection con = DataBaseServices.getInstance().getConnection();
        Statement statement = con.createStatement();
        statement.execute(usersSQL);
        statement.execute(tagsSQL);
        statement.execute(articlesSQL);
        statement.execute(commentsSQL);
        statement.execute(articlesTagsSQL);
        statement.close();

        UsersServices.getInstance().createUser(new User("admin", "Admin", "123456", "admin"));

        con.close();
        System.out.println("Tables created!!");
    }
}
