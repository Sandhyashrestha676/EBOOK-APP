package controller;

import dao.BookDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value="/admin/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    
    public void init() {
        bookDAO = new BookDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String bookIdStr = request.getParameter("id");
        System.out.println("DeleteBookServlet: Received request to delete book with ID: " + bookIdStr);
        
        if (bookIdStr == null || bookIdStr.isEmpty()) {
            System.out.println("DeleteBookServlet: Book ID is null or empty");
            response.sendRedirect(request.getContextPath() + "/admin/books?error=missing-id");
            return;
        }
        
        try {
            int bookId = Integer.parseInt(bookIdStr);
            System.out.println("DeleteBookServlet: Attempting to delete book with ID: " + bookId);
            
            boolean deleted = bookDAO.deleteBook(bookId);
            if (deleted) {
                System.out.println("DeleteBookServlet: Book with ID " + bookId + " was successfully deleted");
                response.sendRedirect(request.getContextPath() + "/admin/books?deleted=true");
            } else {
                System.out.println("DeleteBookServlet: Failed to delete book with ID " + bookId);
                response.sendRedirect(request.getContextPath() + "/admin/books?error=delete-failed");
            }
        } catch (NumberFormatException e) {
            System.out.println("DeleteBookServlet: Invalid book ID format: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/books?error=invalid-id");
        } catch (Exception e) {
            System.out.println("DeleteBookServlet: Error deleting book: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/books?error=delete-error");
        }
    }
}
