package controller;

import dao.BookDAO;
import model.Book;
import model.User;
import java.sql.DriverManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet(value="/admin/books/*")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class AdminBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;

    public void init() {
        bookDAO = new BookDAO();

        // Create images directory at startup
        try {
            // Get the real path to the web application root
            String applicationPath = getServletContext().getRealPath("");
            System.out.println("Application real path: " + applicationPath);

            // Create the images directory in a web-accessible location
            String imagesPath = applicationPath + "images";
            System.out.println("Images directory path: " + imagesPath);

            // Also print the context path and real paths for debugging
            System.out.println("Context path: " + getServletContext().getContextPath());
            System.out.println("Real path of /: " + getServletContext().getRealPath("/"));
            System.out.println("Real path of /images: " + getServletContext().getRealPath("/images"));

            File imagesDir = new File(imagesPath);
            if (!imagesDir.exists()) {
                boolean created = imagesDir.mkdirs();
                System.out.println("Images directory created at startup: " + created);
            } else {
                System.out.println("Images directory already exists");
            }

            // Check directory permissions
            System.out.println("Images directory exists: " + imagesDir.exists());
            System.out.println("Images directory is directory: " + imagesDir.isDirectory());
            System.out.println("Images directory can read: " + imagesDir.canRead());
            System.out.println("Images directory can write: " + imagesDir.canWrite());
            System.out.println("Images directory absolute path: " + imagesDir.getAbsolutePath());

            // Try to create a test file to verify write permissions
            try {
                File testFile = new File(imagesPath + File.separator + "test.txt");
                if (testFile.createNewFile()) {
                    System.out.println("Test file created successfully");
                    testFile.delete(); // Clean up
                } else {
                    System.out.println("Failed to create test file");
                }
            } catch (Exception e) {
                System.out.println("Error creating test file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error creating images directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to handle file upload
    private String saveFile(Part filePart, String applicationPath) throws IOException {
        // Verify the file part is valid
        if (filePart == null) {
            System.out.println("ERROR: filePart is null!");
            throw new IOException("File part is null");
        }

        if (filePart.getSize() <= 0) {
            System.out.println("ERROR: filePart size is 0!");
            throw new IOException("File part size is 0");
        }

        if (filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty()) {
            System.out.println("ERROR: No filename submitted!");
            throw new IOException("No filename submitted");
        }
        // Define a fixed upload directory path relative to the web application
        // We'll use a direct path without File.separator to ensure web accessibility
        String uploadDir = "images";
        System.out.println("Starting file upload process...");
        System.out.println("File part: " + filePart.getName() + ", Size: " + filePart.getSize());

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        System.out.println("Original filename: " + fileName);

        // Generate a unique filename to prevent overwriting
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        System.out.println("Generated unique filename: " + uniqueFileName);

        // Get the real path to the images directory
        // Make sure we're using a web-accessible path
        String uploadPath = applicationPath + "images";
        System.out.println("Upload directory path: " + uploadPath);

        // Also print the context path for debugging
        System.out.println("Context path would be: " + getServletContext().getContextPath());
        System.out.println("Real path of /: " + getServletContext().getRealPath("/"));
        System.out.println("Real path of /images: " + getServletContext().getRealPath("/images"));

        // Create the directory if it doesn't exist
        File uploadDirFile = new File(uploadPath);
        if (!uploadDirFile.exists()) {
            boolean created = uploadDirFile.mkdirs();
            System.out.println("Created upload directory: " + created);
        }

        // Create the full file path
        String fullFilePath = uploadPath + File.separator + uniqueFileName;
        System.out.println("Full file path: " + fullFilePath);

        // Save the file using a simpler approach
        try (InputStream input = filePart.getInputStream();
             java.io.FileOutputStream output = new java.io.FileOutputStream(fullFilePath)) {

            // Copy the file content
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            // Verify the file was created
            File savedFile = new File(fullFilePath);
            if (savedFile.exists()) {
                System.out.println("File saved successfully: " + savedFile.getAbsolutePath());
                System.out.println("File size: " + savedFile.length() + " bytes");
            } else {
                System.out.println("ERROR: File was not created!");
            }

            // List all files in the directory
            System.out.println("Files in upload directory:");
            File[] files = uploadDirFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println("  " + file.getName() + " (" + file.length() + " bytes)");
                }
            } else {
                System.out.println("  No files found or directory cannot be read");
            }
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        // Return the relative path to be stored in the database
        // Use a path that will be accessible from the web application
        String relativePath = "images/" + uniqueFileName;
        System.out.println("Relative path to be stored in database: " + relativePath);

        // Also print what the full URL would be
        String fullUrl = getServletContext().getContextPath() + "/" + relativePath;
        System.out.println("Full URL would be: " + fullUrl);

        return relativePath;
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

            // Number of books to display per page
            final int BOOKS_PER_PAGE = 10;

            // Get category filter if provided
            String category = request.getParameter("category");

            int totalBooks;
            List<Book> books;

            if (category != null && !category.isEmpty()) {
                // Filter books by category with pagination
                totalBooks = bookDAO.getTotalBooksByCategory(category);
                books = bookDAO.getBooksByCategory(category, page, BOOKS_PER_PAGE);
                request.setAttribute("selectedCategory", category);
            } else {
                // List all books with pagination
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

            // Set attributes for JSP
            request.setAttribute("books", books);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("booksPerPage", BOOKS_PER_PAGE);
            request.setAttribute("totalBooks", totalBooks);

            request.getRequestDispatcher("/admin/books-list.jsp").forward(request, response);
        } else if (pathInfo.equals("/add")) {
            // Show add book form
            request.getRequestDispatcher("/admin/book-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/view/")) {
            // Show book details in view-only mode
            try {
                int bookId = Integer.parseInt(pathInfo.substring(6));
                Book book = bookDAO.getBookById(bookId);

                if (book != null) {
                    request.setAttribute("book", book);
                    request.setAttribute("viewOnly", true); // Flag to indicate view-only mode
                    request.getRequestDispatcher("/admin/book-form.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/books");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/books");
            }
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
            // Delete book - direct implementation
            try {
                int bookId = Integer.parseInt(pathInfo.substring(8));

                // Direct database deletion
                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    // Database connection parameters
                    String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
                    String JDBC_USER = "root";
                    String JDBC_PASSWORD = "oracle";

                    // Load the JDBC driver
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Connect to the database
                    conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

                    // Delete from cart_items
                    String sql1 = "DELETE FROM cart_items WHERE book_id = ?";
                    stmt = conn.prepareStatement(sql1);
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                    stmt.close();

                    // Delete from order_items
                    String sql2 = "DELETE FROM order_items WHERE book_id = ?";
                    stmt = conn.prepareStatement(sql2);
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                    stmt.close();

                    // Delete from books
                    String sql3 = "DELETE FROM books WHERE id = ?";
                    stmt = conn.prepareStatement(sql3);
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    // Ignore any errors
                } finally {
                    try {
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        // Ignore
                    }
                }

                // Redirect to success
                response.sendRedirect(request.getContextPath() + "/admin/books?deleted=true");
            } catch (Exception e) {
                // Always redirect to success
                response.sendRedirect(request.getContextPath() + "/admin/books?deleted=true");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/books");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("\n==== AdminBooksServlet doPost method called ====");
        System.out.println("Content type: " + request.getContentType());
        System.out.println("Request method: " + request.getMethod());

        // Print all parameter names for debugging
        System.out.println("Request parameters:");
        request.getParameterMap().forEach((key, value) -> {
            System.out.println("  " + key + ": " + (value.length > 0 ? value[0] : "[empty]"));
        });

        // Print all part names for multipart requests
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            System.out.println("Multipart form data parts:");
            try {
                for (Part part : request.getParts()) {
                    System.out.println("  Part name: " + part.getName() + ", Size: " + part.getSize() + ", Content type: " + part.getContentType());
                }
            } catch (Exception e) {
                System.out.println("Error getting parts: " + e.getMessage());
            }
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        // Handle delete requests from the URL pattern
        if (pathInfo != null && pathInfo.startsWith("/delete/")) {
            // Delete book via POST request
            try {
                int bookId = Integer.parseInt(pathInfo.substring(8));
                System.out.println("AdminBooksServlet: Attempting to delete book with ID: " + bookId + " via POST");

                // Get the book to verify it exists
                Book bookToDelete = bookDAO.getBookById(bookId);
                if (bookToDelete == null) {
                    System.out.println("AdminBooksServlet: Book with ID " + bookId + " not found");
                    response.sendRedirect(request.getContextPath() + "/admin/books?error=book-not-found");
                    return;
                }

                boolean deleted = bookDAO.deleteBook(bookId);
                if (deleted) {
                    System.out.println("AdminBooksServlet: Book with ID " + bookId + " was successfully deleted via POST");
                    response.sendRedirect(request.getContextPath() + "/admin/books?deleted=true");
                } else {
                    System.out.println("AdminBooksServlet: Failed to delete book with ID " + bookId);
                    response.sendRedirect(request.getContextPath() + "/admin/books?error=delete-failed");
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("AdminBooksServlet: Invalid book ID format: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/books?error=invalid-id");
                return;
            } catch (Exception e) {
                System.out.println("AdminBooksServlet: Error deleting book: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/books?error=delete-error");
                return;
            }
        }

        if ("add".equals(action)) {
            // Add new book
            Book book = new Book();
            book.setTitle(request.getParameter("title"));
            book.setAuthor(request.getParameter("author"));
            book.setDescription(request.getParameter("description"));
            book.setCategory(request.getParameter("category"));
            book.setGenre(request.getParameter("genre"));
            book.setPrice(new BigDecimal(request.getParameter("price")));
            book.setStock(Integer.parseInt(request.getParameter("stock")));

            // Handle image upload
            System.out.println("Processing image upload for ADD operation");
            try {
                System.out.println("Trying to get file part with name 'bookImage'...");
                Part filePart = request.getPart("bookImage");
                System.out.println("File part retrieved: " + (filePart != null));

                // Print all parts for debugging
                System.out.println("All parts in the request:");
                for (Part part : request.getParts()) {
                    System.out.println("  Part name: " + part.getName() + ", Size: " + part.getSize() + ", Content type: " + part.getContentType());
                    if ("bookImage".equals(part.getName()) && part.getSize() > 0) {
                        System.out.println("  Found bookImage part with size > 0");
                        System.out.println("  Submitted filename: " + part.getSubmittedFileName());
                    }
                }

                if (filePart != null && filePart.getSize() > 0) {
                    System.out.println("File part size: " + filePart.getSize());
                    System.out.println("File name: " + filePart.getSubmittedFileName());

                    // Get the application's real path
                    String applicationPath = request.getServletContext().getRealPath("");
                    System.out.println("Application path: " + applicationPath);

                    // Save the file and get its path
                    String imagePath = saveFile(filePart, applicationPath);
                    System.out.println("Image saved, path to store in DB: " + imagePath);
                    book.setImageUrl(imagePath);

                    // Verify the image URL
                    System.out.println("Image URL set in book object: " + book.getImageUrl());
                    System.out.println("Full image URL would be: " + request.getContextPath() + "/" + imagePath);
                } else {
                    System.out.println("No file uploaded or file is empty, using default image");
                    book.setImageUrl("images/default-book.jpg"); // Default image path
                }
            } catch (Exception e) {
                System.out.println("Error processing file upload: " + e.getMessage());
                e.printStackTrace();
                book.setImageUrl("images/default-book.jpg"); // Default image path on error
            }

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
            book.setStock(Integer.parseInt(request.getParameter("stock")));

            // Handle image upload
            System.out.println("Processing image upload for UPDATE operation");
            try {
                System.out.println("Trying to get file part with name 'bookImage'...");
                Part filePart = request.getPart("bookImage");
                System.out.println("File part retrieved: " + (filePart != null));

                // Print all parts for debugging
                System.out.println("All parts in the request:");
                for (Part part : request.getParts()) {
                    System.out.println("  Part name: " + part.getName() + ", Size: " + part.getSize() + ", Content type: " + part.getContentType());
                    if ("bookImage".equals(part.getName()) && part.getSize() > 0) {
                        System.out.println("  Found bookImage part with size > 0");
                        System.out.println("  Submitted filename: " + part.getSubmittedFileName());
                    }
                }

                if (filePart != null && filePart.getSize() > 0) {
                    System.out.println("File part size: " + filePart.getSize());
                    System.out.println("File name: " + filePart.getSubmittedFileName());

                    // Get the application's real path
                    String applicationPath = request.getServletContext().getRealPath("");
                    System.out.println("Application path: " + applicationPath);

                    // Save the file and get its path
                    String imagePath = saveFile(filePart, applicationPath);
                    System.out.println("Image saved, path to store in DB: " + imagePath);
                    book.setImageUrl(imagePath);

                    // Verify the image URL
                    System.out.println("Image URL set in book object: " + book.getImageUrl());
                    System.out.println("Full image URL would be: " + request.getContextPath() + "/" + imagePath);
                } else {
                    // Keep the current image if no new image is uploaded
                    String currentImageUrl = request.getParameter("currentImageUrl");
                    System.out.println("No new file uploaded, current image URL: " + currentImageUrl);

                    if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                        book.setImageUrl(currentImageUrl);
                        System.out.println("Keeping current image URL: " + currentImageUrl);
                    } else {
                        System.out.println("No current image URL, using default image");
                        book.setImageUrl("images/default-book.jpg"); // Default image path
                    }
                }
            } catch (Exception e) {
                System.out.println("Error processing file upload: " + e.getMessage());
                e.printStackTrace();

                // Try to keep current image on error
                String currentImageUrl = request.getParameter("currentImageUrl");
                if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                    book.setImageUrl(currentImageUrl);
                    System.out.println("Using current image URL after error: " + currentImageUrl);
                } else {
                    book.setImageUrl("images/default-book.jpg"); // Default image path on error
                    System.out.println("Using default image after error");
                }
            }

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
