package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnsureSingleAdmin {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();

            // Start transaction
            conn.setAutoCommit(false);

            // Update the admin user with the specified credentials
            String updateAdminSql = "UPDATE users SET email = ?, password = ? WHERE username = ?";
            stmt = conn.prepareStatement(updateAdminSql);
            stmt.setString(1, "admin@gmail.com");
            stmt.setString(2, "admin123");
            stmt.setString(3, "admin");
            int adminUpdated = stmt.executeUpdate();
            System.out.println("Admin user updated: " + adminUpdated + " rows affected");

            // Ensure all other users have the 'user' role
            String updateOthersSql = "UPDATE users SET role = 'user' WHERE username != 'admin'";
            stmt = conn.prepareStatement(updateOthersSql);
            int othersUpdated = stmt.executeUpdate();
            System.out.println("Other users updated: " + othersUpdated + " rows affected");

            // Commit transaction
            conn.commit();

            // Display all users to verify the changes
            String selectSql = "SELECT id, username, email, role FROM users";
            stmt = conn.prepareStatement(selectSql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nUsers in the database:");
            System.out.println("ID | Username | Email | Role");
            System.out.println("--------------------------------");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("username") + " | " +
                    rs.getString("email") + " | " +
                    rs.getString("role")
                );
            }

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
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
