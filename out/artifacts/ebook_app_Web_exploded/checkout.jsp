<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Checkout</h1>

        <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error">
            <%= request.getAttribute("errorMessage") %>
        </div>
        <% } %>

        <div class="checkout-container">
            <div class="order-summary">
                <h2>Order Summary</h2>

                <%
                List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
                BigDecimal total = (BigDecimal) request.getAttribute("total");

                if (cartItems != null && !cartItems.isEmpty()) {
                %>
                <table class="order-table">
                    <thead>
                        <tr>
                            <th>Book</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (CartItem item : cartItems) { %>
                        <tr>
                            <td>
                                <div class="order-book-info">
                                    <img src="<%= item.getBook().getImageUrl() != null ? request.getContextPath() + "/" + item.getBook().getImageUrl() : request.getContextPath() + "/images/default-book.jpg" %>"
                                         alt="<%= item.getBook().getTitle() %>" class="order-item-image">
                                    <span><%= item.getBook().getTitle() %></span>
                                </div>
                            </td>
                            <td>$<%= item.getBook().getPrice() %></td>
                            <td><%= item.getQuantity() %></td>
                            <td>$<%= item.getSubtotal() %></td>
                        </tr>
                        <% } %>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" class="text-right"><strong>Total:</strong></td>
                            <td><strong>$<%= total %></strong></td>
                        </tr>
                    </tfoot>
                </table>
                <% } else { %>
                <p>Your cart is empty.</p>
                <% } %>
            </div>

            <div class="payment-form">
                <h2>Payment Information</h2>

                <form action="<%=request.getContextPath()%>/checkout" method="post">
                    <div class="form-group">
                        <label for="cardName">Name on Card</label>
                        <input type="text" id="cardName" name="cardName" required>
                    </div>

                    <div class="form-group">
                        <label for="cardNumber">Card Number</label>
                        <input type="text" id="cardNumber" name="cardNumber" pattern="[0-9]{13,19}" title="Please enter a valid card number (13-19 digits, numbers only)" maxlength="19" required>
                        <small class="form-text">Enter digits only, no spaces or dashes</small>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="expiryDate">Expiry Date</label>
                            <input type="text" id="expiryDate" name="expiryDate" pattern="(0[1-9]|1[0-2])\/([0-9]{2})" placeholder="MM/YY" title="Please enter a valid expiry date in MM/YY format" maxlength="5" required>
                            <small class="form-text">Format: MM/YY (e.g., 05/25)</small>
                        </div>

                        <div class="form-group">
                            <label for="cvv">CVV</label>
                            <input type="text" id="cvv" name="cvv" pattern="[0-9]{3,4}" title="Please enter a valid CVV (3-4 digits)" maxlength="4" required>
                            <small class="form-text">3-4 digits on the back of your card</small>
                        </div>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Place Order</button>
                        <a href="<%=request.getContextPath()%>/cart" class="btn btn-secondary">Back to Cart</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="<%=request.getContextPath()%>/js/script.js"></script>
</body>
</html>
