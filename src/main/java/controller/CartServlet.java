package controller;

import dao.BookDAO;
import dao.CartDAO;
import model.Book;
import model.CartItem;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(value="/cart/*")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartDAO cartDAO;
    private BookDAO bookDAO;
    
    public void init() {
        cartDAO = new CartDAO();
        bookDAO = new BookDAO();
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
        request.setAttribute("cartItems", cartItems);
        
        // Calculate total
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal().doubleValue();
        }
        request.setAttribute("total", total);
        
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Add to cart
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            Book book = bookDAO.getBookById(bookId);
            if (book != null && book.getStock() >= quantity) {
                CartItem cartItem = new CartItem();
                cartItem.setUserId(user.getId());
                cartItem.setBookId(bookId);
                cartItem.setQuantity(quantity);
                
                cartDAO.addToCart(cartItem);
            }
        } else if ("update".equals(action)) {
            // Update cart item
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            CartItem cartItem = new CartItem();
            cartItem.setId(cartItemId);
            cartItem.setUserId(user.getId());
            cartItem.setQuantity(quantity);
            
            cartDAO.updateCartItem(cartItem);
        } else if ("remove".equals(action)) {
            // Remove from cart
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
            cartDAO.removeFromCart(cartItemId, user.getId());
        } else if ("clear".equals(action)) {
            // Clear cart
            cartDAO.clearCart(user.getId());
        }
        
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
