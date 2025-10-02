-- Create main tables
CREATE TABLE IF NOT EXISTS countries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    continent VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS authors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    country_id BIGINT REFERENCES countries(id)
);

CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    available_copies INTEGER NOT NULL,
    rented BOOLEAN DEFAULT FALSE,
    author_id BIGINT REFERENCES authors(id),
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP
);

INSERT INTO countries (name, continent) VALUES
('USA', 'North America'),
('Canada', 'North America'),
('Mexico', 'North America'),
('Brazil', 'South America'),
('Argentina', 'South America'),
('UK', 'Europe'),
('France', 'Europe'),
('Germany', 'Europe'),
('China', 'Asia'),
('Japan', 'Asia'),
('India', 'Asia'),
('Australia', 'Oceania');

INSERT INTO authors (name, surname, country_id) VALUES
('John', 'Doe', 1),
('Jane', 'Smith', 2),
('Emily', 'Jones', 3),
('Michael', 'Brown', 4),
('Sarah', 'Davis', 5),
('David', 'Wilson', 6),
('Laura', 'Garcia', 7),
('James', 'Martinez', 8),
('Robert', 'Hernandez', 9),
('Linda', 'Lopez', 10);

-- Insert sample books with real data
INSERT INTO books (name, category, available_copies, author_id, created_at) VALUES
('The Art of Programming', 'NOVEL', 5, 1, CURRENT_TIMESTAMP),
('Digital Fortress', 'THRILLER', 3, 2, CURRENT_TIMESTAMP),
('Data Structures Explained', 'BIOGRAPHY', 7, 3, CURRENT_TIMESTAMP),
('The Cloud Atlas', 'FANTASY', 4, 4, CURRENT_TIMESTAMP),
('Machine Learning Basics', 'CLASSICS', 2, 5, CURRENT_TIMESTAMP),
('Web Development Journey', 'HISTORY', 6, 6, CURRENT_TIMESTAMP),
('Artificial Intelligence Era', 'NOVEL', 3, 7, CURRENT_TIMESTAMP),
('Software Architecture Patterns', 'BIOGRAPHY', 8, 8, CURRENT_TIMESTAMP),
('Coding Adventures', 'FANTASY', 5, 9, CURRENT_TIMESTAMP),
('Database Design Principles', 'DRAMA', 4, 10, CURRENT_TIMESTAMP);