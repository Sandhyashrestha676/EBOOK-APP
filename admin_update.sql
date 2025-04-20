-- Connect to the ebookstore database
USE ebookstore;

-- Update the admin user with the specified credentials
UPDATE users SET 
    email = 'admin@gmail.com',
    password = 'admin123'
WHERE username = 'admin';

-- Ensure all other users have the 'user' role
UPDATE users SET 
    role = 'user'
WHERE username != 'admin';

-- Display all users to verify the changes
SELECT id, username, email, role FROM users;
