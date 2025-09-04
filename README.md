# 📚 Online Book Store App

A **Spring Boot** application for managing an online bookstore with **JWT authentication** and a **MySQL database**.  

---

## 🗂 Table of Contents
- [About the Project](#-about-the-project)  
- [Technologies](#-technologies)  
- [Getting Started](#-getting-started)  
  - [Prerequisites](#-prerequisites)  
  - [Environment Configuration](#-environment-configuration)  
  - [Database Setup](#-database-setup)  
  - [Running with Docker](#-running-with-docker)  
  - [Running Locally](#-running-locally)  
- [Swagger UI](#-swagger-ui)  
- [Liquibase](#-liquibase)  
- [Contributing](#-contributing)  
- [License](#-license)  

---

## ℹ️ About the Project

This is a **demo project** of an online bookstore built with **Spring Boot**.  

**Features:**  
- 🔒 **Spring Security** with JWT authentication  
- 🌐 **REST API**  
- 🗄 **MySQL database** integration  
- 🔄 **Database migrations** using Liquibase  
- 📖 **API documentation** with Swagger/OpenAPI  

---

## 🛠 Technologies

- **Java 17**  
- **Spring Boot**  
- **Spring Security**  
- **Spring Data JPA**  
- **MySQL**  
- **Liquibase**  
- **Docker**  
- **Swagger/OpenAPI**  

---

## 🚀 Getting Started

### 🧩 Prerequisites
- **Docker** and **Docker Compose**  
- **Java 17**  
- **MySQL** (optional if using Docker)  

### ⚙️ Environment Configuration

The **`.env`** file is located in the project root. Fill it with your **local configuration** before running the application.  

Example `.env`:

```env
# MySQL
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=book_store_app

# Spring Boot
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/book_store_app?serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

# JWT
JWT_SECRET=your-secret-key
JWT_EXPIRATION=30000000
```
> Each developer should **adapt values** to their local setup.  
> The project does **not include any pre-configured passwords** or credentials.  



## 🗄 Database Setup

If using **Docker**, the database will be created automatically.  

Before calling any API endpoints (except `/auth/register` and `/auth/login`), manually assign **roles** to users in the `users_roles` table.  

**Roles:**  
- 1 → ROLE_USER  
- 2 → ROLE_ADMIN  

**Example SQL:**  
```sql
-- Assign ROLE_USER to user with ID 1
INSERT INTO users_roles(user_id, role_id) VALUES (1, 1);

-- Assign ROLE_ADMIN to user with ID 2
INSERT INTO users_roles(user_id, role_id) VALUES (2, 2);
> This is required for users to access **protected endpoints** after registration or login.

```

## 🐳 Running with Docker

```bash
docker-compose up --build
```
- Application runs at: [http://localhost:8081](http://localhost:8081)  
- MySQL container runs on port 3306 (or as configured in `docker-compose.yml`)  

⚠️ Default ports in `docker-compose.yml`:
- **MySQL:** 3307:3306 (host:container)  
- **Application:** 8081:8080 (host:container)  

> Change these if they conflict with existing services.

## 💻 Running Locally

Make sure **MySQL** is running and `.env` is configured.  

```bash
./mvnw clean package
java -jar target/bookstore-0.0.1-SNAPSHOT.jar
```
## 📑 Swagger UI

Available at: [http://localhost:8081/api/swagger-ui.html](http://localhost:8081/api/swagger-ui.html)  

To use **protected endpoints**, click **Authorize** and enter your **JWT token**.  

⚠️ Swagger automatically adds the `Bearer` prefix, so only the token is required.  
In other setups, you may need:  

Bearer <YOUR_TOKEN>


> Copy the token in a **single line** without spaces or line breaks.

## 🔄 Liquibase

Database migrations are managed using **Liquibase**.  

- Change log files: `classpath:db/changelog/db.changelog-master.yaml`  
- Migrations run **automatically** on application startup.

## 🤝 Contributing

1. **Fork** the repository  
2. Make changes in a **separate branch**  
3. Create a **Pull Request**

## 📝 License

This project is licensed under the **MIT License**.
