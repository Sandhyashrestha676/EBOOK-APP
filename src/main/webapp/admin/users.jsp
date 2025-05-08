<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
    <script>
    function deleteUser(userId) {
        if (confirm('Are you sure you want to delete this user?')) {
            // Create a form dynamically
            var form = document.createElement('form');
            form.method = 'POST';
            form.action = '<%=request.getContextPath()%>/admin/users/delete/' + userId;
            document.body.appendChild(form);
            form.submit();
        }
    }
    </script>
</head>
<body>
    <jsp:include page="../header.jsp" />

    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <h1>Manage Users</h1>
                <a href="<%=request.getContextPath()%>/admin/users/add" class="btn btn-primary">Add New User</a>
            </div>

            <!-- Dashboard menu moved to header -->

            <div class="dashboard-content">
                <% if (request.getParameter("added") != null) { %>
                <div class="alert alert-success">User added successfully.</div>
                <% } else if (request.getParameter("updated") != null) { %>
                <div class="alert alert-success">User updated successfully.</div>
                <% } else if (request.getParameter("deleted") != null) { %>
                <div class="alert alert-success">User deleted successfully.</div>
                <% } else if (request.getParameter("error") != null && "self-delete".equals(request.getParameter("error"))) { %>
                <div class="alert alert-error">You cannot delete your own account.</div>
                <% } %>

                <div class="dashboard-section">
                    <h2>Users (Page <%= request.getAttribute("currentPage") %> of <%= request.getAttribute("totalPages") %>, Total: <%= request.getAttribute("totalUsers") %>)</h2>
                    <table class="dashboard-table users-table">
                        <thead>
                            <tr>
                                <th class="id-column">ID</th>
                                <th class="username-column">Username</th>
                                <th class="fullname-column">Full Name</th>
                                <th class="email-column">Email</th>
                                <th class="role-column">Role</th>
                                <th class="actions-column">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                            List<User> users = (List<User>) request.getAttribute("users");
                            if (users != null && !users.isEmpty()) {
                                for (User user : users) {
                            %>
                            <tr>
                                <td class="id-column">#<%= user.getId() %></td>
                                <td class="username-column"><%= user.getUsername() %></td>
                                <td class="fullname-column"><%= user.getFullName() %></td>
                                <td class="email-column"><%= user.getEmail() %></td>
                                <td class="role-column"><%= user.getRole() %></td>
                                <td class="actions-column">
                                    <a href="<%=request.getContextPath()%>/admin/users/view/<%= user.getId() %>" class="btn btn-info btn-small">View</a>
                                    <a href="<%=request.getContextPath()%>/admin/users/edit/<%= user.getId() %>" class="btn btn-small">Edit</a>
                                    <a href="<%=request.getContextPath()%>/admin/users/delete/<%= user.getId() %>?t=<%= System.currentTimeMillis() %>" class="btn btn-danger btn-small">Delete</a>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="6">No users found.</td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>

                    <!-- Pagination Controls -->
                    <%
                    Integer currentPage = (Integer) request.getAttribute("currentPage");
                    Integer totalPages = (Integer) request.getAttribute("totalPages");

                    if (currentPage != null && totalPages != null && totalPages > 1) {
                    %>
                    <div style="width: 100%; display: flex; justify-content: center; margin-top: 20px;">
                        <div class="pagination">
                            <% if (currentPage > 1) { %>
                            <a href="<%=request.getContextPath()%>/admin/users?page=<%= currentPage - 1 %>" class="pagination-link nav-btn" aria-label="Previous page"><i class="fas fa-chevron-left"></i></a>
                            <% } else { %>
                            <span class="pagination-link nav-btn disabled"><i class="fas fa-chevron-left"></i></span>
                            <% } %>

                            <%
                            // Simple pagination with limited numbers
                            int maxVisiblePages = 5; // Show at most 5 page numbers
                            int startPage = Math.max(1, currentPage - (maxVisiblePages / 2));
                            int endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

                            // Adjust start page if we're near the end
                            if (endPage - startPage < maxVisiblePages - 1) {
                                startPage = Math.max(1, endPage - maxVisiblePages + 1);
                            }

                            // Display page numbers
                            for (int i = startPage; i <= endPage; i++) {
                            %>
                            <a href="<%=request.getContextPath()%>/admin/users?page=<%= i %>"
                               class="pagination-link <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
                            <% } %>

                            <% if (currentPage < totalPages) { %>
                            <a href="<%=request.getContextPath()%>/admin/users?page=<%= currentPage + 1 %>" class="pagination-link nav-btn" aria-label="Next page"><i class="fas fa-chevron-right"></i></a>
                            <% } else { %>
                            <span class="pagination-link nav-btn disabled"><i class="fas fa-chevron-right"></i></span>
                            <% } %>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
