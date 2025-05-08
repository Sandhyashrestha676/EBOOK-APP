package controller;

import dao.BookDAO;
import dao.OrderDAO;
import dao.UserDAO;
import model.Book;
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

@WebServlet(value="/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    private OrderDAO orderDAO;
    private UserDAO userDAO;

    public void init() {
        bookDAO = new BookDAO();
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

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

        // Get paginated recent orders
        List<Order> recentOrders = orderDAO.getOrders(page, ORDERS_PER_PAGE);

        // Get book count
        int bookCount = bookDAO.getTotalBooks();
        request.setAttribute("bookCount", bookCount);

        // Get user count
        int userCount = userDAO.getTotalUsers();
        request.setAttribute("userCount", userCount);

        // Set all attributes for JSP
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ordersPerPage", ORDERS_PER_PAGE);
        request.setAttribute("totalOrders", totalOrders);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
