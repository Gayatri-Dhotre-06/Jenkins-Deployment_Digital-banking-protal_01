# 🏦 Digital Banking Portal 

A secure backend banking application built using **Java, Spring Boot, Spring Data JPA, MySQL, Spring Security, JWT, and RESTful APIs**.

The project implements customer management, bank account management, beneficiary management, secure money transfers, transaction history, role-based authorization, and centralized exception handling following a layered backend architecture.

---

## 🚀 Features

* User registration and login
* JWT-based authentication
* Role-based authorization using `CUSTOMER` and `ADMIN` roles
* Customer profile management
* Bank account creation and management
* Account balance management
* Account ownership validation
* Beneficiary management
* Money transfer between accounts
* Beneficiary-based money transfer
* Transaction history and filtering
* Transfer amount and daily transfer limit validation
* BCrypt password encryption
* Global exception handling
* Request validation
* Swagger/OpenAPI documentation
* Docker and Docker Compose support

---

## 🛠️ Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA (Hibernate)
* MySQL
* REST APIs
* Swagger / OpenAPI
* Maven
* Docker
* Docker Compose
* Postman
* Git & GitHub

---

## 🧩 Project Architecture

The project follows a layered architecture:

### Controller Layer

Handles REST API requests and responses.

Controllers are responsible for receiving client requests, validating request data, and calling the appropriate service methods.

### Service Layer

Contains the application's business logic.

Handles operations such as customer management, account management, beneficiary management, money transfers, transaction processing, and validation.

### Repository Layer

Uses **Spring Data JPA** to interact with the MySQL database.

### Entity Layer

Contains JPA entities representing database tables such as:

* User
* Customer
* Account
* Beneficiary
* Transaction

### DTO Layer

Uses Data Transfer Objects to handle API request and response data separately from database entities.

### Security Layer

Handles:

* JWT authentication
* JWT token validation
* Role-based authorization
* BCrypt password encryption
* Protected API access

### Exception Layer

Provides centralized exception handling using a global exception handler.

---

## 🔐 Key Backend Concepts Implemented

### JWT Authentication

* User registration and login
* Password encryption using BCrypt
* JWT token generation after successful login
* JWT validation for protected APIs
* Email and role extraction from JWT
* Stateless authentication

### Role-Based Authorization

The application supports two roles:

```text
CUSTOMER
ADMIN
```

Customers can access their own profiles, accounts, beneficiaries, and transactions.

Admin-only operations are protected using Spring Security role-based authorization.

### Customer Data Isolation

* Customers can access their own profile
* Customer profile access is validated using the authenticated user's email
* Unauthorized access to another customer's data is rejected
* Protected APIs require valid authentication

### Account Ownership Validation

Account-related operations validate whether the authenticated customer owns the requested account before allowing access.

### Transaction Management

Money transfers are handled as a transactional operation.

The transfer process validates:

* Sender account
* Receiver account
* Account ownership
* Account status
* Transfer amount
* Available balance
* Same sender and receiver account
* Single transfer limit
* Daily transfer limit

After successful validation:

```text
Sender Account
      │
      │ Debit
      ▼
Transaction Processing
      │
      │ Credit
      ▼
Receiver Account
```

### Exception Handling

Centralized exception handling provides appropriate responses for situations such as:

* Customer not found
* Account not found
* Beneficiary not found
* Transaction not found
* Unauthorized access
* Invalid request
* Invalid account
* Insufficient balance
* Invalid transfer operation

---

## 🌐 REST API Endpoints

### Authentication APIs

| **Method** | **Endpoint**         | **Description**                    |
| ---------- | -------------------- | ---------------------------------- |
| POST       | `/api/auth/register` | Register a new customer            |
| POST       | `/api/auth/login`    | Authenticate user and generate JWT |

### Customer APIs

| **Method** | **Endpoint**                  | **Description**                      |
| ---------- | ----------------------------- | ------------------------------------ |
| POST       | `/api/customers`              | Create customer                      |
| GET        | `/api/customers/{id}`         | Get customer by ID                   |
| PUT        | `/api/customers/{id}`         | Update customer                      |
| DELETE     | `/api/customers/{id}`         | Delete customer                      |
| GET        | `/api/customers/me`           | Get authenticated customer's profile |
| GET        | `/api/customers/all`          | Get all customers                    |
| GET        | `/api/customers/profile/{id}` | Get customer profile                 |
| PUT        | `/api/customers/profile/{id}` | Update customer profile              |
| DELETE     | `/api/customers/profile/{id}` | Delete customer profile              |

### Account APIs

