package dao;

import model.Payment;
import java.sql.DriverManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean createPayment(Payment payment) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Direct database connection to avoid version issues
            String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
            String JDBC_USER = "root";
            String JDBC_PASSWORD = "oracle";
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "INSERT INTO payments (order_id, payment_method, card_number, card_holder_name, expiry_date, cvv, amount, status, transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, payment.getOrderId());
            stmt.setString(2, payment.getPaymentMethod());

            // Mask the card number for security (store only last 4 digits)
            String maskedCardNumber = maskCardNumber(payment.getCardNumber());
            stmt.setString(3, maskedCardNumber);

            stmt.setString(4, payment.getCardHolderName());
            stmt.setString(5, payment.getExpiryDate());

            // Mask the CVV for security
            stmt.setString(6, "***");

            stmt.setBigDecimal(7, payment.getAmount());
            stmt.setString(8, payment.getStatus());
            stmt.setString(9, payment.getTransactionId());

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

    // Helper method to mask card number
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        // Keep only the last 4 digits
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        return "************" + lastFourDigits;
    }

    public Payment getPaymentByOrderId(int orderId) {
        Payment payment = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Direct database connection to avoid version issues
            String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
            String JDBC_USER = "root";
            String JDBC_PASSWORD = "oracle";
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM payments WHERE order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setCardNumber(rs.getString("card_number"));
                payment.setCardHolderName(rs.getString("card_holder_name"));
                payment.setExpiryDate(rs.getString("expiry_date"));
                payment.setCvv(rs.getString("cvv"));
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setStatus(rs.getString("status"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
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

        return payment;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Direct database connection to avoid version issues
            String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
            String JDBC_USER = "root";
            String JDBC_PASSWORD = "oracle";
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM payments ORDER BY payment_date DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setCardNumber(rs.getString("card_number"));
                payment.setCardHolderName(rs.getString("card_holder_name"));
                payment.setExpiryDate(rs.getString("expiry_date"));
                payment.setCvv(rs.getString("cvv"));
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setStatus(rs.getString("status"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));

                payments.add(payment);
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

        return payments;
    }
}
