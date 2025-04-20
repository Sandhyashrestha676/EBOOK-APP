<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Shopping Cart</h1>

        <%
        List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
        if (cartItems != null && !cartItems.isEmpty()) {
        %>
        <div class="cart-items">
            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Book</th>
                        <th>Title</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    BigDecimal total = BigDecimal.ZERO;
                    for (CartItem item : cartItems) {
                        total = total.add(item.getSubtotal());
                    %>
                    <tr>
                        <td>
                            <img src="<%= item.getBook().getImageUrl() != null ? request.getContextPath() + "/" + item.getBook().getImageUrl() : request.getContextPath() + "/images/default-book.jpg" %>"
                                 alt="<%= item.getBook().getTitle() %>" class="cart-item-image">
                        </td>
                        <td><%= item.getBook().getTitle() %></td>
                        <td>$<%= item.getBook().getPrice() %></td>
                        <td>
                            <form action="<%=request.getContextPath()%>/cart" method="post" class="quantity-form">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="cartItemId" value="<%= item.getId() %>">
                                <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1" max="<%= item.getBook().getStock() %>">
                                <button type="submit" class="btn btn-small">Update</button>
                            </form>
                        </td>
                        <td>$<%= item.getSubtotal() %></td>
                        <td>
                            <form action="<%=request.getContextPath()%>/cart" method="post">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="cartItemId" value="<%= item.getId() %>">
                                <button type="submit" class="btn btn-danger btn-small">Remove</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" class="text-right"><strong>Total:</strong></td>
                        <td><strong>$<%= total %></strong></td>
                        <td></td>
                    </tr>
                </tfoot>
            </table>

            <div class="cart-actions">
                <form action="<%=request.getContextPath()%>/cart" method="post" class="inline-form">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="btn btn-danger">Clear Cart</button>
                </form>

                <a href="<%=request.getContextPath()%>/books" class="btn btn-secondary">Continue Shopping</a>
                <a href="<%=request.getContextPath()%>/checkout" class="btn btn-primary">Proceed to Checkout</a>
            </div>
        </div>
        <% } else { %>
        <div class="empty-cart">
            <p>Your cart is empty.</p>
            <a href="<%=request.getContextPath()%>/books" class="btn btn-primary">Browse Books</a>
        </div>
        <% } %>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
