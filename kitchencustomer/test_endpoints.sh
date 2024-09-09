#!/bin/bash

# Definir URL base da API
BASE_URL="http://localhost:8080/customers"

# Função para criar um cliente
create_customer() {
    echo "Creating a new customer..."
    RESPONSE=$(curl -s -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"nome": "John Doe", "email": "john.doe@example.com"}')

    echo "Response: $RESPONSE"
    CUSTOMER_ID=$(echo $RESPONSE | jq -r '.id')
    echo "Customer created with ID: $CUSTOMER_ID"
}

# Função para listar todos os clientes
get_all_customers() {
    echo "Getting all customers..."
    curl -s -X GET "$BASE_URL" | jq
}

# Função para obter um cliente por ID
get_customer_by_id() {
    echo "Getting customer with ID: $1"
    curl -s -X GET "$BASE_URL/$1" | jq
}

# Função para atualizar um cliente
update_customer() {
    echo "Updating customer with ID: $1"
    curl -s -X PUT "$BASE_URL/$1" \
    -H "Content-Type: application/json" \
    -d '{"nome": "Jane Doe", "email": "jane.doe@example.com"}' | jq
}

# Função para deletar um cliente
delete_customer() {
    echo "Deleting customer with ID: $1"
    curl -s -X DELETE "$BASE_URL/$1" | jq
}

# Executar os testes
create_customer
CUSTOMER_ID=$CUSTOMER_ID
get_all_customers
get_customer_by_id $CUSTOMER_ID
update_customer $CUSTOMER_ID
delete_customer $CUSTOMER_ID
get_all_customers

