# Requirements Document

## Introduction

NextGadget is a microservices-based e-commerce platform for electronics and gadgets. The project currently has a partially implemented backend (Eureka, Gateway, User, Product, Order services on Spring Boot 3.5.0) but is missing a frontend, a notification service, database schema scripts, has outdated dependencies, deprecated API usage, incomplete features, no input validation, no global error handling, and no Docker orchestration. This modernization effort aims to update all libraries, fix design issues, complete unfinished functionality, build the missing React frontend, add a Node.js notification microservice that consumes Kafka events and stores notifications in MongoDB, create database schema and seed scripts (with imgur-hosted product images), provide a Postman collection for API testing, and add Kafka configuration scripts.

## Glossary

- **Gateway**: The Spring Cloud Gateway service that routes requests to downstream microservices and enforces JWT authentication
- **User_Service**: The Spring Boot microservice responsible for user registration, login, and JWT token generation
- **Product_Service**: The Spring Boot microservice responsible for product CRUD operations and search
- **Order_Service**: The Spring Boot microservice responsible for order placement and retrieval
- **Eureka_Server**: The Netflix Eureka service registry for microservice discovery
- **Frontend**: The React (Vite + TypeScript) single-page application for the customer-facing storefront
- **JWT**: JSON Web Token used for stateless authentication across services
- **JJWT**: The Java JWT library (io.jsonwebtoken) used for token creation and validation
- **Product_Catalog**: The collection of electronics products available for browsing and purchase
- **Shopping_Cart**: Client-side state that tracks products a user intends to purchase
- **Order**: A confirmed purchase containing one or more Order_Items tied to a user
- **Order_Item**: A single line item within an Order, referencing a product, quantity, and unit price
- **Kafka**: Apache Kafka message broker used for asynchronous event-driven communication between services
- **Docker_Compose**: The container orchestration tool used to run all services locally
- **Notification_Service**: A Node.js microservice that consumes Kafka order events and stores notifications in MongoDB
- **MongoDB**: NoSQL document database used by the Notification_Service for notification storage
- **Postman_Collection**: A JSON collection file containing all API endpoint definitions for testing via Postman
- **Database_Scripts**: SQL schema files, MongoDB schema validators, and seed data scripts for initializing databases
- **Kafka_Config**: Configuration directory containing Kafka topic definitions and setup scripts
- **Notification**: A MongoDB document representing a user-facing message about an order event, with fields for userId, message, status (unread/read), type, and timestamp

## Requirements

### Requirement 1: Update Backend Dependencies to Latest Stable Versions

**User Story:** As a developer, I want all backend services to use the latest stable library versions, so that the project benefits from security patches, bug fixes, and modern API support.

#### Acceptance Criteria

1. THE User_Service SHALL use JJWT version 0.12.x or later for JWT token operations
2. THE Gateway SHALL use JJWT version 0.12.x or later for JWT token validation
3. THE User_Service SHALL use non-deprecated Spring Security API methods for HTTP security configuration
4. THE Gateway SHALL use non-deprecated Spring Security WebFlux API methods for reactive security configuration
5. THE Product_Service SHALL use `spring.jpa.properties.hibernate.dialect` only when required, removing the deprecated `database-platform` property
6. THE Order_Service SHALL remove the duplicate `application.properties` file and consolidate configuration into `application.yml`

### Requirement 2: Fix Gateway Security and Routing Design

**User Story:** As a developer, I want the API Gateway to have a single, consistent security filter chain without duplicate JWT processing, so that authentication is reliable and maintainable.

#### Acceptance Criteria

