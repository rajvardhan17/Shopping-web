package servlets;

import db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.html").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Simple password match check
        if (!password.equals(confirmPassword)) {
            out.println("<h3 style='color:red;'>Passwords do not match!</h3>");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if email already exists
            PreparedStatement checkUser = conn.prepareStatement(
                    "SELECT * FROM users WHERE email = ?");
            checkUser.setString(1, email);
            ResultSet rs = checkUser.executeQuery();

            if (rs.next()) {
                out.println("<h3 style='color:red;'>Email already registered.</h3>");
                return;
            }

            // Insert user
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (full_name, email, password_hash) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password); // You should hash this in production

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("Login.html");
            } else {
                out.println("<h3 style='color:red;'>Registration failed. Try again.</h3>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
    

}
