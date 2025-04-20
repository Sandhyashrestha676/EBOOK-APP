package controller;

import dao.OrderDAO;
import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(value="/user/orders")
public class UserOrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO;
    
    public void init() {
        orderDAO = new OrderDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if there's a success message
        String success = request.getParameter("success");
        if ("true".equals(success)) {
            request.setAttribute("successMessage", "Your order has been placed successfully!");
        }
        
        // Get order details if specified
        String orderId = request.getParameter("id");
        if (orderId != null && !orderId.isEmpty()) {
            try {
                int id = Integer.parseInt(orderId);
                Order order = orderDAO.getOrderById(id);
                
                if (order != null && order.getUserId() == user.getId()) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/user/order-details.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid order ID, continue to show all orders
            }
        }
        
        // Get all orders for the user
        List<Order> orders = orderDAO.getOrdersByUser(user.getId());
        request.setAttribute("orders", orders);
        
        request.getRequestDispatcher("/user/orders.jsp").forward(request, response);
    }
}
