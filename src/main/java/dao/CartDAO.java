package dao;

import model.Book;
import model.CartItem;
import java.sql.DriverManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "oracle";

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT c.*, b.* FROM cart_items c JOIN books b ON c.book_id = b.id WHERE c.user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setId(rs.getInt("c.id"));
                cartItem.setUserId(rs.getInt("c.user_id"));
                cartItem.setBookId(rs.getInt("c.book_id"));
                cartItem.setQuantity(rs.getInt("c.quantity"));

                Book book = new Book();
                book.setId(rs.getInt("b.id"));
                book.setTitle(rs.getString("b.title"));
                book.setAuthor(rs.getString("b.author"));
                book.setDescription(rs.getString("b.description"));
                book.setCategory(rs.getString("b.category"));
                book.setGenre(rs.getString("b.genre"));
                book.setPrice(rs.getBigDecimal("b.price"));
                String imageUrl = rs.getString("b.image_url");
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
                book.setStock(rs.getInt("b.stock"));

                cartItem.setBook(book);
                cartItems.add(cartItem);
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

        return cartItems;
    }

    public boolean addToCart(CartItem cartItem) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Check if the item already exists in the cart
            String checkSql = "SELECT * FROM cart_items WHERE user_id = ? AND book_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, cartItem.getUserId());
            checkStmt.setInt(2, cartItem.getBookId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update quantity if item exists
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + cartItem.getQuantity();

                String updateSql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND book_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, cartItem.getUserId());
                updateStmt.setInt(3, cartItem.getBookId());

                int rowsAffected = updateStmt.executeUpdate();
                updateStmt.close();
                return rowsAffected > 0;
            } else {
                // Insert new item if it doesn't exist
                String insertSql = "INSERT INTO cart_items (user_id, book_id, quantity) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(insertSql);
                stmt.setInt(1, cartItem.getUserId());
                stmt.setInt(2, cartItem.getBookId());
                stmt.setInt(3, cartItem.getQuantity());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
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

    public boolean updateCartItem(CartItem cartItem) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "UPDATE cart_items SET quantity = ? WHERE id = ? AND user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartItem.getQuantity());
            stmt.setInt(2, cartItem.getId());
            stmt.setInt(3, cartItem.getUserId());

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

    public boolean removeFromCart(int cartItemId, int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartItemId);
            stmt.setInt(2, userId);

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

    public boolean clearCart(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "DELETE FROM cart_items WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

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
