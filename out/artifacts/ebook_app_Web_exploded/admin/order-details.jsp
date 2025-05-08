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
                    <li><a href="<%=request.getContextPath()%>/admin/dashboard">Dashboard</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/books">Manage Books</a></li>
                    <li><a href="<%=request.getContextPath()%>/admin/users">Manage Users</a></li>
                    <li class="active"><a href="<%=request.getContextPath()%>/admin/orders">Manage Orders</a></li>
                </ul>
            </div>

            <div class="dashboard-content">
                <% if (request.getParameter("updated") != null) { %>
                <div class="alert alert-success">Order status updated successfully.</div>
                <% } else if (request.getParameter("error") != null) { %>
                <div class="alert alert-error">Failed to update order status.</div>
                <% } %>

                <%
                Order order = (Order) request.getAttribute("order");
                if (order != null) {
                %>
                <div class="order-details">
                    <div class="order-header">
                        <div class="order-info">
                            <h2>Order #<%= order.getId() %></h2>
                            <p><strong>User ID:</strong> <%= order.getUserId() %></p>
                            <p><strong>Date:</strong> <%= order.getOrderDate() %></p>
                            <p><strong>Total:</strong> $<%= order.getTotalAmount() %></p>
                            <p><strong>Status:</strong> <span class="status-<%= order.getStatus() %>"><%= order.getStatus() %></span></p>
                        </div>

                        <div class="order-actions">
                            <form action="<%=request.getContextPath()%>/admin/orders" method="post">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="orderId" value="<%= order.getId() %>">

                                <div class="form-group">
                                    <label for="status">Update Status:</label>
                                    <select id="status" name="status">
                                        <option value="pending" <%= "pending".equals(order.getStatus()) ? "selected" : "" %>>Pending</option>
                                        <option value="processing" <%= "processing".equals(order.getStatus()) ? "selected" : "" %>>Processing</option>
                                        <option value="shipped" <%= "shipped".equals(order.getStatus()) ? "selected" : "" %>>Shipped</option>
                                        <option value="delivered" <%= "delivered".equals(order.getStatus()) ? "selected" : "" %>>Delivered</option>
                                        <option value="cancelled" <%= "cancelled".equals(order.getStatus()) ? "selected" : "" %>>Cancelled</option>
                                    </select>
                                    <button type="submit" class="btn btn-primary">Update</button>
                                </div>
                            </form>
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
                                        <%
                                        String imageUrl = item.getBook().getImageUrl();
                                        if (imageUrl != null && !imageUrl.isEmpty()) {
                                        %>
                                            <img src="<%=request.getContextPath()%>/<%= imageUrl %>"
                                                 alt="<%= item.getBook().getTitle() %>" class="order-item-image">
                                        <% } else { %>
                                            <img src="<%=request.getContextPath()%>/images/default-book.jpg"
                                                 alt="<%= item.getBook().getTitle() %>" class="order-item-image">
                                        <% } %>
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
                        <a href="<%=request.getContextPath()%>/admin/orders" class="btn btn-primary">Back to Orders</a>
                    </div>
                </div>
                <% } else { %>
                <div class="error-message">
                    <p>Order not found.</p>
                    <a href="<%=request.getContextPath()%>/admin/orders" class="btn btn-primary">Back to Orders</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
