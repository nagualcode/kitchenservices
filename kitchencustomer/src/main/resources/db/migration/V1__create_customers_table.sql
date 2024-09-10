
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    orders_id INTEGER[] DEFAULT ARRAY[]::INTEGER[] 
);


INSERT INTO customers (nome, email) VALUES ('Cliente Teste', 'teste@example.com');


CREATE TABLE customer_orders (
    order_id SERIAL PRIMARY KEY,
    customer_id INTEGER,
    valor DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'aguardando pagamento',
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
