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
        
        // Get recent orders
        List<Order> recentOrders = orderDAO.getAllOrders();
        request.setAttribute("recentOrders", recentOrders);
        
        // Get book count
        List<Book> books = bookDAO.getAllBooks();
        request.setAttribute("bookCount", books.size());
        
        // Get user count
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("userCount", users.size());
        
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
