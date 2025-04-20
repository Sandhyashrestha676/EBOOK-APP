<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <h1>Manage Orders</h1>
            </div>
            
            <div class="dashboard-menu">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/books">Manage Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/users">Manage Users</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/admin/orders">Manage Orders</a></li>
                </ul>
            </div>
            
            <div class="dashboard-content">
                <div class="dashboard-section">
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
                            <% 
                            List<Order> orders = (List<Order>) request.getAttribute("orders");
                            if (orders != null && !orders.isEmpty()) {
                                for (Order order : orders) {
                            %>
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
                            <% 
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="6">No orders found.</td>
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
