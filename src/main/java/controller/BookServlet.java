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

@WebServlet(value="/books/*")
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    private static final int BOOKS_PER_PAGE = 8; // Number of books to display per page

    public void init() {
        bookDAO = new BookDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String category = request.getParameter("category");

        System.out.println("BookServlet: pathInfo=" + pathInfo + ", category=" + category);

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

            int totalBooks;
            List<Book> books;

            // Check if category filter is applied
            if (category != null && !category.isEmpty()) {
                // Get books by category with pagination
                System.out.println("Filtering by category: " + category);
                totalBooks = bookDAO.getTotalBooksByCategory(category);
                books = bookDAO.getBooksByCategory(category, page, BOOKS_PER_PAGE);
                request.setAttribute("selectedCategory", category);
            } else {
                // List all books with pagination
                System.out.println("Showing all books");
                totalBooks = bookDAO.getTotalBooks();
                books = bookDAO.getBooks(page, BOOKS_PER_PAGE);
            }

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);

            // Ensure page doesn't exceed total pages
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
                // Reload books with corrected page
                if (category != null && !category.isEmpty()) {
                    books = bookDAO.getBooksByCategory(category, page, BOOKS_PER_PAGE);
                } else {
                    books = bookDAO.getBooks(page, BOOKS_PER_PAGE);
                }
            }

            System.out.println("Found " + books.size() + " books on page " + page + " of " + totalPages);

            // Set attributes for JSP
            request.setAttribute("books", books);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("booksPerPage", BOOKS_PER_PAGE);

            request.getRequestDispatcher("/books.jsp").forward(request, response);
        } else {
            // Get book details
            try {
                int bookId = Integer.parseInt(pathInfo.substring(1));
                Book book = bookDAO.getBookById(bookId);

                if (book != null) {
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/book-details.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/books");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/books");
            }
        }
    }
}
