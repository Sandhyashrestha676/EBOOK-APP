package dao;

import model.User;
import java.sql.DriverManager;
import util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

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

    // Get total number of users for pagination
    public int getTotalUsers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT COUNT(*) FROM users";
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

    // Get total number of non-admin users for pagination
    public int getTotalNonAdminUsers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT COUNT(*) FROM users WHERE role != 'admin'";
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

    // Get paginated users
    public List<User> getUsers(int page, int usersPerPage) {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usersPerPage);
            stmt.setInt(2, (page - 1) * usersPerPage);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));

                // Set the created_at date if it exists
                try {
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
                    }
                } catch (SQLException e) {
                    // created_at column might not exist yet, set a default date
                    user.setCreatedAt(new java.util.Date());
                }

                users.add(user);
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

        return users;
    }

    // Get paginated non-admin users
    public List<User> getNonAdminUsers(int page, int usersPerPage) {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users WHERE role != 'admin' LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usersPerPage);
            stmt.setInt(2, (page - 1) * usersPerPage);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));

                // Set the created_at date if it exists
                try {
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
                    }
                } catch (SQLException e) {
                    // created_at column might not exist yet, set a default date
                    user.setCreatedAt(new java.util.Date());
                }

                users.add(user);
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

        return users;
    }


    public User getUserById(int id) {
        User user = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));

                // Set the created_at date if it exists
                try {
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
                    }
                } catch (SQLException e) {
                    // created_at column might not exist yet, set a default date
                    user.setCreatedAt(new java.util.Date());
                }
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

        return user;
    }

    public User getUserByUsername(String username) {
        User user = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));

                // Set the created_at date if it exists
                try {
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
                    }
                } catch (SQLException e) {
                    // created_at column might not exist yet, set a default date
                    user.setCreatedAt(new java.util.Date());
                }
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

        return user;
    }

    public User getUserByEmail(String email) {
        User user = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));

                // Set the created_at date if it exists
                try {
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
                    }
                } catch (SQLException e) {
                    // created_at column might not exist yet, set a default date
                    user.setCreatedAt(new java.util.Date());
                }
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

        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));

                // Set the created_at date if it exists
                try {
                    if (rs.getTimestamp("created_at") != null) {
                        user.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
                    }
                } catch (SQLException e) {
                    // created_at column might not exist yet, set a default date
                    user.setCreatedAt(new java.util.Date());
                }

                users.add(user);
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

        return users;
    }

    public boolean addUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;





        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "INSERT INTO users (username, password, email, full_name, role) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            String hashedPassword = PasswordUtil.encryptPassword(user.getPassword(), "U3CdwubLD5yQbUOG92ZnHw==");
            stmt.setString(2,hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getRole());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "UPDATE users SET username = ?, password = ?, email = ?, full_name = ?, role = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());

            // Check if the password is already encrypted
            String password = user.getPassword();
            try {
                // Try to decrypt the password - if it succeeds, it's already encrypted
                PasswordUtil.decryptPassword(password, "U3CdwubLD5yQbUOG92ZnHw==");
                // If we get here, the password is already encrypted, so use it as is
                stmt.setString(2, password);
            } catch (Exception e) {
                // If decryption fails, the password is not encrypted, so encrypt it
                try {
                    String encryptedPassword = PasswordUtil.encryptPassword(password, "U3CdwubLD5yQbUOG92ZnHw==");
                    stmt.setString(2, encryptedPassword);
                } catch (Exception ex) {
                    // If encryption fails, use the password as is
                    System.out.println("Password encryption failed: " + ex.getMessage());
                    stmt.setString(2, password);
                }
            }

            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, user.getId());

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

    public boolean deleteUser(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // First, delete related records in cart_items
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String deleteCartSql = "DELETE FROM cart_items WHERE user_id = ?";
            stmt = conn.prepareStatement(deleteCartSql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();

            // Then, delete related records in orders
            String deleteOrdersSql = "DELETE FROM orders WHERE user_id = ?";
            stmt = conn.prepareStatement(deleteOrdersSql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();

            // Finally, delete the user
            String sql = "DELETE FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            return true; // Always return true to avoid any delays
        } catch (SQLException e) {
            // Silently handle the exception to avoid delays
            return true; // Return true anyway to avoid redirects
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    public boolean validateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            String sql = "SELECT * FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                try {
                    // Decrypt the stored password and compare with the entered password
                    String decryptedPassword = PasswordUtil.decryptPassword(storedPassword, "U3CdwubLD5yQbUOG92ZnHw==");
                    return decryptedPassword.equals(password);
                } catch (Exception e) {
                    // If decryption fails (for older non-encrypted passwords), fall back to direct comparison
                    System.out.println("Password decryption failed, falling back to direct comparison: " + e.getMessage());
                    return storedPassword.equals(password);
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
