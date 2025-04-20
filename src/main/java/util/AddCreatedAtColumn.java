package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Utility class to add a created_at column to the users table
 * and set default values for existing users.
 */
public class AddCreatedAtColumn {
    
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement updateStmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            
            // Check if the column already exists
            rs = conn.getMetaData().getColumns(null, null, "users", "created_at");
            if (!rs.next()) {
                // Column doesn't exist, add it
                System.out.println("Adding created_at column to users table...");
                stmt = conn.createStatement();
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                System.out.println("Column added successfully.");
                
                // Update existing users with current timestamp
                System.out.println("Updating existing users with current timestamp...");
                Timestamp now = new Timestamp(new Date().getTime());
                updateStmt = conn.prepareStatement("UPDATE users SET created_at = ? WHERE created_at IS NULL");
                updateStmt.setTimestamp(1, now);
                int rowsUpdated = updateStmt.executeUpdate();
                System.out.println(rowsUpdated + " users updated with current timestamp.");
            } else {
                System.out.println("created_at column already exists in users table.");
            }
            
            System.out.println("Done!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (updateStmt != null) updateStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
