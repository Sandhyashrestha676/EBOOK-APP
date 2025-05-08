<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation - E-Book Store</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
    <style>
        .confirmation-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .confirmation-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .confirmation-header h1 {
            color: #28a745;
            margin-bottom: 10px;
        }
        
        .confirmation-header p {
            font-size: 18px;
            color: #555;
        }
        
        .confirmation-details {
            margin-top: 30px;
            border-top: 1px solid #ddd;
            padding-top: 20px;
        }
        
        .confirmation-details h2 {
            margin-bottom: 15px;
            color: #333;
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 10px;
            background-color: #fff;
            border-radius: 4px;
        }
        
        .detail-label {
            font-weight: bold;
            color: #555;
        }
        
        .detail-value {
            color: #333;
        }
        
        .confirmation-footer {
            margin-top: 30px;
            text-align: center;
        }
        
        .confirmation-footer a {
            display: inline-block;
            margin: 10px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
        }
        
        .confirmation-footer a:hover {
            background-color: #0056b3;
        }
        
        .check-icon {
            font-size: 60px;
            color: #28a745;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <div class="container">
        <div class="confirmation-container">
            <div class="confirmation-header">
                <div class="check-icon">✓</div>
                <h1>Order Confirmed!</h1>
                <p>Thank you for your purchase. Your order has been successfully placed and your payment has been processed.</p>
            </div>
            
            <% 
            Order order = (Order) request.getAttribute("order");
            if (order != null) {
            %>
            <div class="confirmation-details">
                <h2>Order Details</h2>
                <div class="detail-row">
                    <span class="detail-label">Order ID:</span>
                    <span class="detail-value"><%= order.getId() %></span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Order Date:</span>
                    <span class="detail-value"><%= order.getOrderDate() %></span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Total Amount:</span>
                    <span class="detail-value">$<%= order.getTotalAmount() %></span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Status:</span>
                    <span class="detail-value"><%= order.getStatus() %></span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Payment Method:</span>
                    <span class="detail-value">Credit Card</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Transaction ID:</span>
                    <span class="detail-value"><%= request.getAttribute("transactionId") %></span>
                </div>
            </div>
            <% } %>
            
            <div class="confirmation-footer">
                <p>You will receive an email confirmation shortly with your order details.</p>
                <div>
                    <a href="<%=request.getContextPath()%>/user/orders">View My Orders</a>
                    <a href="<%=request.getContextPath()%>/books">Continue Shopping</a>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>
