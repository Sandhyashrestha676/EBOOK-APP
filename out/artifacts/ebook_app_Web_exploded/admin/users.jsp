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

            <div class="dashboard-menu">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/books">Manage Books</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/admin/users">Manage Users</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/orders">Manage Orders</a></li>
                </ul>
            </div>

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
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
