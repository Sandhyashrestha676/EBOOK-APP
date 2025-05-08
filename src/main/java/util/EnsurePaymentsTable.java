package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to ensure the payments table exists in the database.
 */
public class EnsurePaymentsTable {

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ebookjava";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "oracle";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Direct database connection
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Check if the payments table already exists
            rs = conn.getMetaData().getTables(null, null, "payments", null);
            if (!rs.next()) {
                // Table doesn't exist, create it
                System.out.println("Creating payments table...");
                stmt = conn.createStatement();

                String createTableSQL = "CREATE TABLE payments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id INT NOT NULL, " +
                    "payment_method VARCHAR(50) NOT NULL, " +
                    "card_number VARCHAR(255), " +
                    "card_holder_name VARCHAR(100), " +
                    "expiry_date VARCHAR(10), " +
                    "cvv VARCHAR(10), " +
                    "amount DECIMAL(10, 2) NOT NULL, " +
                    "status VARCHAR(20) NOT NULL DEFAULT 'pending', " +
                    "transaction_id VARCHAR(100), " +
                    "payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE" +
                ")";

                stmt.executeUpdate(createTableSQL);
                System.out.println("Payments table created successfully.");
            } else {
                System.out.println("Payments table already exists.");
            }

            System.out.println("Done!");

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
    }
}
