CREATE DATABASE taskdb;
CREATE DATABASE userdb;

\c userdb;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
);

INSERT INTO users (username, password, email) VALUES ('admin', 'admin', 'admin@example.com');

\c taskdb;

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    completed VARCHAR(255),
    user_id BIGINT
);