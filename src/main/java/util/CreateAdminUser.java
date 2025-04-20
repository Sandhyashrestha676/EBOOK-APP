package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAdminUser {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();

            // Check if admin user already exists
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, "admin");
            rs = checkStmt.executeQuery();

            boolean adminExists = false;
            if (rs.next()) {
                adminExists = rs.getInt(1) > 0;
            }

            if (!adminExists) {
                // Create admin user
                String insertSql = "INSERT INTO users (username, password, email, full_name, role) VALUES (?, ?, ?, ?, ?)";
                insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, "admin");
                insertStmt.setString(2, "admin123");
                insertStmt.setString(3, "admin@gmail.com");
                insertStmt.setString(4, "Admin User");
                insertStmt.setString(5, "admin");

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Admin user created successfully.");
                } else {
                    System.out.println("Failed to create admin user.");
                }
            } else {
                System.out.println("Admin user already exists.");
            }

            // Display all users
            String selectSql = "SELECT id, username, email, role FROM users";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            ResultSet usersRs = selectStmt.executeQuery();

            System.out.println("\nUsers in the database:");
            System.out.println("ID | Username | Email | Role");
            System.out.println("--------------------------------");

            while (usersRs.next()) {
                System.out.println(
                    usersRs.getInt("id") + " | " +
                    usersRs.getString("username") + " | " +
                    usersRs.getString("email") + " | " +
                    usersRs.getString("role")
                );
            }

            usersRs.close();
            selectStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
