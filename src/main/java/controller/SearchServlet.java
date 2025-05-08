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

@WebServlet(value="/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;

    public void init() {
        bookDAO = new BookDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

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

        // Number of books to display per page
        final int BOOKS_PER_PAGE = 8;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Get total search results count
            int totalResults = bookDAO.countSearchResults(keyword);
            int totalPages = (int) Math.ceil((double) totalResults / BOOKS_PER_PAGE);

            // Ensure page doesn't exceed total pages
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            // Get paginated search results
            List<Book> searchResults = bookDAO.searchBooks(keyword, page, BOOKS_PER_PAGE);

            // Set attributes for JSP
            request.setAttribute("books", searchResults);
            request.setAttribute("keyword", keyword);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("booksPerPage", BOOKS_PER_PAGE);
        }

        request.getRequestDispatcher("/search-results.jsp").forward(request, response);
    }
}
