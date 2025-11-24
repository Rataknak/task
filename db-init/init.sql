CREATE DATABASE bookdb;
CREATE DATABASE userdb;

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

INSERT INTO users (username, password, email, email_verified, role) VALUES ('admin', 'admin', 'admin@example.com', TRUE, 'ADMIN');

\c bookdb;

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    completed BOOLEAN NOT NULL,
    user_id BIGINT
);