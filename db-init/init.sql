CREATE DATABASE userdb;
CREATE DATABASE taskdb;

\c userdb;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
);