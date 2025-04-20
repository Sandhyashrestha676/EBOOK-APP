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

    public void init() {
        bookDAO = new BookDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String category = request.getParameter("category");

        System.out.println("BookServlet: pathInfo=" + pathInfo + ", category=" + category);

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Book> books;

            // Check if category filter is applied
            if (category != null && !category.isEmpty()) {
                // Get books by category
                System.out.println("Filtering by category: " + category);
                books = bookDAO.getBooksByCategory(category);
                request.setAttribute("selectedCategory", category);
            } else {
                // List all books
                System.out.println("Showing all books");
                books = bookDAO.getAllBooks();
            }

            System.out.println("Found " + books.size() + " books");
            request.setAttribute("books", books);
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
