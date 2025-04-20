<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <h1>User Dashboard</h1>
                <% 
                User user = (User) session.getAttribute("user");
                if (user != null) {
                %>
                <p>Welcome, <%= user.getFullName() %>!</p>
                <% } %>
            </div>
            
            <div class="dashboard-menu">
                <ul>
                    <li class="active"><a href="<%=request.getContextPath()%>/user/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/orders">My Orders</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/profile">My Profile</a></li>
                </ul>
            </div>
            
            <div class="dashboard-content">
                <div class="dashboard-section">
                    <h2>Recent Orders</h2>
                    
                    <% 
                    List<Order> recentOrders = (List<Order>) request.getAttribute("recentOrders");
                    if (recentOrders != null && !recentOrders.isEmpty()) {
                    %>
                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
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
                                <td><%= order.getOrderDate() %></td>
                                <td>$<%= order.getTotalAmount() %></td>
                                <td><span class="status-<%= order.getStatus() %>"><%= order.getStatus() %></span></td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/user/orders?id=<%= order.getId() %>" class="btn btn-small">View</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% } else { %>
                    <p>You have no recent orders.</p>
                    <% } %>
                    
                    <div class="action-links">
                        <a href="<%=request.getContextPath()%>/user/orders" class="btn btn-secondary">View All Orders</a>
                        <a href="<%=request.getContextPath()%>/books" class="btn btn-primary">Browse Books</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
    
    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
