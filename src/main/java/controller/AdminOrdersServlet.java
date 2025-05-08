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
            // Get pagination parameters
            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                    }
                } catch (NumberFormatException e) {
                    // If page parameter is invalid, default to page 1
                    page = 1;
                }
            }

            // Number of orders to display per page
            final int ORDERS_PER_PAGE = 10;

            // Get total number of orders for pagination
            int totalOrders = orderDAO.getTotalOrders();
            int totalPages = (int) Math.ceil((double) totalOrders / ORDERS_PER_PAGE);

            // Ensure page doesn't exceed total pages
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            // Get paginated orders
            List<Order> orders = orderDAO.getOrders(page, ORDERS_PER_PAGE);

            // Set attributes for JSP
            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("ordersPerPage", ORDERS_PER_PAGE);
            request.setAttribute("totalOrders", totalOrders);

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
        } else if (pathInfo.startsWith("/edit/")) {
            // Edit order
            try {
                int orderId = Integer.parseInt(pathInfo.substring(6));
                Order order = orderDAO.getOrderById(orderId);

                if (order != null) {
                    // Check if order is not delivered or cancelled
                    if (!"delivered".equals(order.getStatus()) && !"cancelled".equals(order.getStatus())) {
                        request.setAttribute("order", order);
                        request.setAttribute("editMode", true);
                        request.getRequestDispatcher("/admin/order-details.jsp").forward(request, response);
                    } else {
                        // Redirect to view if order is delivered or cancelled
                        response.sendRedirect(request.getContextPath() + "/admin/orders/view/" + orderId);
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/orders");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/orders");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Delete order
            try {
                int orderId = Integer.parseInt(pathInfo.substring(8));
                Order order = orderDAO.getOrderById(orderId);

                if (order != null) {
                    // Check if order is not delivered or cancelled
                    if (!"delivered".equals(order.getStatus()) && !"cancelled".equals(order.getStatus())) {
                        if (orderDAO.deleteOrder(orderId)) {
                            response.sendRedirect(request.getContextPath() + "/admin/orders?deleted=true");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/admin/orders?error=true");
                        }
                    } else {
                        // Redirect to view if order is delivered or cancelled
                        response.sendRedirect(request.getContextPath() + "/admin/orders/view/" + orderId);
                    }
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
