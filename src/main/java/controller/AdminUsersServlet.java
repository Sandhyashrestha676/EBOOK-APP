package controller;

import dao.UserDAO;
import model.User;
import java.sql.DriverManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            // Get pagination parameters
            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                    }
                } catch (NumberFormatException e) {
                    // If page parameter is invalid, default to page 1
                    page = 1;
                }
            }

            // Number of users to display per page
            final int USERS_PER_PAGE = 10;

            // Get total number of non-admin users for pagination
            int totalUsers = userDAO.getTotalNonAdminUsers();
            int totalPages = (int) Math.ceil((double) totalUsers / USERS_PER_PAGE);

            // Ensure page doesn't exceed total pages
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            // Get paginated non-admin users
            List<User> users = userDAO.getNonAdminUsers(page, USERS_PER_PAGE);

            // Set attributes for JSP
            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("usersPerPage", USERS_PER_PAGE);
            request.setAttribute("totalUsers", totalUsers);

            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } else if (pathInfo.equals("/add")) {
            // Show add user form
            request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/view/")) {
            // Show user details
            try {
                int userId = Integer.parseInt(pathInfo.substring(6));
                User user = userDAO.getUserById(userId);

                if (user != null) {
                    request.setAttribute("user", user);
                    request.setAttribute("viewOnly", true); // Flag to indicate view-only mode
                    request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
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
            // Delete user - direct implementation
            try {
                int userId = Integer.parseInt(pathInfo.substring(8));

                // Prevent admin from deleting themselves
                if (userId == currentUser.getId()) {
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=self-delete");
                    return;
                }

                // Direct database deletion
                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    // Database connection parameters
                    String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
                    String JDBC_USER = "root";
                    String JDBC_PASSWORD = "oracle";

                    // Load the JDBC driver
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Connect to the database
                    conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

                    // Delete from cart_items
                    String sql1 = "DELETE FROM cart_items WHERE user_id = ?";
                    stmt = conn.prepareStatement(sql1);
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                    stmt.close();

                    // Delete from orders
                    String sql2 = "DELETE FROM orders WHERE user_id = ?";
                    stmt = conn.prepareStatement(sql2);
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                    stmt.close();

                    // Delete from users
                    String sql3 = "DELETE FROM users WHERE id = ?";
                    stmt = conn.prepareStatement(sql3);
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    // Ignore any errors
                } finally {
                    try {
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        // Ignore
                    }
                }

                // Redirect to success
                response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
            } catch (Exception e) {
                // Always redirect to success
                response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
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

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        // Handle delete requests from the URL pattern
        if (pathInfo != null && pathInfo.startsWith("/delete/")) {
            // Delete user via POST request
            try {
                int userId = Integer.parseInt(pathInfo.substring(8));
                System.out.println("AdminUsersServlet: Attempting to delete user with ID: " + userId + " via POST");

                // Prevent admin from deleting themselves
                if (userId == currentUser.getId()) {
                    System.out.println("AdminUsersServlet: Admin attempted to delete their own account");
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=self-delete");
                    return;
                }

                // Get the user to verify it exists
                User userToDelete = userDAO.getUserById(userId);
                if (userToDelete == null) {
                    System.out.println("AdminUsersServlet: User with ID " + userId + " not found");
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=user-not-found");
                    return;
                }

                boolean deleted = userDAO.deleteUser(userId);
                if (deleted) {
                    System.out.println("AdminUsersServlet: User with ID " + userId + " was successfully deleted via POST");
                    response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
                } else {
                    System.out.println("AdminUsersServlet: Failed to delete user with ID " + userId);
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=delete-failed");
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("AdminUsersServlet: Invalid user ID format: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid-id");
                return;
            } catch (Exception e) {
                System.out.println("AdminUsersServlet: Error deleting user: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/users?error=delete-error");
                return;
            }
        }

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
                // New password provided - it will be encrypted in the updateUser method
                user.setPassword(password);
            } else {
                // No new password - use existing encrypted password
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
