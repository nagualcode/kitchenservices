# kitchenservices
Sistema para gestão de pedidos , projetado em microservicos


docker compose down --volumes

docker compose up --build --no-cache --progress=plain . 2>&1 | tee build.log

docker exec -it customer-postgres psql -U postgres -d customer_db


\d customers




curl -X POST http://localhost:8080/customers \
-H "Content-Type: application/json" \
-d '{"nome": "John Doe", "email": "john.doe@example.com"}'

curl -X GET http://localhost:8080/customers | jq .

curl -X POST http://localhost:8081/orders -H "Content-Type: application/json" -d '{"customerId": 1, "valor": 99.99}'

