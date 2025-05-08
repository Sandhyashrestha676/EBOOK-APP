<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Form - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />

    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <%
                User editUser = (User) request.getAttribute("user");
                boolean viewOnly = (request.getAttribute("viewOnly") != null && (Boolean)request.getAttribute("viewOnly"));

                if (viewOnly) {
                %>
                <h1>View User</h1>
                <% } else if (editUser != null) { %>
                <h1>Edit User</h1>
                <% } else { %>
                <h1>Add New User</h1>
                <% } %>
            </div>

            <!-- Dashboard menu moved to header -->

            <div class="dashboard-content">
                <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
                <% } %>

                <div class="form-container">
                    <% if (viewOnly) { %>
                    <!-- View-only mode -->
                    <div class="view-user-details">
                        <div class="form-group">
                            <label>Username</label>
                            <p><%= editUser.getUsername() %></p>
                        </div>

                        <div class="form-group">
                            <label>Email</label>
                            <p><%= editUser.getEmail() %></p>
                        </div>

                        <div class="form-group">
                            <label>Full Name</label>
                            <p><%= editUser.getFullName() %></p>
                        </div>

                        <div class="form-group">
                            <label>Role</label>
                            <p><%= editUser.getRole() %></p>
                        </div>

                        <div class="form-group">
                            <label>Created At</label>
                            <p><%= editUser.getCreatedAt() != null ? editUser.getCreatedAt() : "N/A" %></p>
                        </div>

                        <div class="form-actions">
                            <a href="<%=request.getContextPath()%>/admin/users/edit/<%= editUser.getId() %>" class="btn btn-primary">Edit</a>
                            <a href="<%=request.getContextPath()%>/admin/users" class="btn btn-secondary">Back</a>
                        </div>
                    </div>
                    <% } else { %>
                    <!-- Edit/Add mode -->
                    <form action="<%=request.getContextPath()%>/admin/users" method="post">
                        <% if (editUser != null) { %>
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= editUser.getId() %>">
                        <% } else { %>
                        <input type="hidden" name="action" value="add">
                        <% } %>

                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" value="<%= editUser != null ? editUser.getUsername() : "" %>" required>
                        </div>

                        <div class="form-group">
                            <label for="password">Password <%= editUser != null ? "(Leave blank to keep current password)" : "" %></label>
                            <input type="password" id="password" name="password" <%= editUser == null ? "required" : "" %>>
                        </div>

                        <% if (editUser == null) { %>
                        <div class="form-group">
                            <label for="confirmPassword">Confirm Password</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" required>
                        </div>
                        <% } %>

                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" value="<%= editUser != null ? editUser.getEmail() : "" %>" required>
                        </div>

                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" id="fullName" name="fullName" value="<%= editUser != null ? editUser.getFullName() : "" %>" required>
                        </div>

                        <div class="form-group">
                            <label for="role">Role</label>
                            <select id="role" name="role" required>
                                <option value="user" <%= editUser != null && "user".equals(editUser.getRole()) ? "selected" : "" %>>User</option>
                                <option value="admin" <%= editUser != null && "admin".equals(editUser.getRole()) ? "selected" : "" %>>Admin</option>
                            </select>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save</button>
                            <a href="<%=request.getContextPath()%>/admin/users" class="btn btn-secondary">Cancel</a>
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
