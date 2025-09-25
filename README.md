# 📑 Collaborative Editing System

A real-time collaborative document editing platform built with Spring Boot microservices, Spring Cloud Gateway, and WebSockets.
It allows multiple users to register, log in, create/edit documents collaboratively, and track version history with the ability to revert changes.
Includes frontend test pages (`ws-test.html`, `version-ws-test.html`) for verifying WebSocket communication.

---

## 🚀 Features

* **User Service** – Handles user registration and authentication.
* **Document Service** – Supports creating, editing, and managing documents, with the ability to retrieve individual documents or the complete list from the database.
* **Real-Time Collaboration** – Enables live collaborative editing, with live updates via WebSockets.
* **Version Service** – Maintains document version history and allows reverting to any previous version.
* **API Gateway** – Centralized entry point routing client requests to backend services.
* **WebSocket Test Pages** – Lightweight HTML test interfaces for WebSocket verification.

---

## 📂 Project Structure

```
collaborative-editing-system/
│
├── backend/
│   ├── api-gateway/
│   ├── user-service/
│   ├── document-service/
│   ├── version-service/
│
├── frontend/
│   └── public/
│       ├── ws-test.html
│       ├── version-ws-test.html
│       └── vite.svg
│
└── README.md
```

---

## 🛠️ Tech Stack

* **Backend:** Java 17+, Spring Boot 3.x, Spring Cloud Gateway
* **Database:** H2 (default) / MySQL (persistent)
* **Realtime:** WebSockets (SockJS + STOMP)
* **Frontend:** Static HTML test pages
* **Build Tool:** Maven
* **API Docs:** Swagger / OpenAPI
* **Testing:** JUnit

---

## 🗄️ Database Configuration (MySQL)

> **Note:** Configure MySQL before running the project.

1. Start MySQL.
2. Create databases:

```sql
CREATE DATABASE userdb;
CREATE DATABASE documentdb;
CREATE DATABASE versiondb;
```

3. Create a user:

```sql
CREATE USER 'CollabSystem'@'%' IDENTIFIED BY 'rootpassword';
GRANT ALL PRIVILEGES ON userdb.* TO 'CollabSystem'@'%';
GRANT ALL PRIVILEGES ON documentdb.* TO 'CollabSystem'@'%';
GRANT ALL PRIVILEGES ON versiondb.* TO 'CollabSystem'@'%';
FLUSH PRIVILEGES;
```

4. Update each service’s (user-service, document-service, version-service) `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<database>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=CollabSystem
spring.datasource.password=rootpassword
```

---

## ⚙️ How to Run the Project

### 1️⃣ Backend Services

Each service runs independently. The API Gateway aggregates all services at **[http://localhost:8080/](http://localhost:8080/)**.

**Default Ports:**

* API Gateway → 8080
* User Service → 8081
* Document Service → 8082
* Version Service → 8083

**Steps:**

```bash
# Navigate to service folder
cd backend/<service-name>

# Build
mvn clean install

# Run
mvn spring-boot:run
```

> After starting the API Gateway (8080), all services can be accessed via it:

* API Gateway → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
> All services indiviual URL
* User Service → [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
* Document Service → [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
* Version Service → [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) 

---

### 2️⃣ Frontend Test Pages

1. Navigate to frontend:

```bash
cd frontend
npm install
npm run dev
```

2. Open in browser:

* Document Service WebSocket → [http://localhost:5173/ws-test.html](http://localhost:5173/ws-test.html)
* Version Service WebSocket → [http://localhost:5173/version-ws-test.html](http://localhost:5173/version-ws-test.html)

---

### 3️⃣ API Endpoints

**User Service**

* `POST /api/users/register` → Register
* `POST /api/users/login` → Login
* `GET /api/users/{username}` → Get user details

**Document Service**

* `POST /api/documents` → Create document
* `GET /api/documents/{id}` → Get document
* `PUT /api/documents/{id}/update-content` → Update content
* `GET /api/documents` → Get all documents

**Version Service**

* `GET /api/versions/{documentId}` → Version history for specific documentId
* `POST /api/versions/revert/{versionId}` → Revert to a specific version

---

### ✅ Future Improvements

* JWT-based authentication
* Docker Compose for multi-service orchestration
* Full frontend client (React/Angular/Vue)

---

## 🎥 Demo Video

[▶️ Download Demo Video (1.12 GB)](https://drive.google.com/file/d/1W_ORHpFC63zpgCmLlV7RRdbpivbArXDL/view?usp=sharing)
