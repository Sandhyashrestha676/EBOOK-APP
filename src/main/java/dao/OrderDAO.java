package dao;

import model.Book;
import model.Order;
import model.OrderItem;
import java.sql.DriverManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

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

    // Get total number of orders for pagination
    public int getTotalOrders() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT COUNT(*) FROM orders";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
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

        return count;
    }

    // Get paginated orders
    public List<Order> getOrders(int page, int ordersPerPage) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM orders ORDER BY order_date DESC LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ordersPerPage);
            stmt.setInt(2, (page - 1) * ordersPerPage);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));

                // Get first order item for display in the orders list
                List<OrderItem> firstItem = getFirstOrderItem(order.getId());
                order.setOrderItems(firstItem);

                orders.add(order);
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

        return orders;
    }

    public int createOrder(Order order, List<OrderItem> orderItems) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            conn.setAutoCommit(false);

            // Insert order
            String orderSql = "INSERT INTO orders (user_id, order_date, total_amount, status) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getStatus());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return -1;
            }

            // Get the generated order ID
            rs = stmt.getGeneratedKeys();
            int orderId;
            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }

            // Insert order items
            String itemSql = "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);

            for (OrderItem item : orderItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getBookId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setBigDecimal(4, item.getPrice());
                itemStmt.addBatch();

                // Update book stock
                String updateStockSql = "UPDATE books SET stock = stock - ? WHERE id = ?";
                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql);
                updateStockStmt.setInt(1, item.getQuantity());
                updateStockStmt.setInt(2, item.getBookId());
                updateStockStmt.executeUpdate();
                updateStockStmt.close();
            }

            int[] itemResults = itemStmt.executeBatch();
            itemStmt.close();

            // Commit transaction
            conn.commit();

            // Set the order ID in the order object
            order.setId(orderId);

            return orderId;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));

                // Get order items
                order.setOrderItems(getOrderItems(order.getId()));

                orders.add(order);
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

        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM orders ORDER BY order_date DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));

                // Get first order item for display in the orders list
                List<OrderItem> firstItem = getFirstOrderItem(order.getId());
                order.setOrderItems(firstItem);

                orders.add(order);
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

        return orders;
    }

    private List<OrderItem> getFirstOrderItem(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT oi.*, b.* FROM order_items oi JOIN books b ON oi.book_id = b.id WHERE oi.order_id = ? LIMIT 1";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("oi.id"));
                item.setOrderId(rs.getInt("oi.order_id"));
                item.setBookId(rs.getInt("oi.book_id"));
                item.setQuantity(rs.getInt("oi.quantity"));
                item.setPrice(rs.getBigDecimal("oi.price"));

                Book book = new Book();
                book.setId(rs.getInt("b.id"));
                book.setTitle(rs.getString("b.title"));
                book.setAuthor(rs.getString("b.author"));

                // Handle image URL
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
                System.out.println("Book image URL: " + imageUrl);

                item.setBook(book);
                orderItems.add(item);
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

        return orderItems;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM orders WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));

                // Get order items
                order.setOrderItems(getOrderItems(order.getId()));
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

        return order;
    }

    private List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT oi.*, b.* FROM order_items oi JOIN books b ON oi.book_id = b.id WHERE oi.order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("oi.id"));
                item.setOrderId(rs.getInt("oi.order_id"));
                item.setBookId(rs.getInt("oi.book_id"));
                item.setQuantity(rs.getInt("oi.quantity"));
                item.setPrice(rs.getBigDecimal("oi.price"));

                Book book = new Book();
                book.setId(rs.getInt("b.id"));
                book.setTitle(rs.getString("b.title"));
                book.setAuthor(rs.getString("b.author"));

                // Handle image URL
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
                System.out.println("Book image URL in getOrderItems: " + imageUrl);

                item.setBook(book);
                orderItems.add(item);
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

        return orderItems;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "UPDATE orders SET status = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, orderId);

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

    public int countOrdersByUser(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
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

        return count;
    }

    public boolean deleteOrder(int orderId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            conn.setAutoCommit(false);

            // First delete order items
            String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
            stmt = conn.prepareStatement(deleteItemsSql);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            stmt.close();

            // Then delete the order
            String deleteOrderSql = "DELETE FROM orders WHERE id = ?";
            stmt = conn.prepareStatement(deleteOrderSql);
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();

            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
