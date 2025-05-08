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

@WebServlet(value="/admin/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    public void init() {
        userDAO = new UserDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userIdStr = request.getParameter("id");
        System.out.println("DeleteUserServlet: Received request to delete user with ID: " + userIdStr);
        
        if (userIdStr == null || userIdStr.isEmpty()) {
            System.out.println("DeleteUserServlet: User ID is null or empty");
            response.sendRedirect(request.getContextPath() + "/admin/users?error=missing-id");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            System.out.println("DeleteUserServlet: Attempting to delete user with ID: " + userId);
            
            // Prevent admin from deleting themselves
            if (userId == currentUser.getId()) {
                System.out.println("DeleteUserServlet: Admin attempted to delete their own account");
                response.sendRedirect(request.getContextPath() + "/admin/users?error=self-delete");
                return;
            }
            
            // Get the user to verify it exists
            User userToDelete = userDAO.getUserById(userId);
            if (userToDelete == null) {
                System.out.println("DeleteUserServlet: User with ID " + userId + " not found");
                response.sendRedirect(request.getContextPath() + "/admin/users?error=user-not-found");
                return;
            }
            
            boolean deleted = userDAO.deleteUser(userId);
            if (deleted) {
                System.out.println("DeleteUserServlet: User with ID " + userId + " was successfully deleted");
                response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
            } else {
                System.out.println("DeleteUserServlet: Failed to delete user with ID " + userId);
                response.sendRedirect(request.getContextPath() + "/admin/users?error=delete-failed");
            }
        } catch (NumberFormatException e) {
            System.out.println("DeleteUserServlet: Invalid user ID format: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid-id");
        } catch (Exception e) {
            System.out.println("DeleteUserServlet: Error deleting user: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/users?error=delete-error");
        }
    }
}
