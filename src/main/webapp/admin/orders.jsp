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

            <!-- Dashboard menu moved to header -->

            <div class="dashboard-content">
                <div class="dashboard-section">
                    <h2>Orders (Page <%= request.getAttribute("currentPage") %> of <%= request.getAttribute("totalPages") %>, Total: <%= request.getAttribute("totalOrders") %>)</h2>
                    <table class="dashboard-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Book</th>
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
                                <td>
                                    <% if (order.getOrderItems() != null && !order.getOrderItems().isEmpty() && order.getOrderItems().get(0).getBook() != null) {
                                        String imageUrl = order.getOrderItems().get(0).getBook().getImageUrl();
                                        if (imageUrl != null && !imageUrl.isEmpty()) {
                                    %>
                                        <img src="<%=request.getContextPath()%>/<%= imageUrl %>"
                                             alt="Book Cover" class="order-item-image" style="width: 50px; height: auto;">
                                    <% } else { %>
                                        <img src="<%=request.getContextPath()%>/images/default-book.jpg" alt="No Image" class="order-item-image" style="width: 50px; height: auto;">
                                    <% } } else { %>
                                        <img src="<%=request.getContextPath()%>/images/default-book.jpg" alt="No Image" class="order-item-image" style="width: 50px; height: auto;">
                                    <% } %>
                                </td>
                                <td><%= order.getUserId() %></td>
                                <td><%= order.getOrderDate() %></td>
                                <td>$<%= order.getTotalAmount() %></td>
                                <td><span class="status-<%= order.getStatus() %>"><%= order.getStatus() %></span></td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/admin/orders/view/<%= order.getId() %>" class="btn btn-info btn-small">View</a>
                                    <% if (!"delivered".equals(order.getStatus()) && !"cancelled".equals(order.getStatus()) && !"canceled".equals(order.getStatus())) { %>
                                    <a href="<%=request.getContextPath()%>/admin/orders/edit/<%= order.getId() %>" class="btn btn-small">Edit</a>
                                    <a href="<%=request.getContextPath()%>/admin/orders/delete/<%= order.getId() %>?t=<%= System.currentTimeMillis() %>" class="btn btn-danger btn-small">Delete</a>
                                    <% } %>
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

                    <!-- Pagination Controls -->
                    <%
                    Integer currentPage = (Integer) request.getAttribute("currentPage");
                    Integer totalPages = (Integer) request.getAttribute("totalPages");

                    if (currentPage != null && totalPages != null && totalPages > 1) {
                    %>
                    <div style="width: 100%; display: flex; justify-content: center; margin-top: 20px;">
                        <div class="pagination">
                            <% if (currentPage > 1) { %>
                            <a href="<%=request.getContextPath()%>/admin/orders?page=<%= currentPage - 1 %>" class="pagination-link nav-btn" aria-label="Previous page"><i class="fas fa-chevron-left"></i></a>
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
                            <a href="<%=request.getContextPath()%>/admin/orders?page=<%= i %>"
                               class="pagination-link <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
                            <% } %>

                            <% if (currentPage < totalPages) { %>
                            <a href="<%=request.getContextPath()%>/admin/orders?page=<%= currentPage + 1 %>" class="pagination-link nav-btn" aria-label="Next page"><i class="fas fa-chevron-right"></i></a>
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