1. THE Gateway SHALL use a single JWT authentication mechanism, eliminating the duplicate filter in `SecurityConfig` and `JwtAuthenticationFilter`
2. THE Gateway SHALL forward the authenticated user's ID and roles as HTTP headers (`X-User-Id`, `X-User-Roles`) to downstream services
3. WHEN a request targets a public endpoint (`/api/users/register`, `/api/users/login`, `GET /api/products/**`, `GET /api/notifications/**`), THE Gateway SHALL allow the request without requiring a JWT token
4. WHEN a request targets an admin endpoint (`POST /api/products/**`, `PUT /api/products/**`, `DELETE /api/products/**`), THE Gateway SHALL require the `ROLE_ADMIN` authority
5. WHEN a request contains an invalid or expired JWT token, THE Gateway SHALL return HTTP 401 Unauthorized
6. THE Gateway SHALL share the same JWT secret configuration as the User_Service
7. THE Gateway SHALL include a route for the Notification_Service using a static URI (`http://notification:8084`) with path predicate `/api/notifications/**`

### Requirement 3: Add Input Validation to All Services

**User Story:** As a developer, I want all API endpoints to validate incoming request data, so that invalid data is rejected before processing.

#### Acceptance Criteria

1. THE User_Service SHALL validate that the `username`, `email`, and `password` fields are non-blank during registration
2. THE User_Service SHALL validate that the `email` field contains a valid email format during registration
3. THE Product_Service SHALL validate that `name` is non-blank and `price` is a positive number when creating a product
4. THE Order_Service SHALL validate that the order request contains at least one Order_Item with a positive quantity
5. WHEN validation fails on any service, THE service SHALL return HTTP 400 Bad Request with a descriptive error message

### Requirement 4: Add Global Error Handling to All Services

**User Story:** As a developer, I want consistent error responses across all microservices, so that the frontend can reliably parse and display errors.

#### Acceptance Criteria

1. THE User_Service SHALL return a structured JSON error response containing `status`, `message`, and `timestamp` fields for all error conditions
2. THE Product_Service SHALL return a structured JSON error response containing `status`, `message`, and `timestamp` fields for all error conditions
3. THE Order_Service SHALL return a structured JSON error response containing `status`, `message`, and `timestamp` fields for all error conditions
4. WHEN a requested resource is not found, THE service SHALL return HTTP 404 Not Found instead of throwing an unhandled RuntimeException
5. WHEN an unexpected server error occurs, THE service SHALL return HTTP 500 Internal Server Error with a generic message that does not expose internal details

### Requirement 5: Complete the Order Service with User Order History

**User Story:** As a customer, I want to view my past orders, so that I can track my purchase history.

#### Acceptance Criteria

1. WHEN a user requests their order history, THE Order_Service SHALL return all orders belonging to that user sorted by creation date descending
2. THE Order_Service SHALL expose a `GET /api/orders/user/{userId}` endpoint for retrieving orders by user ID
3. THE Order_Service SHALL include the list of Order_Items with each order in the response
4. WHEN a user has no orders, THE Order_Service SHALL return an empty list with HTTP 200 OK

### Requirement 6: Prevent Circular JSON Serialization in Order Entities

**User Story:** As a developer, I want Order and OrderItem entities to serialize correctly to JSON, so that API responses do not fail with infinite recursion errors.

#### Acceptance Criteria

1. THE Order_Service SHALL annotate the Order-to-OrderItem relationship to prevent circular JSON serialization
2. THE Order_Service SHALL use `@JsonManagedReference` on the Order's items collection and `@JsonBackReference` on the OrderItem's order reference (or equivalent mechanism)
3. WHEN an Order is serialized to JSON, THE Order_Service SHALL include the Order_Items without causing a stack overflow

### Requirement 7: Build the React Frontend Application

**User Story:** As a customer, I want a web-based storefront to browse products, manage my cart, and place orders, so that I can shop for electronics online.

#### Acceptance Criteria

1. THE Frontend SHALL be built with React 19, TypeScript, and Vite
2. THE Frontend SHALL provide a home page displaying featured products from the Product_Catalog
3. THE Frontend SHALL provide a product listing page with category filtering and search by name
4. THE Frontend SHALL provide a product detail page showing name, category, price, stock status, and image
5. THE Frontend SHALL provide a Shopping_Cart page where users can view, update quantities, and remove items
6. THE Frontend SHALL provide user registration and login pages that authenticate via the User_Service through the Gateway
7. THE Frontend SHALL store the JWT token in localStorage after successful login and include it in the `Authorization` header for authenticated API requests
8. THE Frontend SHALL provide a checkout flow that creates an Order via the Order_Service through the Gateway
9. THE Frontend SHALL provide an order history page displaying past orders for the logged-in user
10. THE Frontend SHALL use React Router for client-side navigation
11. THE Frontend SHALL display product images using imgur-hosted URLs as defined in the Product_Catalog seed data