| **Method** | **Endpoint**                            | **Description**       |
| ---------- | --------------------------------------- | --------------------- |
| POST       | `/api/accounts/{customerId}`            | Create bank account   |
| GET        | `/api/accounts/customer/{customerId}`   | Get customer accounts |
| GET        | `/api/accounts/{accountNumber}/balance` | Get account balance   |
| PUT        | `/api/accounts/{accountNumber}/status`  | Update account status |

### Beneficiary APIs

| **Method** | **Endpoint**                                      | **Description**               |
| ---------- | ------------------------------------------------- | ----------------------------- |
| POST       | `/api/beneficiaries`                              | Add beneficiary               |
| GET        | `/api/beneficiaries`                              | Get beneficiaries             |
| PUT        | `/api/beneficiaries/{beneficiaryId}`              | Update beneficiary            |
| DELETE     | `/api/beneficiaries/{beneficiaryId}`              | Delete beneficiary            |
| POST       | `/api/beneficiaries/{beneficiaryId}/transfer`     | Transfer money to beneficiary |
| GET        | `/api/beneficiaries/{beneficiaryId}/transactions` | Get beneficiary transactions  |

### Transaction APIs

| **Method** | **Endpoint**                                        | **Description**                 |
| ---------- | --------------------------------------------------- | ------------------------------- |
| POST       | `/api/transactions/transfer`                        | Transfer money                  |
| GET        | `/api/transactions/{id}`                            | Get transaction by ID           |
| GET        | `/api/transactions/history`                         | Get transaction history         |
| GET        | `/api/transactions/history/account/{accountNumber}` | Get account transaction history |

---

## ⚙️ Database Design

The application uses **MySQL** as the relational database.

### Main Entities

* `users`
* `customers`
* `accounts`
* `beneficiaries`
* `transactions`

### Relationships

```text
User
 │
 │
 ▼
Customer
 │
 ├──────────► Account
 │
 └──────────► Beneficiary

Account
 │
 └──────────► Transaction
```

The application uses **JPA/Hibernate** for object-relational mapping and database operations.

---

## 📖 API Documentation

The project uses **Swagger/OpenAPI** for API documentation and testing.

Swagger provides:

* API endpoint documentation
* Request and response structures
* API testing
* JWT authorization
* HTTP response information

After starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🐳 Docker Support

The project supports containerized execution using **Docker and Docker Compose**.

The Docker environment contains:

```text
Docker Compose
      │
      ├── Spring Boot Application
      │
      └── MySQL Database
```

### Application

```text
Port: 8080
```

### MySQL

```text
Container Port: 3306
Host Port: 3307
```

A Docker volume is used for persistent MySQL database storage.

---

## ⚙️ Environment Configuration

Database credentials and JWT configuration are provided using environment variables.

Example:

```text
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET_KEY=your_jwt_secret_key
```

Sensitive `.env` configuration is excluded from Git using `.gitignore`.

A `.env.example` file is included to show the required environment variables.

---

## ▶️ How to Run the Project

### 1. Clone the Repository

```bash
git clone <repository-url>
```

### 2. Navigate to the Project

```bash
cd digital-banking-portal
```

### 3. Configure Environment Variables

Create a `.env` file using `.env.example` as a reference.

```text
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET_KEY=your_jwt_secret_key
```

### 4. Run Using Docker

```bash
docker compose up --build
```

### 5. Access the Application

```text
http://localhost:8080
```

### 6. Access Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 API Testing

The APIs were tested using:

* Postman
* Swagger UI

Typical application flow:

```text
Register Customer
       ↓
Login
       ↓
Generate JWT
       ↓
Authorize Protected APIs
       ↓
Create Bank Account
       ↓
Add Beneficiary
       ↓
Transfer Money
       ↓
View Transaction History
```

---

## 📚 Concepts Demonstrated

* Core Java
* Object-Oriented Programming
* Spring Boot
* REST API Development
* Dependency Injection
* Spring Data JPA
* Hibernate
* DTO Pattern
* Repository Pattern
* Layered Architecture
* Spring Security
* JWT Authentication
* Role-Based Authorization
* BCrypt Password Encryption
* Request Validation
* Global Exception Handling
* Transaction Management
* MySQL
* Swagger/OpenAPI
* Docker
* Maven
* Postman
* Git & GitHub

---

## 🔮 Future Enhancements

* [ ] Add unit and integration tests
* [ ] Add pagination to additional APIs
* [ ] Add email notifications for transactions
* [ ] Add transaction statement generation
* [ ] Add scheduled transactions
* [ ] Add CI/CD pipeline
* [ ] Deploy application on AWS
* [ ] Add application monitoring and logging
* [ ] Develop a frontend application

---

## 📜 License

This project is licensed under the **MIT License**.

---

## 👨‍💻 Author

**Shubham Gadhe**
