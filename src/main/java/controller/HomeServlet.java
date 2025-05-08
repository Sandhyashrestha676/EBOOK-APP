package controller;

import dao.BookDAO;
import model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value="/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    private static final int BOOKS_PER_PAGE = 8; // Number of books to display per page

    public void init() {
        bookDAO = new BookDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        // Get total number of books for pagination
        int totalBooks = bookDAO.getTotalBooks();
        int totalPages = (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);

        // Ensure page doesn't exceed total pages
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        // Get paginated books
        List<Book> featuredBooks = bookDAO.getBooks(page, BOOKS_PER_PAGE);

        // Set attributes for JSP
        request.setAttribute("featuredBooks", featuredBooks);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("booksPerPage", BOOKS_PER_PAGE);

        // Forward to home page
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
