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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Retrieve form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Password confirmation check
        if (!password.equals(confirmPassword)) {
            out.println("<h3 style='color:red;'>Passwords do not match!</h3>");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            // Check for existing user
            String checkSql = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        out.println("<h3 style='color:red;'>Email already registered.</h3>");
                        return;
                    }
                }
            }

            // Insert new user
            String insertSql = """
                INSERT INTO users (full_name, email, password_hash, phone, address)
                VALUES (?, ?, ?, ?, ?)
            """;

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, email);
                insertStmt.setString(3, password); // TODO: hash password in production
                insertStmt.setString(4, phone);
                insertStmt.setString(5, address);


                int rows = insertStmt.executeUpdate();

                if (rows > 0) {
                    response.sendRedirect("Login.html");
                } else {
                    out.println("<h3 style='color:red;'>Registration failed. Please try again.</h3>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
