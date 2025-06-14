# 🔌 NextGadget

**NextGadget** is a modern, microservices-based e-commerce platform focused on selling electronics and gadgets. It demonstrates a scalable architecture using Spring Boot, React, Kafka, MongoDB, Node.js, and PostgreSQL — built for performance, flexibility, and real-time communication.

---

## 🚀 Features

- 🔍 Browse and search electronic products
- 🛒 Add items to cart and place orders
- 📦 Real-time order tracking via Kafka events
- 🔔 Notification service for order updates
- 🧑‍💼 Admin functionality (manage products, users)
- 🧱 Microservices architecture with API Gateway

---

## 🧱 Tech Stack

### 🖥️ Frontend
- **React.js** – UI for product browsing and ordering

### ⚙️ Backend Microservices
| Service | Language | Description |
|---------|----------|-------------|
| `product-service` | Spring Boot | Manages products and inventory |
| `user-service` | Spring Boot | Handles user registration/auth |
| `order-service` | Spring Boot + Kafka | Processes orders and emits events |
| `notification-service` | Node.js + MongoDB | Listens to Kafka for order events, sends notifications |
| `api-gateway` | Spring Cloud Gateway | Centralized API access to all services |

### 🔄 Communication
- **REST APIs** – Sync communication between frontend and services
- **Apache Kafka** – Async event-based communication between services

### 🗃️ Databases
- **PostgreSQL** – Relational data for users, products, and orders
- **MongoDB** – NoSQL store for notifications/logs

### 🐳 DevOps
- **Docker & Docker Compose** – Local containerized setup
- *(Optional)* **JWT Auth / Keycloak** – Secure user access control

---

## 🗂️ Folder Structure

```
nextgadget/
├── frontend/                 # React UI
├── gateway/                  # API Gateway (Spring Cloud)
├── product/                  # Product management
├── user/                     # User registration and login
├── order/                    # Order placement and tracking
├── notification-service/     # Notification microservice
├── eureka/                   # Service Registry
├── docker-compose.yml        # Local dev orchestration
└── README.md
```

---

## 🔄 Microservices Architecture (Simplified)

```text
Frontend (React)
     ↓
API Gateway (Spring Cloud)
     ↓
 ┌──────────────┬──────────────┬──────────────┐
 │ User Service │ Product Svc  │ Order Svc    │
 └──────────────┴──────────────┴──────────────┘
                        ↓
                 [Kafka Broker]
                        ↓
             Notification Service (Node.js + MongoDB)
```

---

## 🧪 Future Enhancements
- Role-based access control (admin vs customer)
- Payment gateway integration (Stripe, Razorpay)
- Order analytics dashboard
- CI/CD pipelines (GitHub Actions)

---

## 📦 Getting Started (Local Dev)

> Make sure Docker & Docker Compose are installed.

```bash
# Clone the repo
git clone https://github.com/your-username/nextgadget.git
cd nextgadget

# Run all services
docker-compose up --build
```

Frontend should be available at: `http://localhost:3000`  
Backend gateway at: `http://localhost:8080`

---

## 👨‍💻 Author

Built by Adam Abdulkadir
📧 [adama1862@outlook.com]  
💼 Infosys | Full-Stack Developer

---

