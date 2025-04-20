<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
    <div class="background-wrapper">
        <jsp:include page="../header.jsp" />

        <div class="container">
            <div class="dashboard">
                <div class="dashboard-header">
                    <h1>My Profile</h1>
                </div>

                <div class="dashboard-menu">
                    <ul>
                        <li><a href="<%=request.getContextPath()%>/user/dashboard">Dashboard</a></li>
                        <li><a href="<%=request.getContextPath()%>/user/orders">My Orders</a></li>
                        <li class="active"><a href="<%=request.getContextPath()%>/user/profile">My Profile</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <% if (request.getAttribute("successMessage") != null) { %>
                    <div class="alert alert-success">
                        <%= request.getAttribute("successMessage") %>
                    </div>
                    <% } %>

                    <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("errorMessage") %>
                    </div>
                    <% } %>

                    <div class="profile-container">
                        <div class="profile-header">
                            <div class="profile-avatar">
                                <i class="fas fa-user-circle"></i>
                                <%
                                User user = (User) session.getAttribute("user");
                                if (user != null) {
                                %>
                                <h2><%= user.getUsername() %></h2>
                                <% } %>
                            </div>
                            <div class="profile-stats">
                                <div class="stat-item">
                                    <i class="fas fa-book"></i>
                                    <span>Member Since</span>
                                    <span class="stat-value">
                                        <%
                                        if (user.getCreatedAt() != null) {
                                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM yyyy");
                                            out.print(dateFormat.format(user.getCreatedAt()));
                                        } else {
                                            out.print("N/A");
                                        }
                                        %>
                                    </span>
                                </div>
                                <div class="stat-item">
                                    <i class="fas fa-shopping-bag"></i>
                                    <span>Orders</span>
                                    <span class="stat-value">
                                        <%
                                        Object orderCountObj = request.getAttribute("orderCount");
                                        if (orderCountObj != null) {
                                            out.print(orderCountObj);
                                        } else {
                                            out.print("0");
                                        }
                                        %>
                                    </span>
                                </div>
                                <div class="stat-item">
                                    <i class="fas fa-star"></i>
                                    <span>Status</span>
                                    <span class="stat-value">Active</span>
                                </div>
                            </div>
                        </div>

                        <div class="profile-tabs">
                            <div class="tab active" data-tab="personal-info">
                                <i class="fas fa-user"></i> Personal Information
                            </div>
                            <div class="tab" data-tab="change-password">
                                <i class="fas fa-lock"></i> Change Password
                            </div>
                        </div>

                        <div class="profile-content">
                            <div class="tab-content active" id="personal-info">
                                <form action="<%=request.getContextPath()%>/user/profile" method="post" class="profile-form">
                                    <div class="form-group">
                                        <label for="username">Username</label>
                                        <div class="input-with-icon">
                                            <i class="fas fa-user"></i>
                                            <input type="text" id="username" name="username" value="<%= user.getUsername() %>" disabled>
                                        </div>
                                        <p class="form-hint">Username cannot be changed</p>
                                    </div>

                                    <div class="form-group">
                                        <label for="fullName">Full Name</label>
                                        <div class="input-with-icon">
                                            <i class="fas fa-id-card"></i>
                                            <input type="text" id="fullName" name="fullName" value="<%= user.getFullName() %>" required>
                                        </div>
                                        <% if (request.getAttribute("fullNameError") != null) { %>
                                        <p class="form-error"><%= request.getAttribute("fullNameError") %></p>
                                        <% } %>
                                    </div>

                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <div class="input-with-icon">
                                            <i class="fas fa-envelope"></i>
                                            <input type="email" id="email" name="email" value="<%= user.getEmail() %>" required>
                                        </div>
                                        <% if (request.getAttribute("emailError") != null) { %>
                                        <p class="form-error"><%= request.getAttribute("emailError") %></p>
                                        <% } %>
                                    </div>

                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Save Changes</button>
                                    </div>
                                </form>
                            </div>

                            <div class="tab-content" id="change-password">
                                <form action="<%=request.getContextPath()%>/user/profile" method="post" class="profile-form">
                                    <!-- Hidden fields to maintain personal info -->
                                    <input type="hidden" name="fullName" value="<%= user.getFullName() %>">
                                    <input type="hidden" name="email" value="<%= user.getEmail() %>">

                                    <div class="form-group">
                                        <label for="currentPassword">Current Password</label>
                                        <div class="input-with-icon">
                                            <i class="fas fa-lock"></i>
                                            <input type="password" id="currentPassword" name="currentPassword" required>
                                        </div>
                                        <% if (request.getAttribute("currentPasswordError") != null) { %>
                                        <p class="form-error"><%= request.getAttribute("currentPasswordError") %></p>
                                        <% } %>
                                    </div>

                                    <div class="form-group">
                                        <label for="newPassword">New Password</label>
                                        <div class="input-with-icon">
                                            <i class="fas fa-key"></i>
                                            <input type="password" id="newPassword" name="newPassword" required>
                                        </div>
                                        <% if (request.getAttribute("newPasswordError") != null) { %>
                                        <p class="form-error"><%= request.getAttribute("newPasswordError") %></p>
                                        <% } %>
                                    </div>

                                    <div class="form-group">
                                        <label for="confirmPassword">Confirm New Password</label>
                                        <div class="input-with-icon">
                                            <i class="fas fa-check-circle"></i>
                                            <input type="password" id="confirmPassword" name="confirmPassword" required>
                                        </div>
                                        <% if (request.getAttribute("confirmPasswordError") != null) { %>
                                        <p class="form-error"><%= request.getAttribute("confirmPasswordError") %></p>
                                        <% } %>
                                    </div>

                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Change Password</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../footer.jsp" />
    </div>

    <script>
        // Tab switching functionality
        document.addEventListener('DOMContentLoaded', function() {
            const tabs = document.querySelectorAll('.tab');
            const tabContents = document.querySelectorAll('.tab-content');

            tabs.forEach(tab => {
                tab.addEventListener('click', function() {
                    const tabId = this.getAttribute('data-tab');

                    // Remove active class from all tabs and contents
                    tabs.forEach(t => t.classList.remove('active'));
                    tabContents.forEach(c => c.classList.remove('active'));

                    // Add active class to clicked tab and corresponding content
                    this.classList.add('active');
                    document.getElementById(tabId).classList.add('active');
                });
            });
        });
    </script>
</body>
</html>
