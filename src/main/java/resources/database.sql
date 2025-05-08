-- Create database
CREATE DATABASE IF NOT EXISTS ebookjava;
USE ebookstore;

-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    genre VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Insert sample data
-- Admin user (password: admin123)
INSERT INTO users (username, password, email, full_name, role) VALUES
('admin', 'admin123', 'admin@ebookstore.com', 'Admin User', 'admin');

-- Regular users (password: user123)
INSERT INTO users (username, password, email, full_name, role) VALUES
('john', 'user123', 'john@example.com', 'John Doe', 'user'),
('jane', 'user123', 'jane@example.com', 'Jane Smith', 'user');

-- Sample books
INSERT INTO books (title, author, description, category, genre, price, image_url, stock) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 'A novel about the mysterious Jay Gatsby and his love for Daisy Buchanan.', 'Fiction', 'Classic', 9.99, 'https://example.com/images/gatsby.jpg', 100),
('To Kill a Mockingbird', 'Harper Lee', 'A novel about racial inequality through the eyes of a young girl in Alabama.', 'Fiction', 'Classic', 8.99, 'https://example.com/images/mockingbird.jpg', 75),
('1984', 'George Orwell', 'A dystopian novel set in a totalitarian society.', 'Fiction', 'Dystopian', 7.99, 'https://example.com/images/1984.jpg', 50),
('The Hobbit', 'J.R.R. Tolkien', 'A fantasy novel about the adventures of Bilbo Baggins.', 'Fiction', 'Fantasy', 12.99, 'https://example.com/images/hobbit.jpg', 60),
('A Brief History of Time', 'Stephen Hawking', 'A book about the nature of time and the universe.', 'Non-Fiction', 'Science', 14.99, 'https://example.com/images/time.jpg', 40),
('Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', 'A book about the history of human evolution.', 'Non-Fiction', 'History', 15.99, 'https://example.com/images/sapiens.jpg', 30),
('Clean Code', 'Robert C. Martin', 'A handbook of agile software craftsmanship.', 'Technology', 'Programming', 29.99, 'https://example.com/images/cleancode.jpg', 25),
('The Art of Computer Programming', 'Donald Knuth', 'A comprehensive monograph on computer programming.', 'Technology', 'Programming', 49.99, 'https://example.com/images/knuth.jpg', 15);
