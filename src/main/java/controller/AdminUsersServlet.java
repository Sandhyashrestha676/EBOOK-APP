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
import java.util.List;

@WebServlet(value="/admin/users/*")
public class AdminUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    public void init() {
        userDAO = new UserDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all users
            List<User> users = userDAO.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } else if (pathInfo.equals("/add")) {
            // Show add user form
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            // Show edit user form
            try {
                int userId = Integer.parseInt(pathInfo.substring(6));
                User user = userDAO.getUserById(userId);
                
                if (user != null) {
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Delete user
            try {
                int userId = Integer.parseInt(pathInfo.substring(8));
                
                // Prevent admin from deleting themselves
                if (userId == currentUser.getId()) {
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=self-delete");
                    return;
                }
                
                userDAO.deleteUser(userId);
                response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Add new user
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setEmail(request.getParameter("email"));
            user.setFullName(request.getParameter("fullName"));
            user.setRole(request.getParameter("role"));
            
            if (userDAO.addUser(user)) {
                response.sendRedirect(request.getContextPath() + "/admin/users?added=true");
            } else {
                request.setAttribute("errorMessage", "Failed to add user");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            }
        } else if ("update".equals(action)) {
            // Update user
            User user = new User();
            int userId = Integer.parseInt(request.getParameter("id"));
            user.setId(userId);
            user.setUsername(request.getParameter("username"));
            
            // Only update password if provided
            String password = request.getParameter("password");
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(password);
            } else {
                User existingUser = userDAO.getUserById(userId);
                user.setPassword(existingUser.getPassword());
            }
            
            user.setEmail(request.getParameter("email"));
            user.setFullName(request.getParameter("fullName"));
            user.setRole(request.getParameter("role"));
            
            if (userDAO.updateUser(user)) {
                // Update session if current user is updated
                if (userId == currentUser.getId()) {
                    session.setAttribute("user", user);
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/users?updated=true");
            } else {
                request.setAttribute("errorMessage", "Failed to update user");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            }
        }
    }
}
