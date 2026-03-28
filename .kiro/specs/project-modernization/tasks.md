# Tasks


## Task 1: Update Backend Dependencies (Requirement 1)

- [x] 1.1 Upgrade JJWT from 0.11.5 to 0.12.6 in `user/pom.xml`
- [x] 1.2 Upgrade JJWT from 0.11.5 to 0.12.6 in `gateway/pom.xml`
- [x] 1.3 Add `spring-boot-starter-validation` dependency to `user/pom.xml`
- [x] 1.4 Add `spring-boot-starter-validation` dependency to `product/pom.xml`
- [x] 1.5 Add `spring-boot-starter-validation` dependency to `order/pom.xml`
- [x] 1.6 Migrate `user/.../security/JwtUtil.java` to JJWT 0.12.x API
- [x] 1.7 Migrate `user/.../security/SecurityConfig.java` to non-deprecated Spring Security lambda DSL
- [x] 1.8 Remove deprecated `database-platform` property from `product/src/main/resources/application.yml`
- [x] 1.9 Delete duplicate `order/src/main/resources/application.properties` file


## Task 2: Fix Gateway Security and Routing (Requirement 2)

- [x] 2.1 Rewrite `gateway/.../config/SecurityConfig.java` to use JJWT 0.12.x API and non-deprecated Spring Security WebFlux lambda DSL
- [x] 2.2 Add request mutation in SecurityConfig to inject `X-User-Id` and `X-User-Roles` headers after JWT authentication
- [x] 2.3 Add `GET /api/notifications/**` to public (permitAll) endpoints in SecurityConfig
- [x] 2.4 Delete `gateway/.../security/JwtAuthenticationFilter.java`
- [x] 2.5 Delete `gateway/.../security/JwtService.java`
- [x] 2.6 Add notification-service route to `gateway/src/main/resources/application.yml` with static URI `http://notification:8084` and path `/api/notifications/**`


## Task 3: Add Input Validation (Requirement 3)

- [x] 3.1 Create `RegisterRequest` DTO in user-service with `@NotBlank` on username/email/password and `@Email` on email
- [x] 3.2 Add `@Valid` annotation on `@RequestBody` parameters in `UserController`
- [x] 3.3 Add `@NotBlank` on `name` and `@Positive` on `price` in `Product` entity
- [x] 3.4 Add `@Valid` annotation on `@RequestBody` parameters in `ProductController` for create and update
- [x] 3.5 Add `@NotEmpty` on items list in `OrderRequest` DTO, `@NotNull` on productId and `@Positive` on quantity in `OrderItemDTO`
- [x] 3.6 Add `@Valid` annotation on `@RequestBody` parameter in `OrderController`


## Task 4: Add Global Error Handling (Requirement 4)

- [x] 4.1 Create `ErrorResponse` DTO (status, message, timestamp) in user-service
- [x] 4.2 Create `GlobalExceptionHandler` (`@RestControllerAdvice`) in user-service
- [x] 4.3 Create `ResourceNotFoundException` in user-service
- [x] 4.4 Create `ErrorResponse` DTO in product-service
- [x] 4.5 Create `GlobalExceptionHandler` in product-service
- [x] 4.6 Create `ResourceNotFoundException` in product-service and replace `RuntimeException` throws in `ProductService`
- [x] 4.7 Create `ErrorResponse` DTO in order-service
- [x] 4.8 Create `GlobalExceptionHandler` in order-service
- [x] 4.9 Create `ResourceNotFoundException` in order-service and replace `RuntimeException` throws in `OrderService`


## Task 5: Complete Order Service (Requirements 5, 6)

- [x] 5.1 Add `@JsonManagedReference` on `Order.items` and `@JsonBackReference` on `OrderItem.order`
- [x] 5.2 Add `findByUserIdOrderByCreatedAtDesc(Long userId)` method to `OrderRepository`
- [x] 5.3 Add `getOrdersByUserId(Long userId)` method to `OrderService`
- [x] 5.4 Add `GET /api/orders/user/{userId}` endpoint to `OrderController`
- [x] 5.5 Change `@RequestHeader("user-id")` to `@RequestHeader("X-User-Id")` in `OrderController`

## Task 6: Fix User Service Authorities (Requirement 11)

- [x] 6.1 Update `CustomUserDetails` to return `SimpleGrantedAuthority(user.getRole())` instead of empty list
- [x] 6.2 Update `JwtUtil` to include user role as a claim in the generated JWT token


## Task 7: Add Kafka Event Publishing (Requirement 10)

- [x] 7.1 Add Kafka bootstrap-servers configuration to `order/src/main/resources/application.yml`
- [x] 7.2 Create `OrderPlacedEvent` DTO (orderId, userId, totalAmount, timestamp) in order-service
- [x] 7.3 Add `KafkaTemplate` injection and event publishing to `OrderService.placeOrder()` with try-catch for Kafka failures
- [x] 7.4 Create `kafka-config/create-topics.sh` script to create the `order-events` topic
- [x] 7.5 Create `kafka-config/README.md` with Kafka setup documentation


## Task 8: Build Notification Service (Requirement 12)

