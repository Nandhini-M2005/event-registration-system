package com.test;

import java.sql.Connection;
import java.sql.SQLException;
import com.db.DBConnection;

public class TestConnection {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Database Connected Successfully!");
            try {
                con.close(); // Always close connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Database Connection Failed!");
        }
    }
}