### Requirement 8: Frontend Responsive Design, Styling, and Accessibility

**User Story:** As a customer, I want the storefront to look modern and professional, work well on mobile and desktop devices, and be accessible, so that all users can shop comfortably.

#### Acceptance Criteria

1. THE Frontend SHALL use a responsive CSS layout that adapts to screen widths from 320px to 1920px
2. THE Frontend SHALL provide a navigation bar with links to Home, Products, Cart, and Login/Account
3. THE Frontend SHALL display product images with descriptive `alt` text attributes
4. THE Frontend SHALL use semantic HTML elements (`nav`, `main`, `header`, `footer`, `button`) for screen reader compatibility
5. THE Frontend SHALL indicate loading states while API requests are in progress
6. THE Frontend SHALL use Bootstrap 5 as the CSS framework, modernized from the Bootstrap 4 reference design
7. THE Frontend SHALL use a dark navbar with the NextGadget logo/brand name
8. THE Frontend SHALL use a green accent color (#77f2a1) for feature sections, buttons, and highlights
9. THE Frontend SHALL use the Roboto font family imported from Google Fonts
10. THE Frontend SHALL apply hover scale effects on product card images for interactive feedback
11. THE Frontend SHALL use a responsive flex container layout for product grids
12. THE Frontend SHALL include a clean footer with site information
13. THE Frontend SHALL style form inputs with rounded borders and consistent padding

### Requirement 9: Add Docker Compose Orchestration

**User Story:** As a developer, I want to start the entire platform with a single command, so that local development setup is fast and reproducible.

#### Acceptance Criteria

1. THE Docker_Compose configuration SHALL define services for PostgreSQL, MongoDB, Eureka_Server, Gateway, User_Service, Product_Service, Order_Service, Notification_Service, and Frontend
2. THE Docker_Compose configuration SHALL define a Kafka broker and Zookeeper for event-driven communication
3. THE Docker_Compose configuration SHALL configure health checks so that dependent services wait for their dependencies to be ready
4. THE Docker_Compose configuration SHALL expose the Gateway on port 8080 and the Frontend on port 3000
5. EACH backend Java service SHALL have a Dockerfile that builds a runnable container image
6. THE Notification_Service SHALL have a Dockerfile that builds a runnable Node.js container image
7. THE Frontend SHALL have a Dockerfile that builds a production bundle and serves it via Nginx
8. THE Docker_Compose configuration SHALL configure MongoDB with a volume for persistent notification data

### Requirement 10: Add Kafka Event Publishing for Order Placement

**User Story:** As a developer, I want the Order Service to publish an event when an order is placed, so that other services (including the Notification_Service) can react to order events asynchronously.

#### Acceptance Criteria

1. WHEN an order is successfully placed, THE Order_Service SHALL publish an `order-placed` event to a Kafka topic named `order-events`
2. THE `order-placed` event SHALL contain the order ID, user ID, total amount, and timestamp
3. IF Kafka is unavailable when an order is placed, THEN THE Order_Service SHALL log the failure and still persist the order to the database
4. THE Order_Service SHALL use Spring Kafka's `KafkaTemplate` for publishing events
5. THE project SHALL provide Kafka topic configuration scripts in a `kafka-config/` directory

### Requirement 11: Fix Typos and File Organization Issues

**User Story:** As a developer, I want the project to have correct naming and clean file organization, so that the codebase is professional and easy to navigate.

#### Acceptance Criteria

1. THE project SHALL rename the `imgaes/` directory to `images/`
2. THE project SHALL rename `deafult-img.png` to `default-img.png`
3. THE Product_Service SHALL reference the corrected image paths in default image URL configuration
4. THE User_Service SHALL use the `CustomUserDetails` role field to populate granted authorities instead of returning an empty authority list

### Requirement 12: Build the Notification Service

**User Story:** As a customer, I want to receive notifications when my orders are placed, so that I have confirmation and can track order activity.

#### Acceptance Criteria

1. THE Notification_Service SHALL be a Node.js application located in a `notification/` directory at the project root
2. THE Notification_Service SHALL consume `order-placed` events from the Kafka `order-events` topic using the KafkaJS library
3. WHEN an `order-placed` event is received, THE Notification_Service SHALL create a notification document in MongoDB with fields: `userId` (String), `message` (String), `status` ("unread"), `type` ("ORDER_EVENT"), and `createdAt` (Date)
4. THE Notification_Service SHALL expose a `GET /api/notifications/:userId` REST endpoint that returns all notifications for a given user sorted by timestamp descending
5. THE Notification_Service SHALL expose a `GET /api/notifications/:userId/unread` REST endpoint that returns only unread notifications for a user
6. THE Notification_Service SHALL expose a `PATCH /api/notifications/:id/read` REST endpoint that marks a notification as read
7. THE Notification_Service SHALL connect to MongoDB using the Mongoose ODM library
8. THE Notification_Service SHALL be structured with `src/consumers/`, `src/controllers/`, `src/models/`, `src/routes/`, and a `src/index.js` entry point
9. THE Notification_Service SHALL use Express.js for HTTP server and routing
10. THE Notification_Service SHALL run on port 8084

### Requirement 13: Create Database Schema and Seed Scripts

**User Story:** As a developer, I want SQL schema files and seed data scripts, so that I can quickly initialize and populate databases for development.

#### Acceptance Criteria

1. THE project SHALL provide a `database/postgres/create_users.sql` file that creates the `users` table with columns: `id SERIAL PRIMARY KEY`, `name VARCHAR(100)`, `email VARCHAR(100) UNIQUE NOT NULL`, `password TEXT NOT NULL`, `role VARCHAR(50)`, `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
2. THE project SHALL provide a `database/postgres/create_products.sql` file that creates the `products` table with columns: `id SERIAL PRIMARY KEY`, `name VARCHAR(100)`, `category VARCHAR(100)`, `price DECIMAL(10,2)`, `stock INT`, `image_url TEXT`, `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
3. THE project SHALL provide a `database/postgres/create_orders.sql` file that creates the `orders` table and `order_items` table with proper foreign key references
4. THE project SHALL provide a `database/mongo/notification_schema.js` file that defines the MongoDB notification collection with a JSON schema validator
5. THE project SHALL provide a `database/mongo/seed_notifications.js` file that inserts sample notification documents
6. THE project SHALL provide a `database/seed/insert_sample_users.sql` file with sample user records
7. THE project SHALL provide a `database/seed/insert_sample_products.sql` file with 20 product records using imgur-hosted image URLs matching the Product_Catalog
8. THE project SHALL provide a `database/seed/notifications_seed.json` file with sample notification documents

### Requirement 14: Create Postman API Collection

**User Story:** As a developer, I want a Postman collection covering all service endpoints, so that I can quickly test and explore the API without writing code.

#### Acceptance Criteria

1. THE project SHALL provide a Postman collection file at `postman/nextgadget_collection.json`
2. THE Postman_Collection SHALL include requests for all User_Service endpoints (register, login)
3. THE Postman_Collection SHALL include requests for all Product_Service endpoints (list, get by ID, search by category, search by name, create, update, delete)
4. THE Postman_Collection SHALL include requests for all Order_Service endpoints (place order, get order by ID, get orders by user ID)
5. THE Postman_Collection SHALL include requests for all Notification_Service endpoints (get notifications, get unread, mark as read)
6. THE Postman_Collection SHALL use a `{{baseUrl}}` variable defaulting to `http://localhost:8080` for the Gateway
7. THE Postman_Collection SHALL include an `{{authToken}}` variable for Bearer token authentication in protected endpoints