- [x] 8.1 Create `notification/package.json` with dependencies (express, mongoose, kafkajs, cors, dotenv)
- [x] 8.2 Create `notification/src/models/Notification.js` Mongoose schema
- [x] 8.3 Create `notification/src/consumers/orderConsumer.js` KafkaJS consumer for `order-events` topic
- [x] 8.4 Create `notification/src/controllers/notificationController.js` with handlers for GET notifications, GET unread, PATCH mark-as-read
- [x] 8.5 Create `notification/src/routes/notificationRoutes.js` Express router
- [x] 8.6 Create `notification/src/index.js` Express app setup with Kafka consumer start and MongoDB connection
- [x] 8.7 Create `notification/Dockerfile` for Node.js container
- [x] 8.8 Create `notification/README.md` with service documentation


## Task 9: Create Database Schema and Seed Scripts (Requirement 13)

- [x] 9.1 Create `database/postgres/create_users.sql` with users table schema
- [x] 9.2 Create `database/postgres/create_products.sql` with products table schema
- [x] 9.3 Create `database/postgres/create_orders.sql` with orders and order_items table schemas
- [x] 9.4 Create `database/mongo/notification_schema.js` with MongoDB JSON schema validator
- [x] 9.5 Create `database/mongo/seed_notifications.js` with sample notification inserts
- [x] 9.6 Create `database/seed/insert_sample_users.sql` with sample user records
- [x] 9.7 Create `database/seed/insert_sample_products.sql` with 20 products using imgur-hosted image URLs
- [x] 9.8 Create `database/seed/notifications_seed.json` with sample notification documents


## Task 10: Build React Frontend (Requirements 7, 8)

- [x] 10.1 Initialize Vite + React 19 + TypeScript project in `frontend/` with `package.json`, `tsconfig.json`, `vite.config.ts`
- [x] 10.2 Install dependencies: react, react-dom, react-router-dom, axios, bootstrap, @popperjs/core
- [x] 10.3 Create `frontend/src/types/index.ts` with Product, CartItem, Order, OrderItem, Notification interfaces
- [x] 10.4 Create `frontend/src/api/client.ts` Axios instance with JWT interceptor
- [x] 10.5 Create `frontend/src/context/AuthContext.tsx` with login, register, logout, isAuthenticated, user state
- [x] 10.6 Create `frontend/src/context/CartContext.tsx` with localStorage-backed cart state
- [x] 10.7 Create `frontend/src/styles/index.css` with CSS custom properties, Bootstrap overrides, hover effects, rounded inputs
- [x] 10.8 Create `frontend/index.html` with Roboto Google Fonts import and root div
- [x] 10.9 Create `frontend/src/main.tsx` importing Bootstrap CSS, custom styles, and rendering App
- [x] 10.10 Create `frontend/src/components/Navbar.tsx` dark navbar with NextGadget brand, nav links, cart badge
- [x] 10.11 Create `frontend/src/components/ProductCard.tsx` Bootstrap card with image hover scale effect
- [x] 10.12 Create `frontend/src/components/CartItem.tsx` cart line item with quantity controls
- [x] 10.13 Create `frontend/src/components/OrderSummary.tsx` order details display
- [x] 10.14 Create `frontend/src/components/LoadingSpinner.tsx`
- [x] 10.15 Create `frontend/src/components/ProtectedRoute.tsx` redirect to login if not authenticated
- [x] 10.16 Create `frontend/src/pages/HomePage.tsx` hero section with green accent and featured products
- [x] 10.17 Create `frontend/src/pages/ProductListPage.tsx` product grid with category filter and name search
- [x] 10.18 Create `frontend/src/pages/ProductDetailPage.tsx` product details with add-to-cart
- [x] 10.19 Create `frontend/src/pages/CartPage.tsx` cart items list with total and checkout link
- [x] 10.20 Create `frontend/src/pages/CheckoutPage.tsx` order summary and place order
- [x] 10.21 Create `frontend/src/pages/OrderHistoryPage.tsx` past orders for logged-in user
- [x] 10.22 Create `frontend/src/pages/LoginPage.tsx` login form with rounded inputs and green accent
- [x] 10.23 Create `frontend/src/pages/RegisterPage.tsx` registration form
- [x] 10.24 Create `frontend/src/App.tsx` with React Router routes
- [x] 10.25 Create `frontend/src/components/Footer.tsx` dark-themed footer


## Task 11: Create Postman Collection (Requirement 14)

- [x] 11.1 Create `postman/nextgadget_collection.json` with Postman v2.1 format, folders for User, Product, Order, Notification services, and variables {{baseUrl}} and {{authToken}}

## Task 12: Docker Compose Orchestration (Requirement 9)

- [x] 12.1 Create `eureka/Dockerfile` multi-stage Maven build to JRE runtime
- [x] 12.2 Create `gateway/Dockerfile` multi-stage Maven build to JRE runtime
- [x] 12.3 Create `user/Dockerfile` multi-stage Maven build to JRE runtime
- [x] 12.4 Create `product/Dockerfile` multi-stage Maven build to JRE runtime
- [x] 12.5 Create `order/Dockerfile` multi-stage Maven build to JRE runtime
- [x] 12.6 Create `frontend/Dockerfile` Node build to Nginx runtime
- [x] 12.7 Create `docker-compose.yml` with all services: postgres, mongodb, zookeeper, kafka, eureka, gateway, user-service, product-service, order-service, notification, frontend with health checks, depends_on, volumes, and port mappings

## Task 13: Fix File Naming Issues (Requirement 11)

- [x] 13.1 Rename `imgaes/` directory to `images/`
- [x] 13.2 Rename `deafult-img.png` to `default-img.png` inside the images directory
