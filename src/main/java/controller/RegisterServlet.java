package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");

        // Simple validation
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                email == null || email.isEmpty() ||
                fullName == null || fullName.isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            request.setAttribute("errorMessage", "Please enter a valid email address");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validate that email is a Gmail address
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            request.setAttribute("errorMessage", "Please use a Gmail address (@gmail.com)");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Check if username already exists
        if (userDAO.getUserByUsername(username) != null) {
            request.setAttribute("errorMessage", "Username already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Check if email already exists
        if (userDAO.getUserByEmail(email) != null) {
            request.setAttribute("errorMessage", "Email address already exists. Please use a different email.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In a real app, you should hash the password
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole("user"); // Default role is user

        if (userDAO.addUser(user)) {
            request.setAttribute("successMessage", "Registration successful. Please login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
