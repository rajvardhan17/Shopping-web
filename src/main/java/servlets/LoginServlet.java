package servlets;

import db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String email = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE email = ? AND password_hash = ?");
            ps.setString(1, email);
            ps.setString(2, password); // hash this in production

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("user", rs.getString("full_name"));
                res.sendRedirect("admin.html"); // or dashboard
            } else {
                res.getWriter().println("Invalid login");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
