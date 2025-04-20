<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <h1>My Orders</h1>
            </div>
            
            <div class="dashboard-menu">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/user/dashboard">Dashboard</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/user/orders">My Orders</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/profile">My Profile</a></li>
                </ul>
            </div>
            
            <div class="dashboard-content">
                <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
                <% } %>
                
                <div class="dashboard-section">
                    <% 
                    List<Order> orders = (List<Order>) request.getAttribute("orders");
                    if (orders != null && !orders.isEmpty()) {
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
                            <% for (Order order : orders) { %>
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
                    <p>You have no orders.</p>
                    <% } %>
                    
                    <div class="action-links">
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
