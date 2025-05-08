package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value="/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String logoutMessage = "You have been logged out successfully.";

        if (session != null) {
            // Get the user before invalidating the session
            model.User user = (model.User) session.getAttribute("user");

            if (user != null) {
                // Set appropriate message based on user role
                if (user.isAdmin()) {
                    logoutMessage = "Admin logged out successfully. Thank you for managing the system.";
                } else {
                    logoutMessage = "User logged out successfully. Thank you for using our service.";
                }
            }

            // Invalidate the session
            session.invalidate();
        }

        // Create a new session for the message
        session = request.getSession(true);
        session.setAttribute("successMessage", logoutMessage);

        response.sendRedirect(request.getContextPath() + "/login");
    }
}
