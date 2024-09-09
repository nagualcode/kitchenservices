CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

INSERT INTO customers (nome, email) 
VALUES ('Example Customer', 'example.customer@example.com');