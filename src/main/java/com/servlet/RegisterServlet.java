package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.DBConnection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String attend = request.getParameter("attend");

        try {
            Connection con = DBConnection.getConnection();

            // Insert response
            PreparedStatement ps1 = con.prepareStatement(
                    "INSERT INTO responses(email) VALUES(?)");
            ps1.setString(1, email);
            ps1.executeUpdate();

            // Update participant status
            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE participants SET status='RESPONDED' WHERE email=?");
            ps2.setString(1, email);
            ps2.executeUpdate();

            response.sendRedirect("success.html");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        }

        }
    }

