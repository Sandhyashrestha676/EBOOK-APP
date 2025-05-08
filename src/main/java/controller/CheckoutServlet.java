package controller;

import dao.CartDAO;
import dao.OrderDAO;
import dao.PaymentDAO;
import model.CartItem;
import model.Order;
import model.OrderItem;
import model.Payment;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(value="/checkout/*")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private PaymentDAO paymentDAO;

    public void init() {
        cartDAO = new CartDAO();
        orderDAO = new OrderDAO();
        paymentDAO = new PaymentDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get cart items
        List<CartItem> cartItems = cartDAO.getCartItems(user.getId());

        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.setAttribute("cartItems", cartItems);

        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        request.setAttribute("total", total);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get cart items
        List<CartItem> cartItems = cartDAO.getCartItems(user.getId());

        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Validate payment information
        String cardNumber = request.getParameter("cardNumber");
        String cvv = request.getParameter("cvv");
        String expiryDate = request.getParameter("expiryDate");

        // Validate card number (should contain only digits)
        if (cardNumber == null || !cardNumber.matches("[0-9]{13,19}")) {
            request.setAttribute("errorMessage", "Please enter a valid card number (13-19 digits, numbers only)");
            request.setAttribute("cartItems", cartItems);

            // Calculate total for display
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : cartItems) {
                total = total.add(item.getSubtotal());
            }
            request.setAttribute("total", total);

            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        // Validate CVV (should contain only digits)
        if (cvv == null || !cvv.matches("[0-9]{3,4}")) {
            request.setAttribute("errorMessage", "Please enter a valid CVV (3-4 digits)");
            request.setAttribute("cartItems", cartItems);

            // Calculate total for display
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem item : cartItems) {
                total = total.add(item.getSubtotal());
            }
            request.setAttribute("total", total);

            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        // Create order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setOrderDate(new Date());

        // Calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        order.setTotalAmount(total);
        order.setStatus("pending");

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(cartItem.getBookId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);
        }

        // Save order to database
        int orderId = -1;
        try {
            orderId = orderDAO.createOrder(order, orderItems);
        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to process your order. Please try again.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        if (orderId > 0) {
            // Create payment record
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentMethod("Credit Card");
            payment.setCardNumber(cardNumber);
            payment.setCardHolderName(request.getParameter("cardName"));
            payment.setExpiryDate(expiryDate);
            payment.setCvv(cvv);
            payment.setAmount(total);
            payment.setStatus("completed");
            payment.setTransactionId("TXN" + System.currentTimeMillis()); // Generate a transaction ID

            // Save payment to database
            try {
                boolean paymentSaved = paymentDAO.createPayment(payment);

                if (paymentSaved) {
                    System.out.println("Payment saved successfully for order ID: " + orderId);
                } else {
                    System.out.println("Failed to save payment for order ID: " + orderId);
                }
            } catch (Exception e) {
                System.out.println("Error saving payment: " + e.getMessage());
                e.printStackTrace();
            }

            // Clear cart after successful order
            cartDAO.clearCart(user.getId());

            // Store the order and transaction ID in the session for the confirmation page
            session.setAttribute("confirmedOrder", order);
            session.setAttribute("transactionId", payment.getTransactionId());

            // Redirect to order confirmation page
            response.sendRedirect(request.getContextPath() + "/user/order-confirmation");
        } else {
            request.setAttribute("errorMessage", "Failed to process your order. Please try again.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        }
    }
}
