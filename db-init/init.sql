CREATE DATABASE bookdb;
CREATE DATABASE userdb;

-- ============================================
-- USER DATABASE
-- ============================================
\c userdb;

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20),
                       email_verified BOOLEAN DEFAULT FALSE,
                       verification_code VARCHAR(255),
                       code_expiry TIMESTAMP,
                       role VARCHAR(50) NOT NULL DEFAULT 'USER',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, email, email_verified, role)
VALUES ('admin', 'admin', 'admin@example.com', TRUE, 'ADMIN');

-- ============================================
-- BOOK DATABASE
-- ============================================
\c bookdb;

-- Categories table
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) UNIQUE NOT NULL,
                            description VARCHAR(255),
                            icon_url VARCHAR(500),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Books table (EXPANDED)
CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255),
                       description TEXT,
                       cover_url VARCHAR(500),
                       file_url VARCHAR(500),
                       category_id BIGINT REFERENCES categories(id),
                       page_count INTEGER DEFAULT 0,
                       summary TEXT,
                       isbn VARCHAR(20),
                       published_year INTEGER,
                       download_count INTEGER DEFAULT 0,
                       rating DECIMAL(2,1) DEFAULT 0.0,
                       is_featured BOOLEAN DEFAULT FALSE,
                       user_id BIGINT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reading sessions table (for IoT)
CREATE TABLE reading_sessions (
                                  id BIGSERIAL PRIMARY KEY,
                                  user_id BIGINT NOT NULL,
                                  book_id BIGINT REFERENCES books(id),
                                  pages_read INTEGER DEFAULT 0,
                                  current_page INTEGER DEFAULT 0,
                                  time_spent_minutes INTEGER DEFAULT 0,
                                  session_start TIMESTAMP,
                                  session_end TIMESTAMP,
                                  device_id VARCHAR(255),
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Download records table (for Blockchain reference)
CREATE TABLE download_records (
                                  id BIGSERIAL PRIMARY KEY,
                                  user_id BIGINT NOT NULL,
                                  book_id BIGINT REFERENCES books(id),
                                  transaction_hash VARCHAR(100),
                                  blockchain_status VARCHAR(50) DEFAULT 'PENDING',
                                  downloaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User favorites table
CREATE TABLE user_favorites (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                book_id BIGINT REFERENCES books(id),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                UNIQUE(user_id, book_id)
);

-- Insert sample categories
INSERT INTO categories (name, description) VALUES
                                               ('Fiction', 'Fictional stories and novels'),
                                               ('Technology', 'Programming, IT, and tech books'),
                                               ('Self-Help', 'Personal development and motivation'),
                                               ('Business', 'Business and entrepreneurship'),
                                               ('Science', 'Scientific and educational books'),
                                               ('History', 'Historical books and biographies');

-- Insert sample books
INSERT INTO books (title, author, description, category_id, page_count, published_year, is_featured) VALUES
                                                                                                         ('The Alchemist', 'Paulo Coelho', 'A story about following your dreams', 1, 208, 1988, TRUE),
                                                                                                         ('Rich Dad Poor Dad', 'Robert Kiyosaki', 'Financial education and wealth building', 4, 336, 1997, TRUE),
                                                                                                         ('Atomic Habits', 'James Clear', 'Tiny changes, remarkable results', 3, 320, 2018, TRUE),
                                                                                                         ('Clean Code', 'Robert C. Martin', 'A handbook of agile software craftsmanship', 2, 464, 2008, FALSE),
                                                                                                         ('Ikigai', 'Héctor García', 'The Japanese secret to a long and happy life', 3, 208, 2016, TRUE);
