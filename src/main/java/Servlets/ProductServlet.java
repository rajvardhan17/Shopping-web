package Servlets;

import db.DBConnection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html"); // or application/json if sending JSON
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT p.product_id, p.name, p.description, p.base_price, " +
                    "p.category, pv.color, pv.size, pv.stock_qty " +
                    "FROM products p " +
                    "JOIN product_variants pv ON p.product_id = pv.product_id";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            out.println("<h2>All Products</h2>");
            out.println("<table border='1' cellpadding='10'><tr>" +
                    "<th>ID</th><th>Name</th><th>Price</th><th>Category</th>" +
                    "<th>Color</th><th>Size</th><th>Stock</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("product_id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>₹" + rs.getDouble("base_price") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getString("color") + "</td>");
                out.println("<td>" + rs.getString("size") + "</td>");
                out.println("<td>" + rs.getInt("stock_qty") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error fetching products: " + e.getMessage());
        }
    }
}
