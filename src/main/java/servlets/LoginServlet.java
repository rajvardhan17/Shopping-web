package servlets;

import db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Login.html").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String email = request.getParameter("username"); // it could be username/email
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password); // NOTE: In real-world apps, hash this

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User found - start session
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", rs.getString("email"));
                session.setAttribute("fullName", rs.getString("full_name"));
                session.setAttribute("isAdmin", rs.getBoolean("is_admin"));
                session.setMaxInactiveInterval(30 * 60); // optional: 30 mins

                // Redirect based on role
                if (rs.getBoolean("is_admin")) {
                    response.sendRedirect("Admin/admin.html");
                } else {
                    response.sendRedirect("index.html");
                }
            } else {
                // Invalid login
                out.println("<h3 style='color:red;'>Invalid email or password</h3>");
                request.getRequestDispatcher("/Login.html").include(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
