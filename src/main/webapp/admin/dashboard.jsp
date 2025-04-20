<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <h1>Admin Dashboard</h1>
                <% 
                User user = (User) session.getAttribute("user");
                if (user != null) {
                %>
                <p>Welcome, <%= user.getFullName() %>!</p>
                <% } %>
            </div>
            
            <div class="dashboard-menu">
                <ul>
                    <li class="active"><a href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/books">Manage Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/users">Manage Users</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/orders">Manage Orders</a></li>
                </ul>
            </div>
            
            <div class="dashboard-content">
                <div class="dashboard-stats">
                    <div class="stat-card">
                        <h3>Total Books</h3>
                        <p class="stat-number"><%= request.getAttribute("bookCount") %></p>
                        <a href="<%=request.getContextPath()%>/admin/books" class="btn btn-small">View All</a>
                    </div>
                    
                    <div class="stat-card">
                        <h3>Total Users</h3>
                        <p class="stat-number"><%= request.getAttribute("userCount") %></p>
                        <a href="<%=request.getContextPath()%>/admin/users" class="btn btn-small">View All</a>
                    </div>
                    
                    <div class="stat-card">
                        <h3>Recent Orders</h3>
                        <% 
                        List<Order> recentOrders = (List<Order>) request.getAttribute("recentOrders");
                        if (recentOrders != null) {
                        %>
                        <p class="stat-number"><%= recentOrders.size() %></p>
                        <% } else { %>
                        <p class="stat-number">0</p>
                        <% } %>
                        <a href="<%=request.getContextPath()%>/admin/orders" class="btn btn-small">View All</a>
                    </div>
                </div>
                
                <div class="dashboard-section">
                    <h2>Recent Orders</h2>
                    
                    <% 
                    if (recentOrders != null && !recentOrders.isEmpty()) {
                    %>
                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>User ID</th>
                                <th>Date</th>
                                <th>Total</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Order order : recentOrders) { %>
                            <tr>
                                <td>#<%= order.getId() %></td>
                                <td><%= order.getUserId() %></td>
                                <td><%= order.getOrderDate() %></td>
                                <td>$<%= order.getTotalAmount() %></td>
                                <td><span class="status-<%= order.getStatus() %>"><%= order.getStatus() %></span></td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/admin/orders/view/<%= order.getId() %>" class="btn btn-small">View</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% } else { %>
                    <p>No recent orders.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
    
    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
