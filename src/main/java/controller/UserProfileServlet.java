package controller;

import dao.OrderDAO;
import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value="/user/profile")
public class UserProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private OrderDAO orderDAO;

    public void init() {
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get the latest user data from the database
        User updatedUser = userDAO.getUserById(user.getId());
        if (updatedUser != null) {
            // Update the session with the latest user data
            session.setAttribute("user", updatedUser);
        }

        // Get the order count for this user
        int orderCount = orderDAO.countOrdersByUser(user.getId());
        System.out.println("Order count for user " + user.getId() + ": " + orderCount);
        request.setAttribute("orderCount", Integer.valueOf(orderCount));

        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get form data
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate form data
        boolean hasError = false;

        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("fullNameError", "Full name is required");
            hasError = true;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("emailError", "Email is required");
            hasError = true;
        }

        // Check if user wants to change password
        boolean changePassword = currentPassword != null && !currentPassword.trim().isEmpty();

        if (changePassword) {
            // Validate current password
            try {
                String storedPassword = user.getPassword();
                boolean passwordMatches = false;

                try {
                    // Try to decrypt the stored password and compare
                    String decryptedPassword = util.PasswordUtil.decryptPassword(storedPassword, "U3CdwubLD5yQbUOG92ZnHw==");
                    passwordMatches = decryptedPassword.equals(currentPassword);
                } catch (Exception e) {
                    // If decryption fails, fall back to direct comparison (for older non-encrypted passwords)
                    System.out.println("Password decryption failed in profile, falling back to direct comparison: " + e.getMessage());
                    passwordMatches = storedPassword.equals(currentPassword);
                }

                if (!passwordMatches) {
                    request.setAttribute("currentPasswordError", "Current password is incorrect");
                    hasError = true;
                }
            } catch (Exception e) {
                System.out.println("Error validating current password: " + e.getMessage());
                request.setAttribute("currentPasswordError", "Error validating password");
                hasError = true;
            }

            // Validate new password
            if (newPassword == null || newPassword.trim().isEmpty()) {
                request.setAttribute("newPasswordError", "New password is required");
                hasError = true;
            }

            // Validate confirm password
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("confirmPasswordError", "Passwords do not match");
                hasError = true;
            }
        }

        if (hasError) {
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }

        // Update user data
        user.setFullName(fullName);
        user.setEmail(email);

        if (changePassword) {
            user.setPassword(newPassword);
        }

        // Save changes to database
        if (userDAO.updateUser(user)) {
            // Update session with new user data
            session.setAttribute("user", user);
            request.setAttribute("successMessage", "Profile updated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to update profile");
        }

        // Get the order count for this user
        int orderCount = orderDAO.countOrdersByUser(user.getId());
        System.out.println("Order count for user " + user.getId() + ": " + orderCount);
        request.setAttribute("orderCount", Integer.valueOf(orderCount));

        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }
}
