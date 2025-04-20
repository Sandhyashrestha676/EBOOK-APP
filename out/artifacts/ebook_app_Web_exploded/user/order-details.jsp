<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Order" %>
<%@ page import="model.OrderItem" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />

    <div class="container">
        <div class="dashboard">
            <div class="dashboard-header">
                <h1>Order Details</h1>
            </div>

            <div class="dashboard-menu">
                <ul>
                    <li><a href="<%=request.getContextPath()%>/user/dashboard">Dashboard</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/user/orders">My Orders</a></li>
                    <li><a href="<%=request.getContextPath()%>/user/profile">My Profile</a></li>
                </ul>
            </div>

            <div class="dashboard-content">
                <%
                Order order = (Order) request.getAttribute("order");
                if (order != null) {
                %>
                <div class="order-details">
                    <div class="order-header">
                        <div class="order-info">
                            <h2>Order #<%= order.getId() %></h2>
                            <p><strong>Date:</strong> <%= order.getOrderDate() %></p>
                            <p><strong>Status:</strong> <span class="status-<%= order.getStatus() %>"><%= order.getStatus() %></span></p>
                            <p><strong>Total:</strong> $<%= order.getTotalAmount() %></p>
                        </div>
                    </div>

                    <div class="order-items">
                        <h3>Order Items</h3>

                        <table class="dashboard-table">
                            <thead>
                                <tr>
                                    <th>Book</th>
                                    <th>Title</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                List<OrderItem> orderItems = order.getOrderItems();
                                if (orderItems != null) {
                                    for (OrderItem item : orderItems) {
                                %>
                                <tr>
                                    <td>
                                        <img src="<%= item.getBook().getImageUrl() != null ? item.getBook().getImageUrl() : "images/default-book.jpg" %>"
                                             alt="<%= item.getBook().getTitle() %>" class="order-item-image">
                                    </td>
                                    <td><%= item.getBook().getTitle() %></td>
                                    <td>$<%= item.getPrice() %></td>
                                    <td><%= item.getQuantity() %></td>
                                    <td>$<%= item.getSubtotal() %></td>
                                </tr>
                                <%
                                    }
                                }
                                %>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="4" class="text-right"><strong>Total:</strong></td>
                                    <td><strong>$<%= order.getTotalAmount() %></strong></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <div class="action-links">
                        <a href="<%=request.getContextPath()%>/user/orders" class="btn btn-primary">Back to Orders</a>
                    </div>
                </div>
                <% } else { %>
                <div class="error-message">
                    <p>Order not found.</p>
                    <a href="<%=request.getContextPath()%>/user/orders" class="btn btn-primary">Back to Orders</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
