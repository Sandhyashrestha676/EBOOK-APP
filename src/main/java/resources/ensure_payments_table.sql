-- Connect to the ebookjava database
USE ebookjava;

-- Create payment table if it doesn't exist
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    card_number VARCHAR(255),
    card_holder_name VARCHAR(100),
    expiry_date VARCHAR(10),
    cvv VARCHAR(10),
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    transaction_id VARCHAR(100),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Display the table structure
DESCRIBE payments;
