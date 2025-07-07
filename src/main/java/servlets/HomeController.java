package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HomeController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Option 1: Forward internally to HTML
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
        dispatcher.forward(request, response);

        // OR Option 2: Redirect to URL
        // response.sendRedirect("index.html");
    }
}
