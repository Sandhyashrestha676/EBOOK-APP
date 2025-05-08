package controller;

import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value="/user/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get the confirmed order from the session
        Order order = (Order) session.getAttribute("confirmedOrder");
        String transactionId = (String) session.getAttribute("transactionId");
        
        if (order == null) {
            // If no confirmed order in session, redirect to orders page
            response.sendRedirect(request.getContextPath() + "/user/orders");
            return;
        }
        
        // Set attributes for the confirmation page
        request.setAttribute("order", order);
        request.setAttribute("transactionId", transactionId);
        
        // Remove the order from session to prevent showing it again on refresh
        session.removeAttribute("confirmedOrder");
        session.removeAttribute("transactionId");
        
        // Forward to the confirmation page
        request.getRequestDispatcher("/user/order-confirmation.jsp").forward(request, response);
    }
}
