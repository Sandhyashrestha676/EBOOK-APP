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

@WebServlet(value="/admin/orders/*")
public class AdminOrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO;
    
    public void init() {
        orderDAO = new OrderDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all orders
            List<Order> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/view/")) {
            // View order details
            try {
                int orderId = Integer.parseInt(pathInfo.substring(6));
                Order order = orderDAO.getOrderById(orderId);
                
                if (order != null) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/admin/order-details.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/orders");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/orders");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("updateStatus".equals(action)) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            
            if (orderDAO.updateOrderStatus(orderId, status)) {
                response.sendRedirect(request.getContextPath() + "/admin/orders/view/" + orderId + "?updated=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/orders/view/" + orderId + "?error=true");
            }
        }
    }
}
