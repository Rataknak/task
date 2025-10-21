CREATE DATABASE bookdb;
CREATE DATABASE userdb;

\c userdb;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
);

INSERT INTO users (username, password, email) VALUES ('admin', 'admin', 'admin@example.com');

\c bookdb;

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    completed BOOLEAN NOT NULL,
    user_id BIGINT
);