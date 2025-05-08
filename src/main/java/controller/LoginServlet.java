package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value="/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if there's a success message in the session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("successMessage") != null) {
            // Transfer the message from session to request attribute
            request.setAttribute("successMessage", session.getAttribute("successMessage"));
            // Remove the message from the session to prevent it from showing again
            session.removeAttribute("successMessage");
        }

    	request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("Login attempt - Username: " + username + ", Role: " + role);

        // Simple validation
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            request.setAttribute("errorMessage", "Username, password, and role are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        if (userDAO.validateUser(username, password)) {
            User user = userDAO.getUserByUsername(username);

            // Check if the selected role matches the user's role
            if (!role.equals(user.getRole())) {
                request.setAttribute("errorMessage", "Invalid role for this user. Please select the correct role.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect based on role
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/dashboard");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
