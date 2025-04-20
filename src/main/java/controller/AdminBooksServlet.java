package controller;

import dao.BookDAO;
import model.Book;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(value="/admin/books/*")
public class AdminBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;

    public void init() {
        bookDAO = new BookDAO();
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
            // Get category filter if provided
            String category = request.getParameter("category");

            List<Book> books;
            if (category != null && !category.isEmpty()) {
                // Filter books by category
                books = bookDAO.getBooksByCategory(category);
                request.setAttribute("selectedCategory", category);
            } else {
                // List all books
                books = bookDAO.getAllBooks();
            }

            request.setAttribute("books", books);
            request.getRequestDispatcher("/admin/books-list.jsp").forward(request, response);
        } else if (pathInfo.equals("/add")) {
            // Show add book form
            request.getRequestDispatcher("/admin/book-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            // Show edit book form
            try {
                int bookId = Integer.parseInt(pathInfo.substring(6));
                Book book = bookDAO.getBookById(bookId);

                if (book != null) {
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/admin/book-form.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/books");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/books");
            }
        } else if (pathInfo.startsWith("/delete/")) {
            // Delete book
            try {
                int bookId = Integer.parseInt(pathInfo.substring(8));
                bookDAO.deleteBook(bookId);
                response.sendRedirect(request.getContextPath() + "/admin/books?deleted=true");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/books");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/books");
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

        if ("add".equals(action)) {
            // Add new book
            Book book = new Book();
            book.setTitle(request.getParameter("title"));
            book.setAuthor(request.getParameter("author"));
            book.setDescription(request.getParameter("description"));
            book.setCategory(request.getParameter("category"));
            book.setGenre(request.getParameter("genre"));
            book.setPrice(new BigDecimal(request.getParameter("price")));
            book.setImageUrl(request.getParameter("imageUrl"));
            book.setStock(Integer.parseInt(request.getParameter("stock")));

            if (bookDAO.addBook(book)) {
                response.sendRedirect(request.getContextPath() + "/admin/books?added=true");
            } else {
                request.setAttribute("errorMessage", "Failed to add book");
                request.setAttribute("book", book);
                request.getRequestDispatcher("/admin/book-form.jsp").forward(request, response);
            }
        } else if ("update".equals(action)) {
            // Update book
            Book book = new Book();
            book.setId(Integer.parseInt(request.getParameter("id")));
            book.setTitle(request.getParameter("title"));
            book.setAuthor(request.getParameter("author"));
            book.setDescription(request.getParameter("description"));
            book.setCategory(request.getParameter("category"));
            book.setGenre(request.getParameter("genre"));
            book.setPrice(new BigDecimal(request.getParameter("price")));
            book.setImageUrl(request.getParameter("imageUrl"));
            book.setStock(Integer.parseInt(request.getParameter("stock")));

            if (bookDAO.updateBook(book)) {
                response.sendRedirect(request.getContextPath() + "/admin/books?updated=true");
            } else {
                request.setAttribute("errorMessage", "Failed to update book");
                request.setAttribute("book", book);
                request.getRequestDispatcher("/admin/book-form.jsp").forward(request, response);
            }
        }
    }
}
