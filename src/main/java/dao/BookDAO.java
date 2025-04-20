package dao;

import model.Book;
import util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("BookDAO: Getting all books");

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM books";
            stmt = conn.prepareStatement(sql);
            System.out.println("Executing SQL: " + sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setDescription(rs.getString("description"));
                book.setCategory(rs.getString("category"));
                book.setGenre(rs.getString("genre"));
                book.setPrice(rs.getBigDecimal("price"));
                String imageUrl = rs.getString("image_url");
                System.out.println("Original image URL for " + book.getTitle() + ": " + imageUrl);
                // Convert example.com URLs to local image paths
                if (imageUrl != null && imageUrl.contains("example.com")) {
                    String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    imageUrl = "images/" + imageName;
                    System.out.println("Converted to: " + imageUrl);
                }

                // Special handling for The Great Gatsby
                if (book.getTitle().equals("The Great Gatsby")) {
                    System.out.println("Special handling for The Great Gatsby");
                    imageUrl = "images/gatsby.jpg";
                }
                book.setImageUrl(imageUrl);
                System.out.println("Final image URL: " + book.getImageUrl());
                book.setStock(rs.getInt("stock"));
                books.add(book);
                System.out.println("Found book: " + book.getTitle() + " with category: " + book.getCategory());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return books;
    }

    public Book getBookById(int id) {
        Book book = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM books WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setDescription(rs.getString("description"));
                book.setCategory(rs.getString("category"));
                book.setGenre(rs.getString("genre"));
                book.setPrice(rs.getBigDecimal("price"));
                String imageUrl = rs.getString("image_url");
                // Convert example.com URLs to local image paths
                if (imageUrl != null && imageUrl.contains("example.com")) {
                    String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    imageUrl = "images/" + imageName;
                }

                // Special handling for The Great Gatsby
                if (book.getTitle().equals("The Great Gatsby")) {
                    imageUrl = "images/gatsby.jpg";
                }
                book.setImageUrl(imageUrl);
                book.setStock(rs.getInt("stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return book;
    }

    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("BookDAO: Getting books by category: " + category);

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM books WHERE category = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            System.out.println("Executing SQL: " + sql + " with parameter: " + category);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setDescription(rs.getString("description"));
                book.setCategory(rs.getString("category"));
                book.setGenre(rs.getString("genre"));
                book.setPrice(rs.getBigDecimal("price"));
                String imageUrl = rs.getString("image_url");
                // Convert example.com URLs to local image paths
                if (imageUrl != null && imageUrl.contains("example.com")) {
                    String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    imageUrl = "images/" + imageName;
                }

                // Special handling for The Great Gatsby
                if (book.getTitle().equals("The Great Gatsby")) {
                    imageUrl = "images/gatsby.jpg";
                }
                book.setImageUrl(imageUrl);
                book.setStock(rs.getInt("stock"));
                books.add(book);
                System.out.println("Found book: " + book.getTitle() + " with category: " + book.getCategory());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return books;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR category LIKE ?";
            stmt = conn.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setDescription(rs.getString("description"));
                book.setCategory(rs.getString("category"));
                book.setGenre(rs.getString("genre"));
                book.setPrice(rs.getBigDecimal("price"));
                String imageUrl = rs.getString("image_url");
                // Convert example.com URLs to local image paths
                if (imageUrl != null && imageUrl.contains("example.com")) {
                    String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    imageUrl = "images/" + imageName;
                }

                // Special handling for The Great Gatsby
                if (book.getTitle().equals("The Great Gatsby")) {
                    imageUrl = "images/gatsby.jpg";
                }
                book.setImageUrl(imageUrl);
                book.setStock(rs.getInt("stock"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return books;
    }

    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "INSERT INTO books (title, author, description, category, genre, price, image_url, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getDescription());
            stmt.setString(4, book.getCategory());
            stmt.setString(5, book.getGenre());
            stmt.setBigDecimal(6, book.getPrice());
            stmt.setString(7, book.getImageUrl());
            stmt.setInt(8, book.getStock());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "UPDATE books SET title = ?, author = ?, description = ?, category = ?, genre = ?, price = ?, image_url = ?, stock = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getDescription());
            stmt.setString(4, book.getCategory());
            stmt.setString(5, book.getGenre());
            stmt.setBigDecimal(6, book.getPrice());
            stmt.setString(7, book.getImageUrl());
            stmt.setInt(8, book.getStock());
            stmt.setInt(9, book.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteBook(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            String sql = "DELETE FROM books WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
