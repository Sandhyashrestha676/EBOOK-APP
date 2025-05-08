-- Add admin user if it doesn't exist
INSERT INTO users (username, password, email, full_name, role)
SELECT 'admin', 'admin123', 'admin@gmail.com', 'Admin User', 'admin'
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

-- Display users
SELECT id, username, email, role FROM users;
