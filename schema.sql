-- schema.sql for Accessible Reading List (ARL)
CREATE DATABASE IF NOT EXISTS arl_db;
USE arl_db;

CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Optional: Insert sample data
INSERT INTO books (title, author, category, description) VALUES
('Clean Code', 'Robert C. Martin', 'Programming', 'A handbook of agile software craftsmanship'),
('Dead Poets Society', 'N.H. Kleinbaum', 'Coming-of-Age', 'Moral growth of the protagonist'),
('Atomic Habits', 'James Clear', 'Self-Help', 'Easy and proven ways to build good habits');