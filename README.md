# Payment Service
A Spring Boot service for creating, retrieving, and cancelling payments.
# Requirements
- Java 17+
- Docker installed and running
# Starting application
Start everything with pulling the code from repository:
```
git clone https://github.com/paulius-kibilda/payments.git
```
To start application:
```
docker compose up --build
```
This will:
- Build the backend application (Maven inside Docker)
- Start the Spring Boot service on port 8080
- Start PostgreSQL 16 on port 5432
To stop everything:
```
docker compose down
```
# Application URLs
- API root:
> http://localhost:8080
- Swagger UI:
> http://localhost:8080/swagger-ui.html
# API Endpoints
## 1. Create Payment

POST /payments

Example:
```
{
  "type": "TYPE1",
  "amount": 100,
  "currency": "EUR",
  "debtorIban": "LT123456",
  "creditorIban": "LT789123",
  "details": "Invoice"
}
```
## 2. Get All Payment IDs
GET /payments
## 3. Get Payment by ID
GET /payments/{id}
## 4. Cancel Payment
POST /payments/{id}/cancel
