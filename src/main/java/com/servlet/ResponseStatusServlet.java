package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.DBConnection;
import com.util.EmailUtil;

@WebServlet("/responseStatus")
public class ResponseStatusServlet extends HttpServlet {

    // =========================
    // GET → VIEW RESPONSE STATUS
    // =========================
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h2>Responded Users</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>Name</th><th>Email</th></tr>");

        try {
            Connection con = DBConnection.getConnection();

            // Responded users
            String responded =
                "SELECT p.name, p.email FROM participants p " +
                "JOIN responses r ON p.email = r.email";

            PreparedStatement ps1 = con.prepareStatement(responded);
            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                out.println("<tr><td>" + rs1.getString("name") + "</td><td>"
                        + rs1.getString("email") + "</td></tr>");
            }

            out.println("</table>");

            out.println("<h2>Not Responded Users</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Name</th><th>Email</th></tr>");

            // Not responded users
            String notResponded =
                "SELECT p.name, p.email FROM participants p " +
                "LEFT JOIN responses r ON p.email = r.email " +
                "WHERE r.email IS NULL";

            PreparedStatement ps2 = con.prepareStatement(notResponded);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                out.println("<tr><td>" + rs2.getString("name") + "</td><td>"
                        + rs2.getString("email") + "</td></tr>");
            }

            out.println("</table>");
            out.println("<br><a href='admin.html'>Back to Admin</a>");

            con.close();

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    // =========================
    // POST → SEND EMAIL
    // =========================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;

            // ===============================
            // SEND EVENT MAIL TO RESPONDED
            // ===============================
            if ("sendEventMail".equals(action)) {

                String subject = "Event Details – Registration Confirmed";
                String message =
                    "Hello,\n\n" +
                    "Thank you for registering for the event!\n\n" +
                    "📍 Venue: Main Auditorium\n" +
                    "📅 Date: 15 January 2026\n" +
                    "⏰ Time: 10:00 AM\n\n" +
                    "Regards,\nEvent Team";

                String sql =
                    "SELECT p.email FROM participants p " +
                    "JOIN responses r ON p.email = r.email";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

                int count = 0;
                while (rs.next()) {
                    EmailUtil.sendEmail(rs.getString("email"), subject, message);
                    count++;
                }

                out.println("<h3>Event details sent to " + count + " responded users</h3>");
            }

            // ===============================
            // SEND REMINDER TO UNRESPONDED
            // ===============================
            else if ("sendReminderMail".equals(action)) {

                String subject = "Reminder: Please Register for the Event";
                String message =
                    "Hello,\n\n" +
                    "You are invited to our upcoming event, but we noticed you haven't registered yet.\n\n" +
                    "Please fill the registration form at the earliest.\n\n" +
                    "📍 Event: Tech Symposium\n" +
                    "📅 Date: 15 January 2026\n\n" +
                    "Regards,\nEvent Team";

                String sql =
                    "SELECT p.email FROM participants p " +
                    "LEFT JOIN responses r ON p.email = r.email " +
                    "WHERE r.email IS NULL";

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

                int count = 0;
                while (rs.next()) {
                    EmailUtil.sendEmail(rs.getString("email"), subject, message);
                    count++;
                }

                out.println("<h3>Reminder email sent to " + count + " unresponded users</h3>");
            }

            out.println("<br><a href='admin.html'>Back to Admin</a>");
            con.close();

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}